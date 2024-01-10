package com.cc221002.ccl3_tgf.ui.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Divider
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cc221002.ccl3_tgf.R
import com.cc221002.ccl3_tgf.ui.theme.BackgroundBlue
import com.cc221002.ccl3_tgf.ui.theme.NavigationBlue
import com.cc221002.ccl3_tgf.ui.view_model.MainViewModel
import kotlinx.coroutines.delay
import java.io.File
import java.util.concurrent.ExecutorService


// creating a sealed class for the Screens to navigate between
sealed class Screen(val route: String) {
	object SplashScreen: Screen("splashScreen")
	object ShowCategories: Screen("categories")
	object News: Screen("news")
}

// this is the MainView Composable which is the first thing i navigate from the MainActivity
// here the routes of the screens are defined
@Composable
fun MainView(
	mainViewModel: MainViewModel,
) {
	val state = mainViewModel.mainViewState.collectAsState()

	// creating instances of the ViewModels and the NavController
	val navController = rememberNavController()
	var showBottomBar by rememberSaveable { mutableStateOf(true) }
	val navBackStackEntry by navController.currentBackStackEntryAsState()

	showBottomBar = when (navBackStackEntry?.destination?.route) {
		"splashScreen" -> false // on this screen bottom bar should be hidden
		else -> true // in all other cases show bottom bar
	}
	// defining the routes to each Screen and what happens when that route is used
	Scaffold(
		bottomBar = { if(showBottomBar)BottomNavigationBar(navController, state.value.selectedScreen) }
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
				mainViewModel.getAllCategories()
				AllCategories(
					mainViewModel,
					navController
				)
			}
		}
	}
}

@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen){

		BottomNavigation(
			backgroundColor = NavigationBlue,
			elevation = 8.dp,

		) {

			NavigationBarItem(
				selected = (selectedScreen == Screen.News),
				onClick = { navController.navigate(Screen.News.route) },
				icon = { Image(
					painter = painterResource(id = R.drawable.lightbulb_icon),
					contentDescription = "News",
					contentScale = ContentScale.Fit,
					modifier = Modifier
						.size(35.dp)
				) })

			NavigationBarItem(
				selected = (selectedScreen == Screen.ShowCategories),
				onClick = { navController.navigate(Screen.ShowCategories.route) },
				icon = { Image(
					painter = painterResource(id = R.drawable.fridge_icon),
					contentDescription = "Fridge",
					contentScale = ContentScale.Fit,
					modifier = Modifier
						.size(35.dp)
				) })
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
	val categories by mainViewModel.categories.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxSize(),
		verticalArrangement = Arrangement.SpaceEvenly,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Row(
			modifier = Modifier
				.clip(shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 20.dp))
				.background(NavigationBlue)
				.fillMaxWidth()
				.height(75.dp)
			,
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		){
			Text(
				text = "Your Fridge",
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold,
				color = Color.White
			)
		}

		LazyColumn(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			item {
				val leftovers = categories.find { it.categoryName == "Leftovers" }

				leftovers?.let {
					Box(
						modifier = Modifier.fillMaxWidth(),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = it.categoryName,
							modifier = Modifier
								.fillMaxWidth()
								.clickable {
									// Handle click action for Leftovers category
								}
								.padding(16.dp),
							textAlign = TextAlign.Center
						)
					}
				}
			}

			item {
				Box(
					modifier = Modifier.fillMaxWidth(),
					contentAlignment = Alignment.Center
				) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.spacedBy(8.dp)
					) {
						val drinks = categories.find { it.categoryName == "Drinks" }
						val dairy = categories.find { it.categoryName == "Dairy" }

						drinks?.let {
							Text(
								text = it.categoryName,
								modifier = Modifier
									.weight(1f)
									.clickable {
										// Handle click action for Drinks category
									}
									.padding(16.dp),
								textAlign = TextAlign.Center
							)
						}

						dairy?.let {
							Text(
								text = it.categoryName,
								modifier = Modifier
									.weight(1f)
									.clickable {
										// Handle click action for Dairy category
									}
									.padding(16.dp),
								textAlign = TextAlign.Center
							)
						}
					}
				}
			}

			item {
				val extras = categories.find { it.categoryName == "Extras" }
				val meat = categories.find { it.categoryName == "Meat" }

				extras?.let {
					Text(
						text = it.categoryName,
						modifier = Modifier
							.fillMaxWidth()
							.clickable {
								// Handle click action for Extras category
							}
							.padding(16.dp),
						textAlign = TextAlign.Center
					)
				}

				meat?.let {
					Text(
						text = it.categoryName,
						modifier = Modifier
							.fillMaxWidth()
							.clickable {
								// Handle click action for Meat category
							}
							.padding(16.dp),
						textAlign = TextAlign.Center
					)
				}
			}

			item {
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(8.dp)
				) {
					val fruit = categories.find { it.categoryName == "Fruit" }
					val vegetable = categories.find { it.categoryName == "Vegetable" }

					fruit?.let {
						Text(
							text = it.categoryName,
							modifier = Modifier
								.weight(1f)
								.clickable {
									// Handle click action for Fruit category
								}
								.padding(16.dp),
							textAlign = TextAlign.Center
						)
					}

					vegetable?.let {
						Text(
							text = it.categoryName,
							modifier = Modifier
								.weight(1f)
								.clickable {
									// Handle click action for Vegetable category
								}
								.padding(16.dp),
							textAlign = TextAlign.Center
						)
					}
				}
			}
		}
	}

}


