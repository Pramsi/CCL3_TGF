package com.cc221002.ccl3_tgf.ui.view

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Slider
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cc221002.ccl3_tgf.R
import com.cc221002.ccl3_tgf.data.Category
import com.cc221002.ccl3_tgf.data.model.SingleEntry
import com.cc221002.ccl3_tgf.ui.theme.BackgroundBlue
import com.cc221002.ccl3_tgf.ui.theme.BackgroundLightBlue
import com.cc221002.ccl3_tgf.ui.theme.ExpiredRed
import com.cc221002.ccl3_tgf.ui.theme.FridgeBlue
import com.cc221002.ccl3_tgf.ui.theme.GreatJobGreen
import com.cc221002.ccl3_tgf.ui.theme.HistoryItemGray
import com.cc221002.ccl3_tgf.ui.theme.HistoryItemGreen
import com.cc221002.ccl3_tgf.ui.theme.NavigationBlue
import com.cc221002.ccl3_tgf.ui.theme.TransparentLightBlue
import com.cc221002.ccl3_tgf.ui.view_model.MainViewModel
import com.cc221002.ccl3_tgf.ui.view_model.MainViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import java.util.concurrent.ExecutorService


// creating a sealed class for the Screens to navigate between
sealed class Screen(val route: String) {
	object SplashScreen: Screen("splashScreen")
	object ShowCategories: Screen("categories")
	object Overview: Screen("overview")
	object ShowCategoryEntries: Screen("showCategoryEntries")
}

