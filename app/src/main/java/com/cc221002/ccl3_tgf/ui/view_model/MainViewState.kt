package com.cc221002.ccl3_tgf.ui.view_model

import com.cc221002.ccl3_tgf.data.model.SingleEntry
import com.cc221002.ccl3_tgf.ui.view.Screen

// this class defines all the states that are used in the MainViewModel

data class MainViewState (
	// a list where all the Entries will be saved
	val entries: List<SingleEntry> = emptyList(),
	// when editing an Entry the information from the database gets saved here and is then used for displaying it
	val editSingleEntry: SingleEntry = SingleEntry("","",0,0,"",0),

	// this one saves on which screen the user is
	val selectedScreen: Screen = Screen.SplashScreen,

	val openEditDialog: Boolean = false,
	val openAddDialog: Boolean = false,


	)