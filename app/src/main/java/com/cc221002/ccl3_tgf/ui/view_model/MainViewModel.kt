package com.cc221002.ccl3_tgf.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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


	// this variable is a list of all the entries
	private val _entries = MutableStateFlow<List<SingleEntry>>(emptyList())
	val entries: StateFlow<List<SingleEntry>> = _entries.asStateFlow()

	private var _entriesForCategory = MutableStateFlow<List<SingleEntry>>(emptyList())
	val entriesForCategory: StateFlow<List<SingleEntry>> = _entriesForCategory.asStateFlow()

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

	// this function determines whether category has entries or not (background color blue or light blue?)
	fun hasEntriesInCategory(categoryId: Int) : Boolean {
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

	// Function to get entries by category
	fun getEntriesByCategory(categoryId: Int) {
		viewModelScope.launch {
			dao.getEntriesByCategory(categoryId).collect { entries ->
				_entriesForCategory.value = entries
				Log.d("CATEGORY","$categoryId")
				Log.d("ENTRIES","$entries")

				// Handle retrieved entries by category

				// call hasEntriesInCategory after updating entriesForCategory
				_mainViewState.update {
					it.copy(selectedScreen = Screen.ShowCategoryEntries)
				}
			}
		}
	}

	fun insertCategories(){
		val hardcodedCategory = listOf(
			Category("Leftovers"),
			Category("Drinks"),
			Category("Dairy"),
			Category("Extras"),
			Category("Meat"),
			Category("Fruit"),
			Category("Vegetable"),
			)
		viewModelScope.launch{
			for (category in hardcodedCategory)
				categoriesDao.insertCategory(category)
		}
	}

	fun insertPreTrips(){
        val hardcodedSamples = listOf(
            SingleEntry("ChickenNuggets","24.10.2022", 1, 4, "Portions", 0),
	        SingleEntry("Avocados","24.10.2022", 6, 2, "Pieces", 0),
	        SingleEntry("Pasta Aciutta","5.10.2022", 1, 2, "Portions", 0),
	        SingleEntry("AppleJuice","24.10.2022", 2, 4, "Glasses", 0),
	        SingleEntry("Milk","24.10.2022", 3, 1, "Glasses", 0),
	        SingleEntry("Filet","24.10.2022", 5, 1, "Pieces", 0),
	        SingleEntry("Banana","24.10.2022", 6, 2, "Pieces", 0),
	        SingleEntry("Carrot","24.10.2022", 7, 8, "Pieces", 0),

        )
        viewModelScope.launch{
            for (entry in hardcodedSamples)
                dao.insertEntry(entry)
        }
    }



}