package com.cc221002.ccl3_tgf.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cc221002.ccl3_tgf.data.EntriesDao
import com.cc221002.ccl3_tgf.data.model.SingleEntry
import com.cc221002.ccl3_tgf.ui.view.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val dao: EntriesDao):ViewModel() {
	// in those variables the states are saved
	private val _mainViewState = MutableStateFlow(MainViewState())
	val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()


	// this variable is a list of all the entries
	private val _entries = MutableStateFlow<List<SingleEntry>>(emptyList())
	val entries: StateFlow<List<SingleEntry>> = _entries.asStateFlow()

	// this function updates on which screen the user currently is
	fun selectScreen(screen: Screen){
		_mainViewState.update { it.copy(selectedScreen = screen) }
	}


	// this function calls the dao function to collect all the trips that are saved in the database
	fun getEntries() {
		viewModelScope.launch {
			dao.getEntries().collect{entries ->
				_entries.value = entries
			}
		}
	}


	fun insertPreTrips(){
        val hardcodedSamples = listOf(
            SingleEntry("ChickenNuggets","24.10.2022", "Leftovers", 4, "Portions", 0),
	        SingleEntry("Avocados","24.10.2022", "Fruit", 2, "Pieces", 0),

        )
        viewModelScope.launch{
            for (entry in hardcodedSamples)
                dao.insertEntry(entry)
        }
    }



}