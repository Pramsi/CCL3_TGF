package com.cc221002.ccl3_tgf.ui.view_model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.cc221002.ccl3_tgf.data.CategoriesDao
import com.cc221002.ccl3_tgf.data.Category
import com.cc221002.ccl3_tgf.data.EntriesDao
import com.cc221002.ccl3_tgf.data.model.SingleEntry
import com.cc221002.ccl3_tgf.ui.view.Screen
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

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

	private var _entriesForCategory = MutableStateFlow<List<SingleEntry>>(emptyList())
	val entriesForCategory: StateFlow<List<SingleEntry>> = _entriesForCategory.asStateFlow()

	private var _entriesForIsCheckedCheck = MutableStateFlow<List<SingleEntry>>(emptyList())
	val entriesForIsCheckedCheck: StateFlow<List<SingleEntry>> = _entriesForIsCheckedCheck.asStateFlow()

	// this variable is for swipe deleting a trip (it saves for which trip the alert opens)
	private val _openAlertDialogForEntry = mutableStateOf<String?>(null)
	val openAlertDialogForEntry: State<String?> = _openAlertDialogForEntry

	private val _openEditDialogForEntry = mutableStateOf<String?>(null)
	val openEditDialogForEntry: State<String?> = _openEditDialogForEntry

	private val _openAskAmountDialogForEntry = mutableStateOf<String?>(null)
	val openAskAmountDialogForEntry: State<String?> = _openAskAmountDialogForEntry

	private val _openQuickAddingDialogFor = mutableStateOf<String?>(null)
	val openQuickAddingDialogFor: State<String?> = _openQuickAddingDialogFor

	private var _giveEntries = MutableStateFlow<List<SingleEntry>>(emptyList())
	val giveEntries: StateFlow<List<SingleEntry>> = _entriesForCategory.asStateFlow()

	// this function updates on which screen the user currently is
	fun selectScreen(screen: Screen){
		_mainViewState.update { it.copy(selectedScreen = screen) }
	}

	fun getAllCategories() {
		viewModelScope.launch {
			categoriesDao.getAllCategories().collect { categories ->
				_categories.value = categories
			}
		}
	}
	fun getDistinctCategories(): List<String> {
		return _categories.value.map{it.categoryName}.distinct()
	}

	// this function determines whether category has entries or not (background color blue or light blue?)
	fun hasEntriesInCategory(categoryId: Int): Boolean {
		return entries.value.any { it.categoryId == categoryId }
	}

	// this function calls the dao function to collect all the trips that are saved in the database
	fun getEntries() {
		viewModelScope.launch {
			dao.getEntries().collect{entries ->
				_entries.value = entries
			}
		}
	}

	// Method to set the current category
	fun setCurrentCategory(categoryName: String) {
		_currentCategory.value = categoryName
	}

	// Function to get entries by category
	fun getEntriesByCategory(categoryId: Int) {
		viewModelScope.launch {
			dao.getEntriesByCategory(categoryId).collect { entries ->
				_entriesForCategory.value = entries

				// call hasEntriesInCategory after updating entriesForCategory
				_mainViewState.update {
					it.copy(selectedScreen = Screen.ShowCategoryEntries)
				}
			}
		}
	}



	// Function to check if all entries for a category are checked
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

	fun openAddDialog(){
		_mainViewState.update{ it.copy(openAddDialog = true)}
	}
	fun dismissAddDialog(){
		_mainViewState.update{ it.copy(openAddDialog = false)}
	}

	fun openQuickAddDialog(foodName: String){
		_mainViewState.update{ it.copy(openQuickAddDialog = true)}
		_openQuickAddingDialogFor.value = foodName
	}
	fun dismissQuickAddDialog(){
		_mainViewState.update{ it.copy(openQuickAddDialog = false)}
		_openQuickAddingDialogFor.value = ""
	}

	fun saveButton(entry: SingleEntry){
		viewModelScope.launch {
			dao.insertEntry(entry)
			dismissAddDialog()
			dismissQuickAddDialog()
			getEntries()
		}
	}

	fun openEditDialog(singleEntry: SingleEntry){
		_mainViewState.update{ it.copy(openEditDialog = true, editSingleEntry = singleEntry) }
		_openEditDialogForEntry.value = singleEntry.id.toString()

	}

	fun saveEditedEntry(singleEntry: SingleEntry){
		dismissEditDialog()
		viewModelScope.launch {
			dao.updateEntry(singleEntry)
			getEntries()
		}
	}

	fun dismissEditDialog(){
		_mainViewState.update{ it.copy(openEditDialog = false) }
		_openEditDialogForEntry.value = ""

	}

	fun openAskAmountDialog(singleEntry: SingleEntry){
		_mainViewState.update{ it.copy(openAskAmountDialog = true, editSingleEntry = singleEntry)}
		_openAskAmountDialogForEntry.value = singleEntry.id.toString()
	}

	fun dismissAskAmountDialog(){
		_mainViewState.update{ it.copy(openAskAmountDialog = false)}
		_openAskAmountDialogForEntry.value = ""
	}


	// this function opens the alertDialog
	fun openAlertDialog(entryId: String) {
		_mainViewState.update { it.copy(openAlertDialog = true) }
		// and saves for which the alert is opened
		_openAlertDialogForEntry.value = entryId
	}

	// this function closes the alertDialog
	fun dismissAlertDialog(){
		_mainViewState.update { it.copy(openAlertDialog = false) }
		// and "deletes" for which trip the alert was opened
		_openAlertDialogForEntry.value = ""
	}

	fun openConfirmationDialog() {
		_mainViewState.update { it.copy(openConfirmDialog = true) }
	}

	// this function closes the alertDialog
	fun dismissConfirmationDialog(){
		_mainViewState.update { it.copy(openConfirmDialog = false) }
	}

	// this function calls the dao function to delete the entry that was passed to it
	fun deleteTrip(singleEntry: SingleEntry) {
		viewModelScope.launch() {
			dao.deleteEntry(singleEntry)
			getEntries()
			// and then navigates to the ShowAllTrips Screen
		}
	}

	fun enableFridgeView(){
		_mainViewState.update{ it.copy(fridgeView = true)}
		_mainViewState.update{ it.copy(listView = false)}
	}

	fun enableListView(){
		_mainViewState.update{ it.copy(listView = true)}
		_mainViewState.update{ it.copy(fridgeView = false)}
	}

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
		viewModelScope.launch{
			for (category in hardcodedCategory)
				categoriesDao.insertCategory(category)
		}
	}
}