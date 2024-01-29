package com.cc221002.ccl3_tgf.ui.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221002.ccl3_tgf.data.CategoriesDao
import com.cc221002.ccl3_tgf.data.model.Category
import com.cc221002.ccl3_tgf.data.EntriesDao
import com.cc221002.ccl3_tgf.data.model.SingleEntry
import com.cc221002.ccl3_tgf.ui.view.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel (
	private val dao: EntriesDao,
	private val categoriesDao: CategoriesDao
) : ViewModel() {

	// in those variables the states are saved
	private val _mainViewState = MutableStateFlow(MainViewState())
	val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

	// State for categories
	private val _categories = MutableStateFlow<List<Category>>(emptyList())
	val categories: StateFlow<List<Category>> = _categories.asStateFlow()

	// Get the current Category the user is on
	private val _currentCategory = mutableStateOf<String?>(null)
	val currentCategory: String?
		get() = _currentCategory.value

	// this variable is a list of all the entries
	private val _entries = MutableStateFlow<List<SingleEntry>>(emptyList())
	val entries: StateFlow<List<SingleEntry>> = _entries.asStateFlow()

	// this variable is a list of all the entries of one Category
	private var _entriesForCategory = MutableStateFlow<List<SingleEntry>>(emptyList())
	val entriesForCategory: StateFlow<List<SingleEntry>> = _entriesForCategory.asStateFlow()

	// the next four variables save the entry for which each dialog opens
	private val _openAlertDialogForEntry = mutableStateOf<String?>(null)
	val openAlertDialogForEntry: State<String?> = _openAlertDialogForEntry

	private val _openEditDialogForEntry = mutableStateOf<String?>(null)
	val openEditDialogForEntry: State<String?> = _openEditDialogForEntry

	private val _openAskAmountDialogForEntry = mutableStateOf<String?>(null)
	val openAskAmountDialogForEntry: State<String?> = _openAskAmountDialogForEntry

	private val _openQuickAddingDialogFor = mutableStateOf<String?>(null)
	val openQuickAddingDialogFor: State<String?> = _openQuickAddingDialogFor

	// this function updates on which screen the user currently is
	fun selectScreen(screen: Screen){
		_mainViewState.update { it.copy(selectedScreen = screen) }
	}

	// this part gets all the categories from the datatable
	fun getAllCategories() {
		viewModelScope.launch {
			withContext(Dispatchers.Main) {
				categoriesDao.getAllCategories().collect { categories ->
					_categories.value = categories
				}
			}
		}
	}

	// this function gets the distinct category names for the dropdown
	fun getDistinctCategories(): List<String> {
		return _categories.value.map{it.categoryName}.distinct()
	}

	// this function calls the dao function to collect all the entries that are saved in the database
	fun getEntries() {
		viewModelScope.launch {
			withContext(Dispatchers.Main) {
				dao.getEntries().collect { entries ->
					_entries.value = entries
				}
			}
		}
	}

	// Method to set the current category the user is in
	fun setCurrentCategory(categoryName: String) {
		_currentCategory.value = categoryName
	}

	// Function to get entries by category
	fun getEntriesByCategory(categoryId: Int) {
		viewModelScope.launch {
			withContext(Dispatchers.Main) {

				dao.getEntriesByCategory(categoryId).collect { entries ->
					withContext(Dispatchers.Main) {
						_entriesForCategory.value = (emptyList())

						_entriesForCategory.value = entries

						_mainViewState.update {
							it.copy(selectedScreen = Screen.ShowCategoryEntries)
						}
					}
				}
			}
		}
	}

	// Function to check if all entries for a category are checked (returns true if that is the case)
	fun areAllEntriesChecked(categoryId: Int): Boolean {
		if(categoryId == 0){
			val allEntries = entries.value
			return allEntries.all { it.isChecked != 0 }
		} else {
			val entriesForCategory = entries.value.filter { it.categoryId == categoryId }
			return entriesForCategory.all { it.isChecked != 0 }
		}
	}

	// Function to check if there are any entries for a category
	fun hasEntriesForCategory(categoryId: Int): Boolean {
		return entries.value.any { it.categoryId == categoryId }
	}

	// those two functions manage opening and closing the adding Dialog
	fun openAddDialog(){
		_mainViewState.update{ it.copy(openAddDialog = true)}
	}
	fun dismissAddDialog(){
		_mainViewState.update{ it.copy(openAddDialog = false)}
	}

	// those two functions manage opening and closing the quick adding Dialog
	fun openQuickAddDialog(foodName: String){
		_mainViewState.update{ it.copy(openQuickAddDialog = true)}
		_openQuickAddingDialogFor.value = foodName
	}
	fun dismissQuickAddDialog(){
		_mainViewState.update{ it.copy(openQuickAddDialog = false)}
		_openQuickAddingDialogFor.value = ""
	}

	// this function inserts the entry it gets sent into the database and closes the Dialogs
	fun saveButton(entry: SingleEntry){
		viewModelScope.launch {
			withContext(Dispatchers.Main) {
				dao.insertEntry(entry)
				dismissAddDialog()
				dismissQuickAddDialog()
				getEntries()
			}
		}
	}

	// those two functions manage opening and closing the editing Dialog
	fun openEditDialog(singleEntry: SingleEntry){
		_mainViewState.update{ it.copy(openEditDialog = true, editSingleEntry = singleEntry) }
		// it saves for which entry the edit is opened
		_openEditDialogForEntry.value = singleEntry.id.toString()
	}
	fun dismissEditDialog(){
		_mainViewState.update{ it.copy(openEditDialog = false) }
		// it "deletes" for which entry the edit was opened
		_openEditDialogForEntry.value = ""

	}
	// this function update the information of an entry in the database and closes the dialog
	fun saveEditedEntry(singleEntry: SingleEntry){
		dismissEditDialog()
		viewModelScope.launch {
			withContext(Dispatchers.Main) {
				dao.updateEntry(singleEntry)
				getEntries()
			}
		}
		_mainViewState.update{ it.copy(editSingleEntry = SingleEntry("","",0,"","",0, ""),)}

	}

	// those two functions manage opening and closing the Dialog to ask how much the user used of the item
	fun openAskAmountDialog(singleEntry: SingleEntry){
		_mainViewState.update{ it.copy(openAskAmountDialog = true, editSingleEntry = singleEntry)}
		_openAskAmountDialogForEntry.value = singleEntry.id.toString()
	}
	fun dismissAskAmountDialog(){
		_mainViewState.update{ it.copy(openAskAmountDialog = false, editSingleEntry = SingleEntry("","",0,"","",0, ""))}
		_openAskAmountDialogForEntry.value = ""
	}

	// those two functions manage opening and closing the adding Dialog
		fun openAlertDialog(entryId: String) {
		_mainViewState.update { it.copy(openAlertDialog = true) }
		// it saves for which the alert is opened
		_openAlertDialogForEntry.value = entryId
	}
	fun dismissAlertDialog(){
		_mainViewState.update { it.copy(openAlertDialog = false) }
		// it "deletes" for which entry the alert was opened
		_openAlertDialogForEntry.value = ""
	}

	// those two functions manage opening and closing the confirmation Dialog
	fun openConfirmationDialog() {
		_mainViewState.update { it.copy(openConfirmDialog = true) }
	}

	fun dismissConfirmationDialog(){
		_mainViewState.update { it.copy(openConfirmDialog = false) }
	}

	// this function calls the dao function to delete the entry that was passed to it
	fun deleteTrip(singleEntry: SingleEntry) {
		viewModelScope.launch() {
			withContext(Dispatchers.Main) {
				dao.deleteEntry(singleEntry)
				getEntries()
				// and then navigates to the ShowAllTrips Screen
			}
		}
	}

	// the next two functions handle which view of the Fridge is active
	fun enableFridgeView(){
		_mainViewState.update{ it.copy(fridgeView = true)}
		_mainViewState.update{ it.copy(listView = false)}
	}

	fun enableListView(){
		_mainViewState.update{ it.copy(listView = true)}
		_mainViewState.update{ it.copy(fridgeView = false)}
	}


	// this function creates the predefined category list and saves it into the database
	fun insertCategories() {
		val hardcodedCategory = listOf(
			Category("Leftovers"),
			Category("Drinks"),
			Category("Dairy"),
			Category("Extras"),
			Category("Meat"),
			Category("Fruit"),
			Category("Vegetables"),
			)
		viewModelScope.launch {
			withContext(Dispatchers.Main) {
				for (category in hardcodedCategory)
					categoriesDao.insertCategory(category)
			}
		}
	}
}