// this is the MainView Composable which is the first thing i navigate from the MainActivity
// here the routes of the screens are defined
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainView(
	mainViewModel: MainViewModel,
) {
	val state = mainViewModel.mainViewState.collectAsState()

	// creating instances of the ViewModels and the NavController
	val navController = rememberNavController()
	var showBottomBar by rememberSaveable { mutableStateOf(true) }
	var showFloatingButton by rememberSaveable { mutableStateOf(true) }

	val navBackStackEntry by navController.currentBackStackEntryAsState()

	showBottomBar = when (navBackStackEntry?.destination?.route) {
		"splashScreen" -> false
		else -> true // in all other cases show bottom bar
	}
	showFloatingButton = when (navBackStackEntry?.destination?.route) {
		"splashScreen" -> false
		"overview" -> false// on this screen bottom bar should be hidden
		else -> true // in all other cases show bottom bar
	}
	// defining the routes to each Screen and what happens when that route is used
	Scaffold(
		floatingActionButton ={
			if(showFloatingButton) {
				// Add a floating button to navigate to the AddingPopup
				FloatingActionButton(
					containerColor = NavigationBlue,
					onClick = {
						mainViewModel.openAddDialog()
					},
					modifier = Modifier
				) {
					Image(
						painter = painterResource(id = R.drawable.add_icon),
						contentDescription = "Fridge",
						contentScale = ContentScale.Fit,
						modifier = Modifier
							.size(35.dp)
					)
				}
			}
		},
		floatingActionButtonPosition = FabPosition.End,
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
				SplashScreen(navController)
			}
			composable(Screen.ShowCategories.route) {
				mainViewModel.selectScreen(Screen.ShowCategories)
				mainViewModel.getAllCategories()
				AllCategories(mainViewModel, navController)
			}
			composable(Screen.ShowCategoryEntries.route) {
				mainViewModel.selectScreen(Screen.ShowCategoryEntries)
				categoryEntries(navController, mainViewModel)
			}
			composable(Screen.Overview.route) {
				mainViewModel.selectScreen(Screen.Overview)
				mainViewModel.getEntries()
				OverviewScreen(mainViewModel, navController)
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
				selected = (selectedScreen == Screen.Overview),
				colors = NavigationBarItemColors(
					selectedIndicatorColor = FridgeBlue,
					selectedIconColor = Black,
					selectedTextColor = Black,
					unselectedIconColor = Black,
					unselectedTextColor = Black,
					disabledIconColor = Black,
					disabledTextColor = Black,
				),
				onClick = { navController.navigate(Screen.Overview.route) },
				icon = { Image(
					painter = painterResource(id = R.drawable.notification_icon),
					contentDescription = "Overview",
					contentScale = ContentScale.Fit,
					modifier = Modifier
						.size(35.dp)
				) })

			NavigationBarItem(
				selected = (selectedScreen == Screen.ShowCategories),
				colors = NavigationBarItemColors(
					selectedIndicatorColor = FridgeBlue,
					selectedIconColor = Black,
					selectedTextColor = Black,
					unselectedIconColor = Black,
					unselectedTextColor = Black,
					disabledIconColor = Black,
					disabledTextColor = Black,
				),
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
		navController.navigate(Screen.Overview.route)
	}
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllCategories (
	mainViewModel: MainViewModel,
	navController: NavHostController,
) {
	val state by mainViewModel.mainViewState.collectAsState()
	val categories by mainViewModel.categories.collectAsState()
	val entriesForCategories by mainViewModel.entriesForCategory.collectAsState()

	mainViewModel.getEntries()

	Column(
		modifier = Modifier
			.background(White)
			.fillMaxSize(),
		verticalArrangement = Arrangement.SpaceEvenly,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {

	Header(title = "Your Fridge")
		if(state.openAddDialog){
			AddingPopup(mainViewModel = mainViewModel)
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
						// Determine background color based on conditions
						val backgroundColor = if (!mainViewModel.hasEntriesForCategory(leftovers.id)) {
							// No entries for the category
							BackgroundLightBlue
						} else if (mainViewModel.areAllEntriesChecked(leftovers.id)) {
							// All entries for the category are checked
							BackgroundLightBlue
						} else {
							// Some entries are not checked
							BackgroundBlue
						}

						Box(
							modifier = Modifier
								.shadow(
									color = Color(0x950B1418),
									borderRadius = 6.dp,
									blurRadius = 4.dp,
									offsetY = 4.dp,
									spread = 1f.dp
								)
								.fillMaxWidth()
								.clip(RoundedCornerShape(6.dp))
								.background(color = backgroundColor),
							contentAlignment = Alignment.Center
						) {
							Text(
								text = it.categoryName,
								modifier = Modifier
									.fillMaxWidth()
									.clickable {
										mainViewModel.setCurrentCategory(it.categoryName)
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
								val backgroundColor = if (!mainViewModel.hasEntriesForCategory(drinks.id)) {
									// No entries for the category
									BackgroundLightBlue
								} else if (mainViewModel.areAllEntriesChecked(drinks.id)) {
									// All entries for the category are checked
									BackgroundLightBlue
								} else {
									// Some entries are not checked
									BackgroundBlue
								}
								Text(
									text = it.categoryName,
									modifier = Modifier
										.shadow(
											color = Color(0x950B1418),
											borderRadius = 6.dp,
											blurRadius = 4.dp,
											offsetY = 4.dp,
											spread = 1f.dp
										)
										.weight(1f)
										.clickable {
											mainViewModel.setCurrentCategory(it.categoryName)
											mainViewModel.getEntriesByCategory(it.id)
											navController.navigate(Screen.ShowCategoryEntries.route)
											// Handle click action for Drinks category
										}
										.clip(RoundedCornerShape(6.dp))
										.background(color = backgroundColor)
										.padding(30.dp),
									textAlign = TextAlign.Center,
									fontSize = 20.sp,
									fontWeight = FontWeight.Bold,
									color = Color.White
								)
							}

							dairy?.let {
								val backgroundColor = if (!mainViewModel.hasEntriesForCategory(dairy.id)) {
									// No entries for the category
									BackgroundLightBlue
								} else if (mainViewModel.areAllEntriesChecked(dairy.id)) {
									// All entries for the category are checked
									BackgroundLightBlue
								} else {
									// Some entries are not checked
									BackgroundBlue
								}
								Text(
									text = it.categoryName,
									modifier = Modifier
										.shadow(
											color = Color(0x950B1418),
											borderRadius = 6.dp,
											blurRadius = 4.dp,
											offsetY = 4.dp,
											spread = 1f.dp
										)
										.weight(1f)
										.clickable {
											mainViewModel.setCurrentCategory(it.categoryName)
											mainViewModel.getEntriesByCategory(it.id)
											navController.navigate(Screen.ShowCategoryEntries.route)
											// Handle click action for Dairy category
										}
										.clip(RoundedCornerShape(6.dp))
										.background(color = backgroundColor)
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
						val backgroundColor = if (!mainViewModel.hasEntriesForCategory(extras.id)) {
							// No entries for the category
							BackgroundLightBlue
						} else if (mainViewModel.areAllEntriesChecked(extras.id)) {
							// All entries for the category are checked
							BackgroundLightBlue
						} else {
							// Some entries are not checked
							BackgroundBlue
						}
						Text(
							text = it.categoryName,
							modifier = Modifier
								.shadow(
									color = Color(0x950B1418),
									borderRadius = 6.dp,
									blurRadius = 4.dp,
									offsetY = 4.dp,
									spread = 1f.dp
								)
								.fillMaxWidth()
								.clickable {
									mainViewModel.setCurrentCategory(it.categoryName)
									mainViewModel.getEntriesByCategory(it.id)
									navController.navigate(Screen.ShowCategoryEntries.route)
									// Handle click action for Extras category
								}
								.clip(RoundedCornerShape(6.dp))
								.background(color = backgroundColor)
								.padding(30.dp),
							textAlign = TextAlign.Center,
							fontSize = 20.sp,
							fontWeight = FontWeight.Bold,
							color = Color.White
						)
					}

					Spacer(modifier = Modifier.height(8.dp))

					meat?.let {
						val backgroundColor = if (!mainViewModel.hasEntriesForCategory(meat.id)) {
							// No entries for the category
							BackgroundLightBlue
						} else if (mainViewModel.areAllEntriesChecked(meat.id)) {
							// All entries for the category are checked
							BackgroundLightBlue
						} else {
							// Some entries are not checked
							BackgroundBlue
						}
						Text(
							text = it.categoryName,
							modifier = Modifier
								.shadow(
									color = Color(0x950B1418),
									borderRadius = 6.dp,
									blurRadius = 4.dp,
									offsetY = 4.dp,
									spread = 1f.dp
								)
								.fillMaxWidth()
								.clickable {
									mainViewModel.setCurrentCategory(it.categoryName)
									mainViewModel.getEntriesByCategory(it.id)
									navController.navigate(Screen.ShowCategoryEntries.route)
									// Handle click action for Meat category
								}
								.clip(RoundedCornerShape(6.dp))
								.background(color = backgroundColor)
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
						val vegetables = categories.find { it.categoryName == "Vegetables" }

						fruit?.let {
							val backgroundColor = if (!mainViewModel.hasEntriesForCategory(fruit.id)) {
								// No entries for the category
								BackgroundLightBlue
							} else if (mainViewModel.areAllEntriesChecked(fruit.id)) {
								// All entries for the category are checked
								BackgroundLightBlue
							} else {
								// Some entries are not checked
								BackgroundBlue
							}
							Text(
								text = it.categoryName,
								modifier = Modifier
									.shadow(
										color = Color(0x950B1418),
										borderRadius = 6.dp,
										blurRadius = 4.dp,
										offsetY = 4.dp,
										spread = 1f.dp
									)
									.weight(1f)
									.clickable {
										mainViewModel.setCurrentCategory(it.categoryName)
										mainViewModel.getEntriesByCategory(it.id)
										navController.navigate(Screen.ShowCategoryEntries.route)
										// Handle click action for Fruit category
									}
									.clip(RoundedCornerShape(6.dp))
									.background(color = backgroundColor)
									.padding(30.dp),
								textAlign = TextAlign.Center,
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold,
								color = Color.White
							)
						}

						vegetables?.let {
							val backgroundColor = if (!mainViewModel.hasEntriesForCategory(vegetables.id)) {
								// No entries for the category
								BackgroundLightBlue
							} else if (mainViewModel.areAllEntriesChecked(vegetables.id)) {
								// All entries for the category are checked
								BackgroundLightBlue
							} else {
								// Some entries are not checked
								BackgroundBlue
							}
							Text(
								text = it.categoryName,
								modifier = Modifier
									.shadow(
										color = Color(0x950B1418),
										borderRadius = 6.dp,
										blurRadius = 4.dp,
										offsetY = 4.dp,
										spread = 1f.dp
									)
									.weight(1f)
									.clickable {
										mainViewModel.setCurrentCategory(it.categoryName)
										mainViewModel.getEntriesByCategory(it.id)
										navController.navigate(Screen.ShowCategoryEntries.route)
										// Handle click action for Vegetable category
									}
									.clip(RoundedCornerShape(6.dp))
									.background(color = backgroundColor)
									.padding(
										start = 28.dp,
										end = 28.dp,
										top = 30.dp,
										bottom = 30.dp
									),
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun categoryEntries(navController: NavHostController,mainViewModel: MainViewModel){
	val state = mainViewModel.mainViewState.collectAsState()
	val entries = mainViewModel.entriesForCategory.collectAsState()
	val categories by mainViewModel.categories.collectAsState()
	Log.d("CategoryEntries", entries.value.toString())
	Log.d("CategoryEntries", categories.toString())

	val categoryName = mainViewModel.currentCategory

		Column(
			modifier = Modifier
				.background(White)
		) {
			Header("$categoryName")
			Box(
				modifier = Modifier
					.offset(y = (-15).dp)
					.fillMaxWidth()
					.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 45.dp)
					.clip(RoundedCornerShape(16.dp))
					.background(FridgeBlue),
				contentAlignment = Alignment.Center
			) {
				LazyColumn(
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.fillMaxSize()
//			.background(FridgeBlue),
			) {
				items(entries.value.sortedBy { it.bbDate }) { entry ->
					if(entry.isChecked == 0) {
						ItemUI(mainViewModel, entry = entry)
					}
				}
			}
			if (state.value.openAddDialog) {
				AddingPopup(mainViewModel = mainViewModel)
			}
		}
	}
}

@Composable
fun Header(title:String){

	val categoryImageMap = mapOf(
		"Leftovers" to R.drawable.leftovers_icon,
		"Drinks" to R.drawable.drinks_icon,
		"Dairy" to R.drawable.dairy_icon,
		"Extras" to R.drawable.extras_icon,
		"Meat" to R.drawable.meat_icon,
		"Fruit" to R.drawable.fruit_icon,
		"Vegetables" to R.drawable.vegetables_icon
	)

	if(title == "Your Fridge" || title == "Overview"){

		Box(
			modifier = Modifier
				.clip(shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 20.dp))
				.fillMaxWidth()
				.height(75.dp)
				.background(NavigationBlue),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = title,
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold,
				color = Color.White,
				modifier = Modifier.padding(start = 8.dp) // Adjust padding as needed
			)
		}
	} else {
		Column(
			modifier = Modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Box(
				modifier = Modifier
					.clip(shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 20.dp))
					.fillMaxWidth()
					.height(75.dp)
					.background(NavigationBlue),
				contentAlignment = Alignment.Center
			) {
				Row {
					Text(
						text = title,
						fontSize = 30.sp,
						fontWeight = FontWeight.Bold,
						color = Color.White,
						modifier = Modifier.padding(start = 8.dp) // Adjust padding as needed
					)
				}

			}
			Box(
				modifier = Modifier
					.width(100.dp)
					.height(50.dp)
					.clip(RoundedCornerShape(0.dp, 0.dp, 50.dp, 50.dp))
					.background(NavigationBlue),
				contentAlignment = Alignment.Center
			) {
				val categoryImage = categoryImageMap[title]
				// Display category image if provided
				categoryImage?.let { image ->
					Image(
						painter = painterResource(id = image),
						contentDescription = null,
						modifier = Modifier
							.size(60.dp)
							.padding(bottom = 10.dp)
					)
				}
			}
		}
	}
}




@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemUI(mainViewModel: MainViewModel,entry:SingleEntry) {
	var checkBoxState by remember { mutableStateOf(false) }
	val state = mainViewModel.mainViewState.collectAsState()

	val currentDate = LocalDate.now()
	val storedDate = runCatching { LocalDate.parse(entry.bbDate) }.getOrNull()


		Row(
			modifier = Modifier
//				.shadow(
//					color = Color(0x950B1418),
//					borderRadius = 4.dp,
//					blurRadius = 2.dp,
//					offsetY = 4.dp,
//					spread = 1.dp
//				)
				.fillMaxWidth()
				.padding(10.dp)
				.clip(RoundedCornerShape(10.dp))
				.background(
					if (storedDate != null && storedDate.isAfter(currentDate)) {
						BackgroundBlue
					} else {
						ExpiredRed
					}
				)
				.clickable { mainViewModel.editEntry(entry) },
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.CenterVertically
		) {
			Spacer(modifier = Modifier.padding(7.dp))
			Box(
				modifier = Modifier
					.size(25.dp)
					.clickable { mainViewModel }
			){
				Checkbox(
					checked = checkBoxState,
					onCheckedChange = {
						mainViewModel.openAskAmountDialog(entry)
						checkBoxState = it
						if(entry.portionAmount?.toFloatOrNull()!! >= 0) {
							checkBoxState = false
						}
									  },
					colors = CheckboxDefaults.colors(BackgroundLightBlue)
				)
			}
			Spacer(modifier = Modifier.padding(7.dp))
			Column(
				modifier = Modifier
					.padding(vertical = 10.dp),
			){
				Text(
					text = "${entry.foodName}",
					fontWeight = FontWeight.Bold,
					textAlign = TextAlign.Start,
					fontSize = 20.sp,
					style = TextStyle(fontFamily = FontFamily.Monospace),
					color = Color.White,
					modifier = Modifier
						.padding(bottom = 10.dp)
						.width(200.dp)
				)
				Text(
					text = "${entry.portionAmount} ${entry.portionType}",
					textAlign = TextAlign.Start,
					fontSize = 12.sp,
					style = TextStyle(fontFamily = FontFamily.Monospace),
					color = Color.White,
					modifier = Modifier
						.padding(bottom = 7.dp)
						.width(200.dp)
				)
				Text(
					text = "Best Before: ${entry.bbDate}",
					textAlign = TextAlign.Start,
					fontSize = 12.sp,
					style = TextStyle(fontFamily = FontFamily.Monospace),
					color = if (storedDate != null && storedDate.isAfter(currentDate)) {
						White
					} else {
						Yellow
					},
					modifier = Modifier
						.padding(bottom = 7.dp)
						.width(200.dp)
				)
			}
			Spacer(modifier = Modifier.padding(7.dp))
			Box(
				modifier = Modifier
					.padding(end = 10.dp)
					.size(25.dp)
					.clickable {
						mainViewModel.openAlertDialog(entry.id.toString())
//						mainViewModel.deleteTrip(entry)
					},
				contentAlignment = Alignment.Center
			){
				Image(
					painter = painterResource(id = R.drawable.delete_icon),
					contentDescription = "Delete",
					contentScale = ContentScale.Fit,
					modifier = Modifier
						.size(25.dp),
					colorFilter = ColorFilter.tint(White)
				)
			}
		}

	if (state.value.openAskAmountDialog) {
		AskAmountModal(mainViewModel = mainViewModel,entry = entry, checkBoxState )
	}

	if(state.value.openEditDialog){
		EditPopUp(mainViewModel = mainViewModel)
	}

	val openAlertDialogForEntry = mainViewModel.openAlertDialogForEntry.value
	if (openAlertDialogForEntry == entry.id.toString()) {
		// if the entry id from the mainViewModel is the same as the one from the entry you clicked, it shows the
		// confirmation alert to ask if the user is sure to delete the entry
		showDeleteConfirmationDialog(
			mainViewModel = mainViewModel,
			entry = entry,
			onDeleteConfirmed = {
				// if the user confirms, it closes the dialog and deletes the entry
				mainViewModel.dismissAlertDialog()
				mainViewModel.deleteTrip(entry)
			})
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun checkedItemUI(entry:SingleEntry) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp)
			.clip(RoundedCornerShape(10.dp))
			.background(
				HistoryItemGreen
			),
		horizontalArrangement = Arrangement.SpaceEvenly,
		verticalAlignment = Alignment.CenterVertically
	) {
		Spacer(modifier = Modifier.padding(7.dp))
		Box(
			modifier = Modifier
				.size(25.dp)

		){
			Checkbox(
				enabled = false,
				checked = true,
				onCheckedChange = {},
				colors = CheckboxDefaults.colors(Transparent),
			)
		}
		Spacer(modifier = Modifier.padding(7.dp))
		Column(
			modifier = Modifier
				.padding(vertical = 10.dp),
		) {
			Text(
				text = "${entry.foodName}",
				fontWeight = FontWeight.Bold,
				textAlign = TextAlign.Start,
				fontSize = 20.sp,
				style = TextStyle(fontFamily = FontFamily.Monospace),
				color = HistoryItemGray,
				modifier = Modifier
					.padding(bottom = 10.dp)
					.width(200.dp)
			)
			Text(
				text = "${entry.portionAmount} ${entry.portionType}",
				textAlign = TextAlign.Start,
				fontSize = 12.sp,
				style = TextStyle(fontFamily = FontFamily.Monospace),
				color = HistoryItemGray,
				modifier = Modifier
					.padding(bottom = 7.dp)
					.width(200.dp)
			)
			Text(
				text = "Best Before: ${entry.bbDate}",
				textAlign = TextAlign.Start,
				fontSize = 12.sp,
				style = TextStyle(fontFamily = FontFamily.Monospace),
				color = HistoryItemGray,
				modifier = Modifier
					.padding(bottom = 7.dp)
					.width(200.dp)
			)
		}
	}
}



// the Composable to define the Alert to ask for confirmation of deleting an entry
@Composable
fun showDeleteConfirmationDialog(
	mainViewModel: MainViewModel,
	entry: SingleEntry,
	onDeleteConfirmed: (SingleEntry) -> Unit
) {
	// It generally is just an AlertDialog which is dismissed when you tap beside the alert, when you press "Cancel" and when you press "Delete"
	// but on Confirm the function sends back that it was confirmed to delete that entry
	AlertDialog(
		containerColor = White,
		onDismissRequest = { mainViewModel.dismissAlertDialog() },
		title = {
			Text(
				"Delete Entry?",
				color = Black,
				fontWeight = FontWeight.Bold,
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth()
			)
		},
		text = { Text("Are you sure you want to delete this entry?", color = Black) },
		confirmButton = {
			androidx.compose.material3.Button(
				onClick = {
					onDeleteConfirmed(entry) // Perform deletion on confirmation
				},
				modifier = Modifier.padding(top = 20.dp),
				colors = ButtonDefaults.buttonColors(Transparent)
			) {
				Text("Delete", color = ExpiredRed, fontWeight = FontWeight.Bold)
			}
		},
		dismissButton = {
			androidx.compose.material3.Button(
				onClick = { mainViewModel.dismissAlertDialog() },
				colors = ButtonDefaults.buttonColors(Transparent),
				modifier = Modifier.padding(top = 20.dp)
			) {
				Text("Cancel", color = Black)
			}
		}
	)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingPopup(
	mainViewModel: MainViewModel,
){
	val categories by mainViewModel.categories.collectAsState()

	var foodName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
	var bbDate by remember { mutableStateOf("") }
	var categoryId by remember { mutableIntStateOf(0) }
	var portionAmount by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
//	var portionType by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
	var isChecked = 0
	var categorySelection by remember { mutableStateOf("") }
	var portionSelection by remember { mutableStateOf("") }
	var timeStampChecked by remember { mutableStateOf("")}
	AlertDialog(
		onDismissRequest = {
			mainViewModel.dismissAddDialog()
						   },
		modifier = Modifier
			.clip(RoundedCornerShape(20.dp))
			.background(White)
			.padding(20.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally

		) {
			Text(text = "ADD ITEM",
				lineHeight = 45.sp,
				fontWeight = FontWeight.Bold,
				fontSize = 25.sp,
				letterSpacing = 2.sp,
				style = TextStyle(fontFamily = FontFamily.SansSerif),
				color = Color.Black,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.padding(10.dp)
					.fillMaxWidth(),
				)


			TextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp)
					.shadow(3.dp, RectangleShape, false),
				colors = TextFieldDefaults.colors(
					focusedTextColor = Black,
					unfocusedTextColor = Black,
					focusedContainerColor = White,
					unfocusedContainerColor = White,
					disabledContainerColor = White,
				),
				value = foodName,
				onValueChange = {
					newText-> foodName = newText
				},
				label = {
					Text(text ="Food Name", color = Color.Gray)}
			)

			DatePickerField(selectedDate = bbDate , onDateSelected = {bbDate = it.toString()})

			CategoryDropDownMenu(mainViewModel, categorySelection){ selectedCategory->
				 categorySelection = selectedCategory
				for(category in categories){
					if(categorySelection == category.categoryName){
						categoryId = category.id
					}
				}
				Log.d("Categoryselected","$categorySelection $categoryId")
			}

			Row (
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			){
				TextField(
					modifier = Modifier
						.fillMaxWidth(0.4f)
						.shadow(3.dp, RectangleShape, false),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					trailingIcon ={Image(
						painter = painterResource(id = R.drawable.arrows_up_down_icon),
						contentDescription = "Amount",
						contentScale = ContentScale.Fit,
						modifier = Modifier
							.size(25.dp)
					)} ,
					colors = TextFieldDefaults.colors(
						focusedTextColor = Black,
						unfocusedTextColor = Black,
						focusedContainerColor = White,
						unfocusedContainerColor = White,
						disabledContainerColor = White,
					),
					value = portionAmount,
					onValueChange = {
							newText-> portionAmount = newText
					},
					label = {
						Text(text ="#", color = Color.Gray)}
				)

				PortionsDropDownMenu(mainViewModel = mainViewModel, selectedPortion = portionSelection){selectedCategory->
					portionSelection = selectedCategory
				}
			}


			Button(
				onClick = {
					mainViewModel.saveButton(SingleEntry(foodName.text, bbDate, categoryId, portionAmount.text, portionSelection, isChecked, timeStampChecked))
						  },
				modifier = Modifier.padding(top = 20.dp),
				colors = androidx.compose.material.ButtonDefaults.buttonColors(BackgroundBlue)
			) {
				Text(text = "Add", color = White )
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPopUp(
	mainViewModel: MainViewModel
){
	val state = mainViewModel.mainViewState.collectAsState()

	var foodName by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.foodName)
	}
	var bbDate by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.bbDate)
	}
	var categoryId by rememberSaveable {
		mutableIntStateOf(state.value.editSingleEntry.categoryId)
	}
	var portionAmount by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.portionAmount)
	}
	var portionType by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.portionType)
	}
	var isChecked by rememberSaveable {
		mutableIntStateOf(state.value.editSingleEntry.isChecked)
	}
	var timeStampChecked by rememberSaveable {
		mutableStateOf("")
	}

	val categories by mainViewModel.categories.collectAsState()

	var categorySelection by remember { mutableStateOf("") }

	for (category in categories) {
		if (categoryId == category.id) {
			categorySelection = category.categoryName
			Log.d("IDTOCATEGORY", categorySelection)
		}
	}


		AlertDialog(
			onDismissRequest = {
				mainViewModel.dismissEditDialog()
			},
			modifier = Modifier
				.clip(RoundedCornerShape(20.dp))
				.background(White)
				.padding(20.dp)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally

			) {
				Text(
					text = "EDIT",
					lineHeight = 45.sp,
					fontWeight = FontWeight.Bold,
					fontSize = 25.sp,
					letterSpacing = 2.sp,
					style = TextStyle(fontFamily = FontFamily.SansSerif),
					color = Color.Black,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.padding(10.dp)
				)

				foodName?.let {
					TextField(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 20.dp)
							.shadow(3.dp, RectangleShape, false),
						colors = TextFieldDefaults.colors(
							focusedTextColor = Black,
							unfocusedTextColor = Black,
							focusedContainerColor = White,
							unfocusedContainerColor = White,
							disabledContainerColor = White,
						),
						value = it,
						onValueChange = { newText ->
							foodName = newText
						},
						label = {
							Text(text = "Food Name", color = Black)
						}
					)
				}

				bbDate?.let { DatePickerField(selectedDate = it, onDateSelected = { bbDate = it.toString() }) }

				CategoryDropDownMenu(mainViewModel, categorySelection) { selectedCategory ->
					categorySelection = selectedCategory
					for (category in categories) {
						if (categorySelection == category.categoryName) {
							categoryId = category.id
						}
					}
					Log.d("Categoryselected", "$categorySelection $categoryId")
				}

				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 20.dp),
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					TextField(
						value = portionAmount.toString(),
						modifier = Modifier
							.fillMaxWidth(0.4f)
							.shadow(3.dp, RectangleShape, false),
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
						trailingIcon ={Image(
							painter = painterResource(id = R.drawable.arrows_up_down_icon),
							contentDescription = "Amount",
							contentScale = ContentScale.Fit,
							modifier = Modifier
								.size(25.dp)
						) },
						colors = TextFieldDefaults.colors(
							focusedTextColor = Black,
							unfocusedTextColor = Black,
							focusedContainerColor = White,
							unfocusedContainerColor = White,
							disabledContainerColor = White,
						),

						onValueChange = {
								newText:String-> portionAmount = newText
						},
						label ={Text(text ="#", color = Black  )}

					)

					portionType?.let {
						PortionsDropDownMenu(
							mainViewModel = mainViewModel,
							selectedPortion = it
						) { selectedCategory ->
							portionType = selectedCategory
						}
					}
				}

				Button(
					onClick = {
						mainViewModel.saveEditedEntry(
							SingleEntry(
								foodName,
								bbDate,
								categoryId,
								portionAmount,
								portionType,
								isChecked,
								timeStampChecked,
								state.value.editSingleEntry.id
							)
						)
							  },
					modifier = Modifier.padding(top = 20.dp),
					colors = androidx.compose.material.ButtonDefaults.buttonColors(BackgroundBlue)
				) {
					Text(text = "Save", color = White)
				}
			}
		}
	}



