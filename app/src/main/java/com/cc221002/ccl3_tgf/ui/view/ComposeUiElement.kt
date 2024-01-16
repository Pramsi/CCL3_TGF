package com.cc221002.ccl3_tgf.ui.view

import android.graphics.BlurMaskFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cc221002.ccl3_tgf.R
import com.cc221002.ccl3_tgf.data.model.SingleEntry
import com.cc221002.ccl3_tgf.ui.theme.BackgroundBlue
import com.cc221002.ccl3_tgf.ui.theme.BackgroundLightBlue
import com.cc221002.ccl3_tgf.ui.theme.FridgeBlue
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
	object ShowCategoryEntries: Screen("showCategoryEntries")
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
			composable(Screen.ShowCategoryEntries.route) {
				mainViewModel.selectScreen(Screen.ShowCategoryEntries)
				categoryEntries(navController, mainViewModel)
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
fun AllCategories (
	mainViewModel: MainViewModel,
	navController: NavHostController,
) {
	val categories by mainViewModel.categories.collectAsState()

	mainViewModel.getEntries()

	Column(
		modifier = Modifier
			.background(White)
			.fillMaxSize(),
		verticalArrangement = Arrangement.SpaceEvenly,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Row(
			modifier = Modifier
				.clip(shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 20.dp))
				.background(NavigationBlue)
				.fillMaxWidth()
				.height(75.dp),
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

		// fridge box containing all the categories
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 45.dp)
				.clip(RoundedCornerShape(16.dp))
				.background(FridgeBlue),
			contentAlignment = Alignment.Center
		) {
			LazyColumn(
				modifier = Modifier
					.fillMaxSize()
					.padding(start = 10.dp, top = 45.dp, end = 10.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {


				item {
					val leftovers = categories.find { it.categoryName == "Leftovers" }

					leftovers?.let {
						Box(
							modifier = Modifier
								.shadow(
									color = Color(0x51000000),
									borderRadius = 6.dp,
									blurRadius = 6.dp,
									offsetY = 4.dp,
									spread = 2f.dp
								)
								.fillMaxWidth()
								.clip(RoundedCornerShape(6.dp))
								.background(
									if (mainViewModel.hasEntriesInCategory(leftovers.id)) BackgroundBlue else BackgroundLightBlue,
								),
							contentAlignment = Alignment.Center
						) {
							Text(
								text = it.categoryName,
								modifier = Modifier
									.fillMaxWidth()
									.clickable {
										mainViewModel.getEntriesByCategory(it.id)
										navController.navigate(Screen.ShowCategoryEntries.route)
										// Handle click action for Leftovers category
									}
									.padding(30.dp),
								textAlign = TextAlign.Center,
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold,
								color = Color.White
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
											mainViewModel.getEntriesByCategory(it.id)
											navController.navigate(Screen.ShowCategoryEntries.route)
											// Handle click action for Drinks category
										}
										.clip(RoundedCornerShape(6.dp))
										.background(
											if (mainViewModel.hasEntriesInCategory(drinks.id)) BackgroundBlue else BackgroundLightBlue,
										)
										.padding(30.dp),
									textAlign = TextAlign.Center,
									fontSize = 20.sp,
									fontWeight = FontWeight.Bold,
									color = Color.White
								)
							}

							dairy?.let {
								Text(
									text = it.categoryName,
									modifier = Modifier
										.weight(1f)
										.clickable {
											mainViewModel.getEntriesByCategory(it.id)
											navController.navigate(Screen.ShowCategoryEntries.route)
											// Handle click action for Dairy category
										}
										.clip(RoundedCornerShape(6.dp))
										.background(
											if (mainViewModel.hasEntriesInCategory(dairy.id)) BackgroundBlue else BackgroundLightBlue,
										)
										.padding(30.dp),
									textAlign = TextAlign.Center,
									fontSize = 20.sp,
									fontWeight = FontWeight.Bold,
									color = Color.White
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
									mainViewModel.getEntriesByCategory(it.id)
									navController.navigate(Screen.ShowCategoryEntries.route)
									// Handle click action for Extras category
								}
								.clip(RoundedCornerShape(6.dp))
								.background(
									if (mainViewModel.hasEntriesInCategory(extras.id)) BackgroundBlue else BackgroundLightBlue,
								)
								.padding(30.dp),
							textAlign = TextAlign.Center,
							fontSize = 20.sp,
							fontWeight = FontWeight.Bold,
							color = Color.White
						)
					}

					Spacer(modifier = Modifier.height(8.dp))

					meat?.let {
						Text(
							text = it.categoryName,
							modifier = Modifier
								.fillMaxWidth()
								.clickable {
									mainViewModel.getEntriesByCategory(it.id)
									navController.navigate(Screen.ShowCategoryEntries.route)
									// Handle click action for Meat category
								}
								.clip(RoundedCornerShape(6.dp))
								.background(
									if (mainViewModel.hasEntriesInCategory(meat.id)) BackgroundBlue else BackgroundLightBlue,
								)
								.padding(30.dp),
							textAlign = TextAlign.Center,
							fontSize = 20.sp,
							fontWeight = FontWeight.Bold,
							color = Color.White
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
										mainViewModel.getEntriesByCategory(it.id)
										navController.navigate(Screen.ShowCategoryEntries.route)
										// Handle click action for Fruit category
									}
									.clip(RoundedCornerShape(6.dp))
									.background(
										if (mainViewModel.hasEntriesInCategory(fruit.id)) BackgroundBlue else BackgroundLightBlue,
									)
									.padding(30.dp),
								textAlign = TextAlign.Center,
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold,
								color = Color.White
							)
						}

						vegetable?.let {
							Text(
								text = it.categoryName,
								modifier = Modifier
									.weight(1f)
									.clickable {
										mainViewModel.getEntriesByCategory(it.id)
										navController.navigate(Screen.ShowCategoryEntries.route)
										// Handle click action for Vegetable category
									}
									.clip(RoundedCornerShape(6.dp))
									.background(
										if (mainViewModel.hasEntriesInCategory(vegetable.id)) BackgroundBlue else BackgroundLightBlue,
									)
									.padding(30.dp),
								textAlign = TextAlign.Center,
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold,
								color = Color.White
							)
						}
					}
				}
			}
		}
	}
}

