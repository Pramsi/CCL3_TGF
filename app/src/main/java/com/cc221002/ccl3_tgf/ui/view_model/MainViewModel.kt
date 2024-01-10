package com.cc221002.ccl3_tgf.ui.view_model

import androidx.lifecycle.ViewModel
import com.cc221002.ccl3_tgf.data.EntriesDao
import com.cc221002.ccl3_tgf.ui.view.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(private val dao: EntriesDao):ViewModel() {
	// in those variables the states are saved
	private val _mainViewState = MutableStateFlow(MainViewState())
	val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

	// this function updates on which screen the user currently is
	fun selectScreen(screen: Screen){
		_mainViewState.update { it.copy(selectedScreen = screen) }
	}


}