@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDownMenu(mainViewModel: MainViewModel, selectedCategory:String,onCategorySelected: (String) -> Unit){
	var isExpanded by remember {
		mutableStateOf(false)
	}

	val distinctCategories = mainViewModel.getDistinctCategories()

	Row(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center
	){
		ExposedDropdownMenuBox(
			expanded = isExpanded,
			onExpandedChange = { isExpanded = it }
		){
			TextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp)
					.shadow(3.dp, RectangleShape, false),
				label= { Text(text = "Categories", color = Color.Gray)},
				value = selectedCategory,
				onValueChange = {},
				readOnly = true,
				trailingIcon = {
					ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
				},
				colors = TextFieldDefaults.colors(
					focusedTextColor = Black,
					unfocusedTextColor = Black,
					focusedContainerColor = White,
					unfocusedContainerColor = White,
					disabledContainerColor = White,
				),
			)
			ExposedDropdownMenu(
				expanded = isExpanded,
				onDismissRequest = { isExpanded = false }
			) {
				for(category in distinctCategories){
					DropdownMenuItem(
						text = { Text(text = category, color = Black, textAlign = TextAlign.Center) },
						onClick = {
						onCategorySelected(category)
						isExpanded = false;
					},

					)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PortionsDropDownMenu(mainViewModel: MainViewModel,selectedPortion:String,onPortionSelected: (String) -> Unit){
	var isExpanded by remember {
		mutableStateOf(false)
	}



		ExposedDropdownMenuBox(
			expanded = isExpanded,
			onExpandedChange = { isExpanded = it }
		){
			TextField(
				modifier = Modifier
					.fillMaxWidth(0.95f)
					.shadow(3.dp, RectangleShape, false),
				label= { Text(text = "Unit(s)", color = Color.Gray)},
				value = selectedPortion,
				onValueChange = {},
				readOnly = true,
				trailingIcon = {
					ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
				},
				colors = TextFieldDefaults.colors(
					focusedTextColor = Black,
					unfocusedTextColor = Black,
					focusedContainerColor = White,
					unfocusedContainerColor = White,
					disabledContainerColor = White,
				),
			)
			ExposedDropdownMenu(
				expanded = isExpanded,
				onDismissRequest = { isExpanded = false }
			) {
				val hardcodedPortions = listOf(
					"Piece(s)",
					"Portion(s)",
					"Glass(es)",
					"Bottle(s)",
					"Can(s)",
					"Pack(s)",
					"g",
					"dag",
					"kg",
				)
				for(portion in hardcodedPortions){
					DropdownMenuItem(
						text = { Text(text = portion, color = Black, textAlign = TextAlign.Center) },
						onClick = {
							onPortionSelected(portion)
							isExpanded = false;
						},

						)
				}
			}
		}
	}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(
	selectedDate: String,
	onDateSelected: (LocalDate) -> Unit
) {
	// variables to get the context and an instance of a Calendar
	val context = LocalContext.current
	val calendar = Calendar.getInstance()

	// This is the window to show the calendar
	val datePicker = remember { // Use remember to ensure the dialog state is retained
		DatePickerDialog(
			context,
			{ _, selectedYear, selectedMonth, selectedDayOfMonth ->
				onDateSelected(LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth))
			},
			calendar[Calendar.YEAR],
			calendar[Calendar.MONTH],
			calendar[Calendar.DAY_OF_MONTH]
		)
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp)
			.shadow(3.dp, RectangleShape, false),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	)

	{
		// in the textfield it displays the selected Date
		TextField(
			value = selectedDate,
			colors = TextFieldDefaults.colors(
				focusedTextColor = Black,
				unfocusedTextColor = Black,
				focusedContainerColor = White,
				unfocusedContainerColor = White,
				disabledContainerColor = White,
			),
			trailingIcon ={Image(
				painter = painterResource(id = R.drawable.calendar_icon),
				contentDescription = "Calendar",
				contentScale = ContentScale.Fit,
				modifier = Modifier
					.size(35.dp)
			)},
			onValueChange = {},
			label = { Text(text = "Best-Before Date", color = Color.Gray) },
			readOnly = true,

			modifier = Modifier
				.fillMaxWidth(),
			interactionSource = remember{ MutableInteractionSource()}.also { interactionSource ->
				LaunchedEffect(interactionSource){
					interactionSource.interactions.collect{
						if(it is PressInteraction.Release){
							datePicker.show()
						}
					}
				}
			}
		)
	}
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OverviewScreen(
	mainViewModel: MainViewModel,
	navController: NavHostController
) {
	val state by mainViewModel.mainViewState.collectAsState()
	mainViewModel.getEntries()
	val allEntries by mainViewModel.entries.collectAsState()

	val currentDate = LocalDate.now()
	val hasOverdueItems = allEntries.any { entry ->
		val storedDate = runCatching { LocalDate.parse(entry.bbDate) }.getOrNull()
		storedDate != null && storedDate.isBefore(currentDate)
	}
	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(White),
		verticalArrangement = Arrangement.SpaceEvenly

	) {
		Header(title = "Overview")

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(15.dp)
				.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.SpaceEvenly
		)
		{
			Column(modifier = Modifier
				.shadow(
					color = Color(0x950B1418),
					borderRadius = 6.dp,
					blurRadius = 4.dp,
					offsetY = 4.dp,
					spread = 1f.dp
				)
				.clip(RoundedCornerShape(10.dp))
				.background(FridgeBlue)
			) {
				Column(
					modifier = Modifier
						.padding(horizontal = 15.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					if (hasOverdueItems) {
						Text(
							text = "Watch out!",
							fontWeight = FontWeight.Bold,
							fontSize = 25.sp,
							style = TextStyle(fontFamily = FontFamily.SansSerif),
							color = ExpiredRed,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(top = 20.dp, bottom = 15.dp)
						)
						Text(
							text = "There are items in your fridge that need to be taken care of!",
							color = Color.Black,
							modifier = Modifier
								.fillMaxWidth()
								.padding(bottom = 15.dp),
							fontSize = 16.sp
						)
					} else {
						Text(
							text = "Great job!",
							fontWeight = FontWeight.Bold,
							fontSize = 25.sp,
							style = TextStyle(fontFamily = FontFamily.SansSerif),
							color = GreatJobGreen,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(10.dp),
						)
						Text(
							text = "Everything in your fridge seems fine for today.",
							color = Color.Black,
							modifier = Modifier
								.fillMaxWidth()
								.padding(bottom = 16.dp),
							fontSize = 16.sp
						)
						Image(
							painter = painterResource(id = R.drawable.celebration_icon),
							contentDescription = "Great Job!",
							contentScale = ContentScale.FillBounds,
							modifier = Modifier
								.size(100.dp)
						)
					}
				}
				Row {
					LazyColumn(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(min = 100.dp, max = 400.dp)
							.padding(15.dp),
					) {
						items(allEntries.sortedBy { it.bbDate }) { entry ->
							val storedDate =
								runCatching { LocalDate.parse(entry.bbDate) }.getOrNull()
							if (storedDate != null && storedDate.isBefore(currentDate) && entry.isChecked == 0) {
								ItemUI(mainViewModel, entry = entry)
							}
						}
					}
				}
			}
			Column(
				modifier = Modifier.padding(top = 20.dp)
			) {
				Text(
					text = "Articles",
					fontWeight = FontWeight.Bold,
					fontSize = 25.sp,
					color = Color.Black,
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 15.dp, bottom = 3.dp)
				)
				Divider(
						color = Color.Black,
				thickness = 2.dp,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 15.dp, top = 10.dp)
				)
			}
			Row{
				LazyColumn(
					modifier = Modifier
						.fillMaxWidth()
						.height(400.dp)
						.padding(horizontal = 20.dp)
				) {
					items(getDummyArticlePreviews()) { articlePreview ->
						ArticlePreviewUI(articlePreview)
					}
				}
			}

			Column(
				modifier = Modifier.padding(top = 20.dp)
			){
				Text(
				text = "Your recent Items",
				fontWeight = FontWeight.Bold,
				fontSize = 25.sp,
				color = Color.Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 15.dp, bottom = 3.dp)
			)
				Divider(
					color = Color.Black,
					thickness = 2.dp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 15.dp, top = 10.dp)
				)
				Row {

					LazyColumn(
						modifier = Modifier
							.fillMaxWidth()
							.height(400.dp)
							.padding(horizontal = 20.dp)
					) {
						items(
							allEntries.sortedByDescending { it.timeStampChecked }
						) { entry ->
							if (entry.isChecked == 1) {
								checkedItemUI(entry = entry)
							}
						}
					}
				}
			}
		}
	}
}



	// A data class representing an article preview
	data class ArticlePreview(
		val iconResId: Int, // Resource ID of the article icon
		val heading: String,
		val description: String
	)

	// Function to get dummy article previews
	fun getDummyArticlePreviews(): List<ArticlePreview> {
		// You can replace this with your actual data
		return listOf(
			ArticlePreview(
				R.drawable.article2_icon, "Fridge Organization",
				"A well-organised fridge not only looks good, being able to glance into your fridge " +
						"and see exactly what's in there can prevent food from going bad, can save you money and " +
						"help to reduce the 5 million tonnes of edible food waste that households produce every year."
			),
			ArticlePreview(
				R.drawable.article1_icon, "Best Before?",
				"Learn about the storage life of the most important foods! This way, " +
						"you can better manage your fridge items."
			),
		)
	}

	@Composable
	fun ArticlePreviewUI(articlePreview: ArticlePreview) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.shadow(
					color = Color(0x950B1418),
					borderRadius = 6.dp,
					blurRadius = 4.dp,
					offsetY = 4.dp,
					spread = 1f.dp
				)
				.clip(RoundedCornerShape(10.dp))
				.background(Color.White)
				.padding(15.dp)
				.clickable {
					// Handle click action for article preview
				}
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(5.dp)
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(5.dp)
				) {
					Image(
						painter = painterResource(id = articlePreview.iconResId),
						contentDescription = null,
						modifier = Modifier
							.size(50.dp)
					)
					Spacer(modifier = Modifier.width(20.dp))

					Text(
						text = articlePreview.heading,
						fontWeight = FontWeight.Bold,
						fontSize = 20.sp,
						color = Color.Black,
						modifier = Modifier
							.padding(top = 8.dp),
						overflow = TextOverflow.Ellipsis
					)
				}
				Text(
					text = articlePreview.description,
					fontSize = 14.sp,
					color = Color.Black,
					modifier = Modifier
						.padding(top = 4.dp),
					overflow = TextOverflow.Ellipsis
				)
			}
		}
		Spacer(modifier = Modifier.height(20.dp))
	}