@Composable
fun categoryEntries(navController: NavHostController,mainViewModel: MainViewModel){

	val entries = mainViewModel.entriesForCategory.collectAsState()

	Log.d("CategoryEntries", entries.value.toString())

	LazyColumn(
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.background(FridgeBlue),
	){
		items(entries.value){entry->
			ItemUI(entry = entry)
		}
	}
}

@Composable
fun ItemUI(entry:SingleEntry) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(10.dp)
				.clip(RoundedCornerShape(10.dp))
				.background(BackgroundBlue)
				,
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically
		) {
			Spacer(modifier = Modifier.padding(7.dp))
			Box(
				modifier = Modifier
					.size(25.dp)
					.border(BorderStroke(1.dp, White), RoundedCornerShape(3.dp))
			)
			Spacer(modifier = Modifier.padding(10.dp))
			Column(
				modifier = Modifier
					.padding(vertical = 12.dp)
					,

			){
				Text(
					text = "${entry.foodName}",
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Start,
					fontSize = 17.sp,
					style = TextStyle(fontFamily = FontFamily.Monospace),
					color = Color.White,
					modifier = Modifier
						.padding(bottom = 10.dp)
						.width(250.dp)
				)
				Text(
					text = "${entry.portionAmount} ${entry.portionType}",
					textAlign = TextAlign.Start,
					fontSize = 12.sp,
					style = TextStyle(fontFamily = FontFamily.Monospace),
					color = Color.White,
					modifier = Modifier
						.padding(bottom = 5.dp)
						.width(250.dp)
				)
				Text(
					text = "Best Before: ${entry.bbDate}",
					textAlign = TextAlign.Start,
					fontSize = 12.sp,
					style = TextStyle(fontFamily = FontFamily.Monospace),
					color = Color.White,
					modifier = Modifier
						.padding(bottom = 5.dp)
						.width(250.dp)
				)
			}
			Spacer(modifier = Modifier.padding(7.dp))
			Box(
				modifier = Modifier
					.size(25.dp)
			){
				Image(
					painter = painterResource(id = R.drawable.delete_icon),
					contentDescription = "Fridge",
					contentScale = ContentScale.Fit,
					modifier = Modifier
						.size(35.dp)
				)
			}
		}
	}

fun Modifier.shadow(
	color: Color = Color.Black,
	borderRadius: Dp = 0.dp,
	blurRadius: Dp = 0.dp,
	offsetY: Dp = 0.dp,
	offsetX: Dp = 0.dp,
	spread: Dp = 0f.dp,
	modifier: Modifier = Modifier
) = this.then(
	modifier.drawBehind {
		this.drawIntoCanvas {
			val paint = Paint()
			val frameworkPaint = paint.asFrameworkPaint()
			val spreadPixel = spread.toPx()
			val leftPixel = (0f - spreadPixel) + offsetX.toPx()
			val topPixel = (0f - spreadPixel) + offsetY.toPx()
			val rightPixel = (this.size.width + spreadPixel)
			val bottomPixel = (this.size.height + spreadPixel)

			if (blurRadius != 0.dp) {
				frameworkPaint.maskFilter =
					(BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
			}

			frameworkPaint.color = color.toArgb()
			it.drawRoundRect(
				left = leftPixel,
				top = topPixel,
				right = rightPixel,
				bottom = bottomPixel,
				radiusX = borderRadius.toPx(),
				radiusY = borderRadius.toPx(),
				paint
			)
		}
	}
)


// https://github.com/Debdutta-Panda/CustomShadow/blob/master/app/src/main/java/com/debduttapanda/customshadow/MainActivity.kt



