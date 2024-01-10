package com.cc221002.ccl3_tgf.ui.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cc221002.ccl3_tgf.R
import com.cc221002.ccl3_tgf.ui.theme.BackgroundBlue
import com.cc221002.ccl3_tgf.ui.view_model.MainViewModel
import kotlinx.coroutines.delay
import java.io.File
import java.util.concurrent.ExecutorService


// creating a sealed class for the Screens to navigate between
sealed class Screen(val route: String) {
	object SplashScreen: Screen("splashScreen")
	object ShowCategories: Screen("categories")
}

// this is the MainView Composable which is the first thing i navigate from the MainActivity
// here the routes of the screens are defined
@Composable
fun MainView(
	mainViewModel: MainViewModel,
) {
	// creating instances of the ViewModels and the NavController
	val navController = rememberNavController()

	// defining the routes to each Screen and what happens when that route is used
	Scaffold(
	) {
		NavHost(
			navController = navController,
			modifier = Modifier.padding(it),
			// the starting screen is the SplashScreen
			startDestination = Screen.SplashScreen.route
		) {
			composable(Screen.SplashScreen.route) {
				mainViewModel.selectScreen(Screen.SplashScreen)
				SplashScreen(
					navController,
				)
			}
			composable(Screen.ShowCategories.route) {
				mainViewModel.selectScreen(Screen.ShowCategories)
				mainViewModel.getEntries()
				AllCategories(
					mainViewModel,
					navController
				)
			}



		}
	}
}

// this is the composable for the SplashScreen which is the starting Destination of the app
@Composable
fun SplashScreen(
	navController: NavHostController
) {

	// creating a variable to later check if the loading is finished
	val loadingFinished = remember { mutableStateOf(false) }

	// Introduce a 2-second delay to simulate loading
	LaunchedEffect(Unit) {
		delay(2000)  // Delay for 2 seconds

		//setting the variable to true
		loadingFinished.value = true
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
	) {
		// the splashscreen itself is just a picture which is saved in res.drawable
		Image(
			painter = painterResource(id = R.drawable.tgf_splashscreen),
			contentDescription = "Splashscreen",
			contentScale = ContentScale.FillWidth,
			modifier = Modifier
				.fillMaxSize()
		)
	}

	// if the value of the variable is true it navigates to the ShowAllTrips
	if(loadingFinished.value) {
		navController.navigate(Screen.ShowCategories.route)
	}
}

@Composable
fun AllCategories(
	mainViewModel: MainViewModel,
	navController: NavHostController,
){
	// collecting the information for all the trips and creating a mutable List with that
	val entryState by mainViewModel.entries.collectAsState()
	val entries = entryState.toList()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Row {
			Box(modifier = Modifier
				.fillMaxWidth()
				.background(BackgroundBlue)
				.height(100.dp), contentAlignment = Alignment.Center
				){
						Text(text = "Leftovers", color = Color.White)
			}
		}


		// Add more Text composable elements for other fields you want to display
		// You can also format and customize the display as needed
		// For instance, you can use string templates to concatenate information

		Spacer(modifier = Modifier.height(8.dp))
	}
}