// custom inner box shadow
// https://stackoverflow.com/questions/71054138/jetpack-compose-inner-shadow
//	fun Modifier.innerShadow(
//	color: Color = Color.Black,
//	cornersRadius: Dp = 0.dp,
//	spread: Dp = 0.dp,
//	blur: Dp = 0.dp,
//	offsetY: Dp = 0.dp,
//	offsetX: Dp = 0.dp
//	) = drawWithContent {
//
//		drawContent()
//
//		val rect = Rect(Offset.Zero, size)
//		val paint = Paint()
//
//		drawIntoCanvas {
//
//			paint.color = color
//			paint.isAntiAlias = true
//			it.saveLayer(rect, paint)
//			it.drawRoundRect(
//				left = rect.left,
//				top = rect.top,
//				right = rect.right,
//				bottom = rect.bottom,
//				cornersRadius.toPx(),
//				cornersRadius.toPx(),
//				paint
//			)
//			val frameworkPaint = paint.asFrameworkPaint()
//			frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
//			if (blur.toPx() > 0) {
//				frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
//			}
//			val left = if (offsetX > 0.dp) {
//				rect.left + offsetX.toPx()
//			} else {
//				rect.left
//			}
//			val top = if (offsetY > 0.dp) {
//				rect.top + offsetY.toPx()
//			} else {
//				rect.top
//			}
//			val right = if (offsetX < 0.dp) {
//				rect.right + offsetX.toPx()
//			} else {
//				rect.right
//			}
//			val bottom = if (offsetY < 0.dp) {
//				rect.bottom + offsetY.toPx()
//			} else {
//				rect.bottom
//			}
//			paint.color = Color.Black
//			it.drawRoundRect(
//				left = left + spread.toPx() / 2,
//				top = top + spread.toPx() / 2,
//				right = right - spread.toPx() / 2,
//				bottom = bottom - spread.toPx() / 2,
//				cornersRadius.toPx(),
//				cornersRadius.toPx(),
//				paint
//			)
//			frameworkPaint.xfermode = null
//			frameworkPaint.maskFilter = null
//		}
//	}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskAmountModal(mainViewModel: MainViewModel, entry: SingleEntry, checkboxState:Boolean) {
	val state = mainViewModel.mainViewState.collectAsState()

	var foodName by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.foodName)
	}
	var bbDate by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.bbDate)
	}
	var categoryId by rememberSaveable {
		mutableIntStateOf(state.value.editSingleEntry.categoryId)
	}
	var portionAmount by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.portionAmount)
	}
	var portionType by rememberSaveable {
		mutableStateOf(state.value.editSingleEntry.portionType)
	}
	var isChecked by rememberSaveable {
		mutableIntStateOf(state.value.editSingleEntry.isChecked)
	}
	var timeStampChecked by rememberSaveable {
		mutableStateOf(entry.timeStampChecked)
	}

	var amountTaken by rememberSaveable {
		mutableStateOf("")
	}

	val mContext = LocalContext.current

	AlertDialog(
		onDismissRequest = {
			mainViewModel.dismissAskAmountDialog()
		},
		modifier = Modifier
			.clip(RoundedCornerShape(20.dp))
			.background(White)
			.padding(10.dp)
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally

		) {
			Text(
				text = "How much did you take?",
				lineHeight = 45.sp,
				fontWeight = FontWeight.Bold,
				fontSize = 40.sp,
				style = TextStyle(fontFamily = FontFamily.SansSerif),
				color = Color.Black,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth(),
			)

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp),
				horizontalArrangement = Arrangement.SpaceEvenly,
				verticalAlignment = Alignment.CenterVertically
			) {
				TextField(
					value = amountTaken,
					modifier = Modifier
						.fillMaxWidth(0.5f)
						.shadow(3.dp, RectangleShape, false),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					trailingIcon = {
						Image(
							painter = painterResource(id = R.drawable.arrows_up_down_icon),
							contentDescription = "Amount",
							contentScale = ContentScale.Fit,
							modifier = Modifier
								.size(25.dp)
						)
					},
					colors = TextFieldDefaults.colors(
						focusedTextColor = Black,
						unfocusedTextColor = Black,
						focusedContainerColor = White,
						unfocusedContainerColor = White,
						disabledContainerColor = White,
					),
					onValueChange = {
							newText: String ->
						amountTaken = newText
					},
					label = { Text(text = "#", color = Black) }
				)
				Text(
					text = entry.portionType!!,
					color = Black,
					fontSize = 25.sp,
					textAlign = TextAlign.Center
				)
			}
			Button(
				onClick =   {



					val takenAmount = amountTaken.toFloatOrNull()
					Log.d("amountTaken","$amountTaken")

					Log.d("TAKENAMOUNT","$takenAmount")
					if (takenAmount != null) {
						Log.d("IFSTATEMENT", "takenAmount is not null")
						val remainingAmount = portionAmount?.toFloatOrNull()?.minus(takenAmount)
						Log.d("IFSTATEMENT", "Remaining amount: $remainingAmount")

						if ((remainingAmount != null) && (remainingAmount >= 0)) {
							portionAmount = remainingAmount.toString()
							mainViewModel.dismissAskAmountDialog()

							Log.d("INNERIFSTATEMENT", "$portionAmount")
						} else {
							Toast.makeText(
								mContext,
								"Cannot take more than what is in the fridge!",
								Toast.LENGTH_SHORT
							).show()
						}

						if(remainingAmount == 0.0f){
							isChecked = 1;
							val currentTime = LocalTime.now()
							timeStampChecked = currentTime.toString()
							Log.d("ISCHECKED", "$isChecked")
						}

						mainViewModel.saveEditedEntry(
							SingleEntry(
								foodName,
								bbDate,
								categoryId,
								portionAmount,
								portionType,
								isChecked,
								timeStampChecked,
								state.value.editSingleEntry.id
							)
						)
					} else {
						// Handle invalid input (non-numeric amount taken)
						Toast.makeText(
							mContext,
							"Invalid input. Please enter a valid number.",
							Toast.LENGTH_SHORT
						).show()
					}

				},
				modifier = Modifier.padding(top = 20.dp),
				colors = androidx.compose.material.ButtonDefaults.buttonColors(BackgroundBlue)
			) {
				Text(text = "Save", color = White)
			}
		}
	}
}



// custom drop shadow function
// https://github.com/Debdutta-Panda/CustomShadow/blob/master/app/src/main/java/com/debduttapanda/customshadow/MainActivity.kt
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
