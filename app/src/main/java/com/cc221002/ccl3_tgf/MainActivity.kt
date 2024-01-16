package com.cc221002.ccl3_tgf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.cc221002.ccl3_tgf.data.EntriesDatabase
import com.cc221002.ccl3_tgf.ui.theme.CCL3_TGFTheme
import com.cc221002.ccl3_tgf.ui.view.MainView
import com.cc221002.ccl3_tgf.ui.view_model.MainViewModel


class MainActivity : ComponentActivity() {
	//------------------
	// DATABASE
	//------------------

	// Creating a lazy Room Database
	private val db by lazy {
		Room.databaseBuilder(this, EntriesDatabase::class.java, "EntriesDatabase.db").build()
	}
	// Creating the mainViewModel
	private val mainViewModel by viewModels<MainViewModel>(
		factoryProducer = {
			object : ViewModelProvider.Factory{
				override fun <T : ViewModel> create(modelClass: Class<T>): T {
					return MainViewModel(db.dao,db.categoriesDao) as T
				}
			}
		}
	)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

//		deleteDatabase("EntriesDatabase.db")

//		mainViewModel.insertCategories()
//		mainViewModel.insertPreTrips()
		setContent {
			CCL3_TGFTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					MainView(mainViewModel = mainViewModel)
				}
			}
		}
	}
}
