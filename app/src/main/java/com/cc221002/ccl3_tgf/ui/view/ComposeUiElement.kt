package com.cc221002.ccl3_tgf.ui.view

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.RoundedCorner
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
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.graphics.Color.Companion.Gray
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
import androidx.compose.ui.layout.layout
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cc221002.ccl3_tgf.R
import com.cc221002.ccl3_tgf.data.Category
import com.cc221002.ccl3_tgf.data.model.SingleEntry
import com.cc221002.ccl3_tgf.ui.theme.AddButton
import com.cc221002.ccl3_tgf.ui.theme.AlertBoxBlue
import com.cc221002.ccl3_tgf.ui.theme.AlertBoxGrey
import com.cc221002.ccl3_tgf.ui.theme.BackgroundBlue
import com.cc221002.ccl3_tgf.ui.theme.BackgroundLightBlue
import com.cc221002.ccl3_tgf.ui.theme.ButtonBlue
import com.cc221002.ccl3_tgf.ui.theme.ButtonRed
import com.cc221002.ccl3_tgf.ui.theme.ExpiredRed
import com.cc221002.ccl3_tgf.ui.theme.FridgeBlue
import com.cc221002.ccl3_tgf.ui.theme.FridgeBorder
import com.cc221002.ccl3_tgf.ui.theme.HistoryItemGray
import com.cc221002.ccl3_tgf.ui.theme.HistoryItemGreen
import com.cc221002.ccl3_tgf.ui.theme.NavigationBlue
import com.cc221002.ccl3_tgf.ui.theme.SecondaryGray
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

	data class Article(val articleId: String) : Screen("article/$articleId")
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
		"article/{articleId}" -> false
		else -> true // in all other cases show bottom bar
	}
	// defining the routes to each Screen and what happens when that route is used
	Scaffold(
		floatingActionButton ={
			if(showFloatingButton) {
				// Add a floating button to navigate to the AddingPopup
				FloatingActionButton(
					containerColor = AddButton,
					onClick = {
						mainViewModel.openAddDialog()
					},
					modifier = Modifier
						.shadow(
							color = Color(0xFF021116),
							borderRadius = 10.dp,
							blurRadius = 6.dp,
							offsetY = 6.dp,
							offsetX = 6.dp,
							spread = 1f.dp
					)
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
			composable(
				route = "article/{articleId}",
				arguments = listOf(navArgument("articleId") { type = NavType.IntType })
			) { backStackEntry ->
				val arguments = requireNotNull(backStackEntry.arguments)
				val articleId = arguments.getInt("articleId")
				ArticleScreen(articleId, navController)
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
					selectedIndicatorColor = BackgroundBlue,
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
					selectedIndicatorColor = BackgroundBlue,
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
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {

		Header(title = "Your Fridge", navController)

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(start = 10.dp, end = 10.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {


			Row {
				if (state.openAddDialog) {
					AddingPopup(mainViewModel = mainViewModel, categoryName = "")
				}

				// fridge box containing all the categories
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(585.dp)
						.padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 0.dp)
						.clip(RoundedCornerShape(5))
						.border(width = 3.dp, FridgeBorder, shape = RoundedCornerShape(5))
						.background(FridgeBlue),
					contentAlignment = Alignment.Center
				) {
					LazyColumn(
						modifier = Modifier
							.fillMaxWidth()
							.padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 0.dp),
						verticalArrangement = Arrangement.spacedBy(8.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						item {
							val leftovers = categories.find { it.categoryName == "Leftovers" }

							leftovers?.let {
								// Determine background color based on conditions
								val backgroundColor =
									if (!mainViewModel.hasEntriesForCategory(leftovers.id)) {
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
										color = Color(0xFF1C404E),
										borderRadius = 10.dp,
										blurRadius = 5.dp,
										offsetY = 5.dp,
										offsetX = 5.dp,
										spread = 3f.dp
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
										}
										.padding(34.dp),
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
									val backgroundColor =
										if (!mainViewModel.hasEntriesForCategory(drinks.id)) {
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
												color = Color(0xFF1C404E),
												borderRadius = 10.dp,
												blurRadius = 5.dp,
												offsetY = 5.dp,
												offsetX = 5.dp,
												spread = 3f.dp
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
											.padding(34.dp),
										textAlign = TextAlign.Center,
										fontSize = 20.sp,
										fontWeight = FontWeight.Bold,
										color = Color.White
									)
								}

								dairy?.let {
									val backgroundColor =
										if (!mainViewModel.hasEntriesForCategory(dairy.id)) {
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
												color = Color(0xFF1C404E),
												borderRadius = 10.dp,
												blurRadius = 5.dp,
												offsetY = 5.dp,
												offsetX = 5.dp,
												spread = 3f.dp
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
											.padding(34.dp),
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
							val backgroundColor =
								if (!mainViewModel.hasEntriesForCategory(extras.id)) {
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
										color = Color(0xFF1C404E),
										borderRadius = 10.dp,
										blurRadius = 5.dp,
										offsetY = 5.dp,
										offsetX = 5.dp,
										spread = 3f.dp,
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
									.padding(34.dp),
								textAlign = TextAlign.Center,
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold,
								color = Color.White
							)
						}

							Spacer(modifier = Modifier.height(8.dp))

						meat?.let {
							val backgroundColor =
								if (!mainViewModel.hasEntriesForCategory(meat.id)) {
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
										color = Color(0xFF1C404E),
										borderRadius = 10.dp,
										blurRadius = 5.dp,
										offsetY = 5.dp,
										offsetX = 5.dp,
										spread = 3f.dp
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
									.padding(34.dp),
								textAlign = TextAlign.Center,
								fontSize = 20.sp,
								fontWeight = FontWeight.Bold,
								color = Color.White
							)
						}
					}

						item {
							Row(
								modifier = Modifier
									.fillMaxWidth()
									.padding(bottom = 5.dp),
								horizontalArrangement = Arrangement.spacedBy(8.dp)
							) {
								val fruit = categories.find { it.categoryName == "Fruit" }
								val vegetables = categories.find { it.categoryName == "Vegetables" }

							fruit?.let {
								val backgroundColor =
									if (!mainViewModel.hasEntriesForCategory(fruit.id)) {
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
											color = Color(0xFF1C404E),
											borderRadius = 10.dp,
											blurRadius = 5.dp,
											offsetY = 5.dp,
											offsetX = 5.dp,
											spread = 3f.dp
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
										.padding(34.dp),
									textAlign = TextAlign.Center,
									fontSize = 20.sp,
									fontWeight = FontWeight.Bold,
									color = Color.White
								)
							}

							vegetables?.let {
								val backgroundColor =
									if (!mainViewModel.hasEntriesForCategory(vegetables.id)) {
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
											color = Color(0xFF1C404E),
											borderRadius = 10.dp,
											blurRadius = 5.dp,
											offsetY = 5.dp,
											offsetX = 5.dp,
											spread = 3f.dp
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
											start = 23.dp,
											end = 23.dp,
											top = 34.dp,
											bottom = 34.dp
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
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(45.dp),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			Box(
				modifier = Modifier
					.width(35.dp)
					.height(15.dp)
					.clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
					.background(FridgeBorder)
			)
			Box(
				modifier = Modifier
					.width(35.dp)
					.height(15.dp)
					.clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
					.background(FridgeBorder)
			)
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun categoryEntries(navController: NavHostController,mainViewModel: MainViewModel) {
	val state = mainViewModel.mainViewState.collectAsState()
	val entries = mainViewModel.entriesForCategory.collectAsState()
	val categories by mainViewModel.categories.collectAsState()

	val categoryName = mainViewModel.currentCategory
	var categoryId by remember { mutableIntStateOf(0) }
	for (category in categories) {
		if (category.categoryName == categoryName) {
			categoryId = category.id
		}
	}
	Column(
		modifier = Modifier
			.background(White)
			.fillMaxSize()
	) {
		Header("$categoryName", navController)
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(start = 10.dp, end = 10.dp),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
		Row  {
			Box(
				modifier = Modifier
					.offset(y = (-15).dp)
					.fillMaxWidth()
					.height(550.dp)
					.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 0.dp)
					.clip(RoundedCornerShape(16.dp))
					.background(FridgeBlue)
					.border(width = 3.dp, FridgeBorder, shape = RoundedCornerShape(5)),
				contentAlignment = Alignment.Center
			) {
				LazyColumn(
					verticalArrangement = Arrangement.Top,
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier
						.height(550.dp)
						.padding(top = 30.dp, bottom = 30.dp)
				) {
					if (entries.value.isEmpty() || mainViewModel.areAllEntriesChecked(categoryId)) {
						item {
							Text(
								text = "This category is empty. You can add an item with the '+' button in the bottom right.",
								color = SecondaryGray,
								fontSize = 16.sp,
								textAlign = TextAlign.Center,
								modifier = Modifier
									.padding(30.dp),
							)
						}
					} else {
						items(entries.value.sortedBy { it.bbDate }) { entry ->
							if (entry.isChecked == 0) {
								ItemUI(mainViewModel, entry = entry)
							}
						}
					}
				}

					if (state.value.openAddDialog) {
						AddingPopup(mainViewModel = mainViewModel, categoryName)
					}
				}
			}

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.offset(y = (-15).dp)
				.height(45.dp),
			horizontalArrangement = Arrangement.SpaceEvenly
		) {
			Box(
				modifier = Modifier
					.width(35.dp)
					.height(15.dp)
					.clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
					.background(FridgeBorder)
			)
			Box(
				modifier = Modifier
					.width(35.dp)
					.height(15.dp)
					.clip(RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
					.background(FridgeBorder)
			)
			}
		}
	}
}

@Composable
fun Header(title:String, navController: NavHostController){

	val categoryImageMap = mapOf(
		"Leftovers" to R.drawable.leftovers_icon,
		"Drinks" to R.drawable.drinks_icon,
		"Dairy" to R.drawable.dairy_icon,
		"Extras" to R.drawable.extras_icon,
		"Meat" to R.drawable.meat_icon,
		"Fruit" to R.drawable.fruit_icon,
		"Vegetables" to R.drawable.vegetables_icon
	)

	if(title == "Your Fridge" || title == "Overview" || title == "Article"){

		Box(
			modifier = Modifier
				.clip(shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 40.dp))
				.fillMaxWidth()
				.height(75.dp)
				.background(NavigationBlue),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = title,
				letterSpacing = 2.sp,
				fontSize = 25.sp,
				fontWeight = FontWeight.Bold,
				color = Color.White,
				modifier = Modifier
					.padding(start = 8.dp)
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
					.clip(shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 40.dp))
					.fillMaxWidth()
					.height(75.dp)
					.background(NavigationBlue),
				contentAlignment = Alignment.Center
			) {
				Image(
				painter = painterResource(id = R.drawable.go_back_button),
				contentDescription = null,
				modifier = Modifier
					.size(30.dp)
					.padding(start = 15.dp)
					.align(Alignment.CenterStart)
					.clickable {
						if (title == "Leftovers" || title == "Drinks" || title == "Dairy" || title == "Extras" || title == "Meat" || title == "Fruit" || title == "Vegetables") {
							navController.navigate(Screen.ShowCategories.route)
						} else {
							navController.navigate(Screen.Overview.route)
						}
					}
			)
				Row {
					Text(
						text = title,
						fontSize = 25.sp,
						letterSpacing = 2.sp,
						fontWeight = FontWeight.Bold,
						color = Color.White,
						modifier = Modifier.padding(start = 8.dp)
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
				.padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
				.shadow(
					color = Color(0xFF1C404E),
					borderRadius = 10.dp,
					blurRadius = 5.dp,
					offsetY = 5.dp,
					offsetX = 5.dp,
					spread = 3f.dp
				)
				.fillMaxWidth()
				.clip(RoundedCornerShape(10.dp))
				.background(
					if (storedDate != null && storedDate.isAfter(currentDate)) {
						BackgroundBlue
					} else {
						ExpiredRed
					}
				)
				.clickable { mainViewModel.openEditDialog(entry) },
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.CenterVertically
		) {
			Spacer(modifier = Modifier.padding(7.dp))
			Box(
				modifier = Modifier
					.size(40.dp)
					.padding(start = 0.dp)
			){
				Box(
					modifier = Modifier
						.size(37.dp)
						.shadow(
							color = Color(0x25000000),
							borderRadius = 20.dp,
							blurRadius = 0.dp,
							offsetY = 5.dp,
							offsetX = 3.dp,
							spread = 3f.dp
						)
						.clip(RoundedCornerShape(20.dp))
						.background(
							if (storedDate != null && storedDate.isAfter(currentDate)) {
								ButtonBlue
							} else {
								ButtonRed
							}
						)
						.padding(start = 7.dp, top = 7.dp, end = 4.dp, bottom = 7.dp)
						.clickable { mainViewModel.openAskAmountDialog(entry) }
				) {
					Image(
						painter = painterResource(id = R.drawable.pan_icon),
						contentDescription = "Use item",
						contentScale = ContentScale.Fit,
						modifier = Modifier
							.size(37.dp),
						colorFilter = ColorFilter.tint(Color.White)
					)
				}
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
						.padding(bottom = 8.dp)
						.width(193.dp)
				)
				Text(
					text = "${entry.portionAmount} ${entry.portionType}",
					textAlign = TextAlign.Start,
					fontSize = 12.sp,
					style = TextStyle(fontFamily = FontFamily.Monospace),
					color = Color.White,
					modifier = Modifier
						.padding(bottom = 6.dp)
						.width(193.dp)
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
						.width(193.dp)
				)
			}
			Spacer(modifier = Modifier.padding(5.dp))
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

	val openAskAmountDialogForEntry = mainViewModel.openAskAmountDialogForEntry.value
	if(openAskAmountDialogForEntry == entry.id.toString()) {
		AskAmountModal(mainViewModel = mainViewModel, entry = entry, checkBoxState)
	}
	val openEditDialogForEntry = mainViewModel.openEditDialogForEntry.value
	if (openEditDialogForEntry == entry.id.toString()) {
//		if (state.value.openEditDialog) {
			EditPopUp(mainViewModel = mainViewModel)
//		}
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
fun checkedItemUI(mainViewModel: MainViewModel, entry: SingleEntry) {
	mainViewModel.getAllCategories()
	val categories by mainViewModel.categories.collectAsState()

	var categorySelection by remember { mutableStateOf("") }

	for (category in categories) {
		if (entry.categoryId == category.id) {
			categorySelection = category.categoryName
		}
	}

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
				text =  categorySelection,
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
		text = { Text("Are you sure you want to delete this entry?", color = Black, fontSize = 16.sp, textAlign = TextAlign.Center) },
		confirmButton = {
			androidx.compose.material3.Button(
				onClick = {
					onDeleteConfirmed(entry) // Perform deletion on confirmation
				},
				modifier = Modifier.padding(top = 20.dp),
				colors = ButtonDefaults.buttonColors(Transparent)
			) {
				Text("Delete", color = ExpiredRed, fontWeight = FontWeight.Bold, fontSize = 18.sp)
			}
		},
		dismissButton = {
			androidx.compose.material3.Button(
				onClick = { mainViewModel.dismissAlertDialog() },
				colors = ButtonDefaults.buttonColors(Transparent),
				modifier = Modifier.padding(top = 20.dp)
			) {
				Text("Cancel", color = Black, fontSize = 18.sp)
			}
		}
	)
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingPopup(
	mainViewModel: MainViewModel,
	categoryName : String?
){
	val categories by mainViewModel.categories.collectAsState()
	val mContext = LocalContext.current

	var foodName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
	var bbDate by remember { mutableStateOf("") }
	var categoryId by remember { mutableIntStateOf(0) }
	var portionAmount by rememberSaveable { mutableStateOf("1") }
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
			.padding(20.dp),

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

			CategoryDropDownMenu(categoryName, mainViewModel, categorySelection, ){ selectedCategory->
				if(categoryName == "") {
					categorySelection = selectedCategory
				} else if(selectedCategory == null) {
					categorySelection = categoryName!!
				} else{
					categorySelection = selectedCategory
				}
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
						Text(text ="Amount", color = Color.Gray)}
				)

				PortionsDropDownMenu(mainViewModel = mainViewModel, selectedPortion = portionSelection){selectedCategory->
					portionSelection = selectedCategory
				}
			}

			Row(
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically
			) {
				Button(
					elevation = androidx.compose.material.ButtonDefaults.elevation(0.dp),
					onClick = {
						mainViewModel.dismissAddDialog()
							  },
					modifier = Modifier.padding(top = 20.dp),
					colors = androidx.compose.material.ButtonDefaults.buttonColors(Transparent)
				) {
					Text(text = "Cancel", color = Black )
				}

				Button(
					onClick = {
							if (categorySelection == "") {
								categorySelection = categoryName!!

							}
							for (category in categories) {
								if (categorySelection == category.categoryName) {
									categoryId = category.id
								}
							}
							Log.d("Saving prefilled", "categoryname: $categoryName")
							Log.d("Saving prefilled", "categorySelection: $categorySelection")

							if (foodName.text.isBlank() || bbDate.isBlank() || categorySelection.isBlank() || portionAmount.isBlank() || portionSelection.isBlank()) {
								Toast.makeText(
									mContext,
									"You forgot something! Please fill everything out!",
									Toast.LENGTH_SHORT
								).show()
							}else {
							mainViewModel.saveButton(
								SingleEntry(
									foodName.text,
									bbDate,
									categoryId,
									portionAmount,
									portionSelection,
									isChecked,
									timeStampChecked
								)
							)
						}
					},
					modifier = Modifier.padding(top = 20.dp),
					colors = androidx.compose.material.ButtonDefaults.buttonColors(BackgroundBlue)
				) {
					Text(text = "Add", color = White )
				}

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
	val mContext = LocalContext.current

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

				CategoryDropDownMenu(categoryName = categorySelection, mainViewModel, categorySelection) { selectedCategory ->
					categorySelection = selectedCategory
					for (category in categories) {
						if (categorySelection == category.categoryName) {
							categoryId = category.id
						}
					}
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
						label ={Text(text ="Nr.", color = Black  )}

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
				Row(
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically
				) {
					Button(
						elevation = androidx.compose.material.ButtonDefaults.elevation(0.dp),
						onClick = {
							mainViewModel.dismissEditDialog()
						},
						modifier = Modifier.padding(top = 20.dp),
						colors = androidx.compose.material.ButtonDefaults.buttonColors(Transparent)
					) {
						Text(text = "Cancel", color = Black)
					}

					Button(
						onClick = {
							if (foodName!!.isBlank() || bbDate!!.isBlank() || categorySelection.isBlank() || portionAmount!!.isBlank() || portionType!!.isBlank()) {
								Toast.makeText(
									mContext,
									"You forgot something! Please fill everything out!",
									Toast.LENGTH_SHORT
								).show()
							} else {
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
							}
						},
						modifier = Modifier.padding(top = 20.dp),
						colors = androidx.compose.material.ButtonDefaults.buttonColors(
							BackgroundBlue
						)
					) {
						Text(text = "Save", color = White)
					}
				}
			}
		}
	}



@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDownMenu(categoryName: String?,mainViewModel: MainViewModel, selectedCategory:String, onCategorySelected: (String) -> Unit){
	var isExpanded by remember {
		mutableStateOf(false)
	}

	val distinctCategories = mainViewModel.getDistinctCategories()
	Log.d("PREFILLEDCATEGORY", "getting passed categoryName: $categoryName")

	Row(
		modifier = Modifier
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center
	){
		ExposedDropdownMenuBox(
			expanded = isExpanded,
			onExpandedChange = { isExpanded = it }
		){
			val textFieldValue = remember { mutableStateOf(TextFieldValue(categoryName ?: "")) }

			TextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp)
					.shadow(3.dp, RectangleShape, false),
				label= { Text(text = "Categories", color = Color.Gray)},
				value = textFieldValue.value,
				onValueChange = {
					textFieldValue.value = it
                    // Update the selected category when the text changes
                    onCategorySelected(it.toString())
					Log.d("PREFILLEDCATEGORY", "OnValue Change categoryName: $categoryName")

				},
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
							textFieldValue.value = TextFieldValue(category)
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
	mainViewModel.getEntries()

	val allEntries by mainViewModel.entries.collectAsState()
	val state by mainViewModel.mainViewState.collectAsState()
	val currentDate = LocalDate.now()
	val expiredItemsList = allEntries.filter { entry ->
		val storedDate = runCatching { LocalDate.parse(entry.bbDate) }.getOrNull()
		storedDate != null && !storedDate.isAfter(currentDate) && entry.isChecked == 0
	}
	var hasOverdueItems = expiredItemsList.isNotEmpty()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(White),
		verticalArrangement = Arrangement.SpaceEvenly

	) {
		Header(title = "Overview",navController)

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(20.dp)
				.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.SpaceEvenly,
			horizontalAlignment = Alignment.CenterHorizontally
		)
		{
			Box(
				modifier = Modifier
					.clip(shape = RoundedCornerShape(topStart = 68.dp, topEnd = 68.dp))
					.background(
						if (hasOverdueItems) {
							AlertBoxBlue
						} else {
							AlertBoxGrey
						}
					)
					.height(55.dp)
					.width(110.dp)
					.padding(0.dp)
			) {
				Image(
					painter = painterResource(id = R.drawable.tgf_logo_small),
					contentDescription = "Logo",
					contentScale = ContentScale.FillBounds,
					modifier = Modifier
						.width(48.dp)
						.height(74.dp)
						.align(Alignment.Center)
						.padding(top = 5.dp, bottom = 0.dp)
						.graphicsLayer(
							translationY = 62.dp.value
						)
				)
			}
			Column(
				modifier = Modifier
					.clip(RoundedCornerShape(10.dp))
					.background(
						if (hasOverdueItems) {
							AlertBoxBlue
						} else {
							AlertBoxGrey
						}
					)
					.padding(top = 0.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Box(
					modifier = Modifier
						.shadow(
							color = Color(0x430B1418),
							borderRadius = 200.dp,
							blurRadius = 30.dp,
							offsetY = 0.dp,
							spread = 0f.dp
						)
						.clip(shape = RoundedCornerShape(bottomStart = 68.dp, bottomEnd = 68.dp))
						.background(
							if (hasOverdueItems) {
								AlertBoxBlue
							} else {
								AlertBoxGrey
							}
						)
						.height(55.dp)
						.width(110.dp)
						.padding(0.dp)
				) {
					Image(
						painter = painterResource(id = R.drawable.tgf_logo_small),
						contentDescription = "Logo",
						contentScale = ContentScale.FillBounds,
						modifier = Modifier
							.width(48.dp)
							.height(74.dp)
							.align(Alignment.Center)
							.padding(top = 0.dp)
							.graphicsLayer(
								translationY = (-77).dp.value
							)
					)
				}

				Column(
					modifier = Modifier
						.padding(horizontal = 30.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {

					if (hasOverdueItems) {
						Text(
							text = "Watch out!",
							fontWeight = FontWeight.Bold,
							fontSize = 30.sp,
							style = TextStyle(fontFamily = FontFamily.SansSerif),
							color = White,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(top = 15.dp, bottom = 16.dp)
						)
						Text(
							text = "There are items in your fridge that need to be taken care of!",
							color = White,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(bottom = 15.dp),
							fontSize = 18.sp
						)
					} else {
						Text(
							text = "Great job!",
							fontWeight = FontWeight.Bold,
							fontSize = 30.sp,
							style = TextStyle(fontFamily = FontFamily.SansSerif),
							color = Black,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(top = 15.dp, bottom = 16.dp),
						)
						Text(
							text = "Everything in your fridge seems fine for today.",
							color = Black,
							textAlign = TextAlign.Center,
							modifier = Modifier
								.fillMaxWidth()
								.padding(bottom = 16.dp),
							fontSize = 18.sp
						)
						Image(
							painter = painterResource(id = R.drawable.celebration_flags),
							contentDescription = "Great Job!",
							contentScale = ContentScale.FillBounds,
							modifier = Modifier
								.width(120.dp)
								.height(100.dp)
						)
					}
				}
				Row {
					LazyColumn(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(min = 20.dp, max = 400.dp)
							.padding(15.dp),
					) {
						items(allEntries.sortedBy { it.bbDate }) { entry ->
							val storedDate =
								runCatching { LocalDate.parse(entry.bbDate) }.getOrNull()
							if (storedDate != null && !storedDate.isAfter(currentDate) && entry.isChecked == 0 && !mainViewModel.areAllEntriesChecked(entry.categoryId)) {
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
					text = "Quick Add",
					fontWeight = FontWeight.Bold,
					fontSize = 25.sp,
					color = Black,
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 15.dp, bottom = 3.dp, top = 18.dp)
				)
				Divider(
					color = Black,
					thickness = 1.dp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 28.dp, top = 6.dp, start = 18.dp, end = 18.dp)
				)
				Row(
					modifier = Modifier
						.padding(bottom = 15.dp)
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceEvenly
				) {
					quickAddItemUI(mainViewModel,"10", "Piece(s)","Eggs", "Extras")
					quickAddItemUI(mainViewModel,"500", "g","Beef", "Meat")
					quickAddItemUI(mainViewModel,"1", "Pack(s)","Milk", "Dairy")
				}
				Row(
					modifier = Modifier
						.padding(bottom = 15.dp)
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceEvenly
				) {
					quickAddItemUI(mainViewModel,"6", "Bottle(s)","Beer", "Drinks")
					quickAddItemUI(mainViewModel,"6", "Piece(s)","Apples", "Fruit")
					quickAddItemUI(mainViewModel,"500", "g","Carrots", "Vegetables")
				}
			}




			Column(
				modifier = Modifier.padding(top = 20.dp)
			) {
				Text(
					text = "Articles",
					fontWeight = FontWeight.Bold,
					fontSize = 25.sp,
					color = Black,
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 15.dp, bottom = 3.dp, top = 18.dp)
				)
				Divider(
						color = Black,
				thickness = 1.dp,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 28.dp, top = 6.dp, start = 18.dp, end = 18.dp)
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
						ArticlePreviewUI(articlePreview, navController)
					}
				}
			}

			Column(
				modifier = Modifier.padding(top = 20.dp)
			){
				Text(
				text = "Used Up Items",
				fontWeight = FontWeight.Bold,
				fontSize = 25.sp,
				color = Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 15.dp, bottom = 3.dp, top = 5.dp)
			)
				Divider(
					color = Black,
					thickness = 1.dp,
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 22.dp, top = 6.dp, start = 18.dp, end = 18.dp)
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
								checkedItemUI(mainViewModel, entry = entry)
							}
						}
					}
				}
			}
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun quickAddItemUI(mainViewModel: MainViewModel,amount: String, portion: String, foodName: String, categoryName: String?){
	val state = mainViewModel.mainViewState.collectAsState()

	val QuickAddImageMap = mapOf(
		"Eggs" to R.drawable.eggs_icon,
		"Beef" to R.drawable.beef_icon,
		"Milk" to R.drawable.dairy_icon,
		"Beer" to R.drawable.beer_icon,
		"Apples" to R.drawable.apple_icon,
		"Carrots" to R.drawable.carrot_icon,
	)

	Box(modifier = Modifier
//		.border(1.dp, Black, RoundedCornerShape(10.dp))
		.shadow(
			color = Color(0xFF1C404E),
			borderRadius = 10.dp,
			blurRadius = 5.dp,
			offsetY = 5.dp,
			offsetX = 5.dp,
			spread = 3f.dp
		)
		.size(100.dp)
		.clip(RoundedCornerShape(10.dp))
		.background(BackgroundBlue)
		.clickable { mainViewModel.openQuickAddDialog(foodName) }

	){
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			val categoryImage = QuickAddImageMap[foodName]
			categoryImage?.let { image ->
				Image(
					painter = painterResource(id = image),
					contentDescription = null,
					modifier = Modifier
						.size(50.dp)
						.padding(top = 5.dp)
				)
			}
			Text(
				text = "$foodName",
				color = White,
				modifier = Modifier
					.fillMaxWidth(),
				fontSize = 16.sp,
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Bold
			)

			Text(
				text = "$amount $portion",
				color = White,
				modifier = Modifier
					.fillMaxWidth(),
				fontSize = 12.sp,
				textAlign = TextAlign.Center
			)

		}
		val openQuickAddDialogForEntry = mainViewModel.openQuickAddingDialogFor.value
		if (openQuickAddDialogForEntry == foodName) {
			QuickAddingPopup(mainViewModel, categoryName, amount, portion, foodName, QuickAddImageMap)
		}
	}
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickAddingPopup(
	mainViewModel: MainViewModel,
	categoryName : String?,
	amount: String,
	portion: String,
	foodName: String,
	QuickAddImageMap: Map<String, Int>,

) {

	val categories by mainViewModel.categories.collectAsState()
	val mContext = LocalContext.current
	var categoryId by remember { mutableIntStateOf(0) }
	var timeStampChecked by remember { mutableStateOf("")}


	var bbDate by remember { mutableStateOf("") }

//	var portionType by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
	var isChecked = 0

	AlertDialog(
		onDismissRequest = {
			mainViewModel.dismissQuickAddDialog()
		},
		modifier = Modifier
			.clip(RoundedCornerShape(20.dp))
			.background(White)
			.padding(20.dp),

		) {
		Column(
			modifier = Modifier
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally

		) {
			Text(
				text = "ADD ITEM",
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

			Box(modifier = Modifier
				.border(1.dp, Black, RoundedCornerShape(10.dp))
				.size(100.dp)
				.clip(RoundedCornerShape(10.dp))
				.background(White)
			) {
				Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					val categoryImage = QuickAddImageMap[foodName]
					categoryImage?.let { image ->
						Image(
							painter = painterResource(id = image),
							contentDescription = null,
							modifier = Modifier
								.size(50.dp)
								.padding(top = 5.dp)
						)
					}
					Text(
						text = "$foodName",
						color = Black,
						modifier = Modifier
							.fillMaxWidth(),
						fontSize = 16.sp,
						textAlign = TextAlign.Center,
						fontWeight = FontWeight.Bold
					)

					Text(
						text = "$amount $portion",
						color = Gray,
						modifier = Modifier
							.fillMaxWidth(),
						fontSize = 12.sp,
						textAlign = TextAlign.Center
					)
				}
			}

			Text(
				text = "When does it expire?",
				lineHeight = 45.sp,
				fontSize = 15.sp,
				style = TextStyle(fontFamily = FontFamily.SansSerif),
				color = Color.Black,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 15.dp),
			)

			DatePickerField(selectedDate = bbDate, onDateSelected = { bbDate = it.toString() })

			Row(
				horizontalArrangement = Arrangement.Center,
				verticalAlignment = Alignment.CenterVertically
			) {
				Button(
					elevation = androidx.compose.material.ButtonDefaults.elevation(0.dp),
					onClick = {
						mainViewModel.dismissQuickAddDialog()
					},
					modifier = Modifier.padding(top = 20.dp),
					colors = androidx.compose.material.ButtonDefaults.buttonColors(Transparent)
				) {
					Text(text = "Cancel", color = Black )
				}

				Button(
					onClick = {

						for (category in categories) {
							if (categoryName == category.categoryName) {
								categoryId = category.id
							}
						}

							mainViewModel.saveButton(
								SingleEntry(
									foodName,
									bbDate,
									categoryId,
									amount,
									portion,
									isChecked,
									timeStampChecked
								)
							)

					},
					modifier = Modifier.padding(top = 20.dp),
					colors = androidx.compose.material.ButtonDefaults.buttonColors(BackgroundBlue)
				) {
					Text(text = "Add", color = White )
				}

			}
		}
	}
}

data class ArticlePreview(
	val articleId: Int,
	val iconResId: Int,
	val heading: String,
	val description: String,
	val introduction: String,
	val subHeader1: String,
	val subHeader2: String,
	val subHeader3: String,
	val subHeader4: String,
	val paragraph1: String,
	val paragraph2: String,
	val paragraph3: String,
	val paragraph4: String,
	val contentPicture1: Int,
	val picHeight1: Int,
	val contentPicture2: Int,
	val picHeight2: Int
)

// Function to get article previews
fun getDummyArticlePreviews(): List<ArticlePreview> {
	return listOf(
		ArticlePreview(
			// https://blog.secondharvest.ca/2022/02/19/everything-you-need-to-know-about-best-before-dates/
			articleId = 1,
			R.drawable.article1_icon,
			"Best Before?",
			"Learn about the storage life of the most important foods! This way, " +
					"you can better manage your fridge items.",
			"There's no standardized system for food dating in this country. So is it really any wonder why dates on" +
					"packaged foods are a bit, baffling? But spoiler alert (pun intended): Food products are safe to consume past the date on the label.",
			"What's Shelf Life?", "After Best Before", "Simple Checks For Overdue Foods", "Why is understanding the Best Before date important?",
			"Shelf-life is the period of time during which a food maintains its acceptable or desirable characteristics " +
					"under specified storage and handling conditions. These acceptable or desirable characteristics can be related " +
					"to the safety or quality of the product and can be microbiological, chemical or physical in nature.",
			"When packaged correctly and stored or frozen at the correct temperature, the following best before date timelines are generally true:\n" +
					"\n" +
					"Canned goods: Last up to one year past the best before date.\n" +
					"Dairy (and eggs): Lasts up to two weeks past the best before date.\n" +
					"Poultry pieces: Last up to six months in the freezer.\n" +
					"Meats (incl. beef, lamb, pork and whole poultry): Last up to one year in the freezer.\n" +
					"Dry cereals: Last up to one year past the best before date.\n" +
					"Packaged snacks (incl. popcorn, granola bars and bagged snacks): Last up to one year past the best before date.\n" +
					"Prepared and frozen meals: Last up to one year past the best before date in the freezer.\n" +
					"Unopened, shelf-stable condiments: Last up to one year past the best before date.\n" +
					"Unopened drinks (incl. juice or coconut water): Last up to one year past the best before date.",
			"1) What temperature was the food stored at?\n" +
					"Regardless of the best before date, perishable food items must be stored at the correct temperature. Two to four hours " +
					"in a bad temperature zone (4-60 degrees celsius) is enough to spoil the food.\n" +
					"\n" +
					"2) How does the packaging look, feel and smell?\n" +
					"Check canned goods and food packaging for bulging, tears, rips, water damage or signs of insects. Look for " +
					"mould, foul smells or discolouration. All of these may be signs that the food has gone bad and " +
					"is not safe to eat, regardless of what the best before date says.\n" +
					"\n" +
					"3) Was the food frozen properly and how is the packaging?\n" +
					"If the frozen items have freezer burn or icicles formed on them — or if the packaging " +
					"is ripped — they may not be safe to eat.",
			"Too much food goes to waste because of a lack of awareness and education. Consumers throw out or avoid purchasing good " +
					"food because it was too close to the best before date. Grocers dump milk and dairy products (that we " +
					"now know are good for another two weeks!) for this very same reason.\n" +
					"But if we all made the commitment to understanding best before dates, think of the food that we could divert from landfills.",
			R.drawable.bb_milk, picHeight1 = 300,
			R.drawable.after_bb_dates, picHeight2 = 480,
		),
		ArticlePreview(
			// https://www.thespruce.com/how-to-organize-a-fridge-5085366
			articleId = 2,
			R.drawable.article2_icon,
			"Fridge Organization",
			"A well-organised fridge not only looks good, being able to glance into your fridge " +
					"and see exactly what's in there can prevent food from going bad, can save you money and " +
					"help to reduce the 5 million tonnes of edible food waste that households produce every year.",
			"When was the last time you spent way too long trying to find that food item you knew you had somewhere in the back of your fridge? " +
					"Even if you are short on space, a well-organized refrigerator is essential to keeping your food fresh and saving you time when it comes to planning meals.\n" +
					"\n" +
					"Not only will organizing your fridge the right way help cut down on the time you spend digging through it to " +
					"find that cream cheese you lost, but it will also help you reduce your grocery bill and waste much less. " +
					"So, here is how to organize the different parts of your fridge:",
			"Top Shelf", "Middle Shelf", "Bottom Shelf", "The Drawers",
			"This is one of the warmer shelves in your fridge, so avoid keeping any fresh meat here. Instead, " +
					"consider designating it for leftovers and takeout—stuff you need to eat soon that will expire more quickly.\n" +
					"\n" +
					"This is typically the first shelf you see when you open your door, so it's also a good zone to keep the foods you reach for often such as quick snacks.",
			"Here, consider storing dairy products or produce that can be left uncovered (such as berries). " +
					"You also probably have the most vertical space here, so taller containers can be stored on this shelf as well.\n" +
					"\n" +
					"This shelf is also good for putting items you reach for most often because it is closer to eye level. " +
					"Placing healthier, easy-to-grab items towards the front of this shelf could remind you and your family to reach for them more often.",
			"Use your bottom shelf for raw meat, fish, or poultry. This is the coolest spot in your fridge so there is less risk of spoilage.\n" +
					"\n" +
					"Furthermore, you also won't have to worry about accidental leakage over the rest of your food. This " +
					"could cause nasty odors or cross-contamination if, for example, juices containing harmful bacteria leak from raw meat packaging onto other foods.",
			"This one is pretty easy—try to fit all of your produce in the crisper drawers for maximum freshness. " +
					"These drawers have vents to control airflow and adequate drainage to prevent moisture which can lead to mold.\n" +
					"\n" +
					"Keeping your refrigerator organized may take a little effort, but it's worth the time it takes upfront to save money and brain space later. " +
					"A few key steps can help your food last longer and get dinner on the table quicker.",
			R.drawable.fridge_org, picHeight1 = 300,
			R.drawable.fridge_org_figma, picHeight2 = 450,
		),
		ArticlePreview(
			// https://www.bafu.admin.ch/bafu/en/home/topics/waste/guide-to-waste-a-z/biodegradable-waste/types-of-waste/lebensmittelabfaelle.html
			// https://www.eufic.org/en/food-safety/article/food-waste-in-europe-statistics-and-facts-about-the-problem
			articleId = 3,
			R.drawable.trash_can,
			"Food Waste in Europe",
			"Would you go into a supermarket, buy three shopping bags of food, and then immediately throw one away? " +
					"Statistically, that’s what’s happening to our food today.",
			"One third of all the food that is produced for human consumption is wasted. When we waste food, " +
					"we waste all the resources that go into producing and transporting the food, such as land, water and fuel use, " +
					"without gaining any of the benefits of feeding people. When food ends up in landfill it also contributes to greenhouse gas emissions. " +
					"Food waste remains a problem in Europe and around the world.",
			"Food Waste vs Loss", "Some Statistics", "Environmental Impact", "Reducing Food Waste at Home",
			"In order to tackle food waste, understanding the problem is key to finding good solutions. A first step is to measure the amount of food that goes to " +
					"waste and to understand where along the supply chain the waste is happening. Depending on where it happens along the supply chain, we use the terms food loss or food waste.\n" +
					"\n" +
					"Food loss refers to any food that is discarded, incinerated or otherwise disposed of along the food supply chain from harvest/slaughter/catch up to, but excluding, the retail level, " +
					"and is not used for any other productive use, such as animal feed or seed.\n" +
					"\n" +
					"Food waste refers to food that is discarded at the level of retailers, food service providers and consumers.",
			"Below, we list some of key statistics and facts about food waste and loss based on current estimates:\n" +
					"\n" +
					"Roughly 1/3 of the food produced in the world for human consumption is lost or wasted.\n" +
					"Food waste alone generates about 8% - 10% of global greenhouse gas emissions.\n" +
					"Latest estimates suggest that around 931 million tonnes of food waste were generated in 2019, out of which 61% came from households.\n" +
					"Around 88 million tonnes of food waste are generated annually in the EU.4 This is equal to 174 kg per person, 143 billion euros or 170 000 000 tonnes of CO2.\n" +
					"Estimates show that up to 10% of the 88 million tonnes of food waste that is generated in the EU every year are somehow linked to date labelling - " +
					"53% of consumers don’t know the meaning of “best before” labelling.\n",
			"Different foods have different environmental impacts. For example, the volume of meat that is wasted and lost is not very high compared to foods such as " +
					"cereals and vegetables. However, meat requires much more resources to produce, so wasting meat still has a significant impact on climate change (estimated " +
					"to contribute to 20% of the carbon footprint of total food waste and loss).",
			"Here are 5 ways you can reduce food waste in your kitchen:\n" +
					"\n" +
					"1) Take an inventory\nTake stock of your pantry, refrigerator and freezer before going to the store to prevent overbuying\n" +
					"2) Create a meal plan\nPlanning at least a few meals for each week is a great way to ensure you have healthy meals. It also prevents you from buying too " +
					"much food because you feel like you need to be prepared for anything.\n" +
					"3) Save and eat leftovers safely\nIf you don't think you will be able to eat your leftovers within three days, store them in the freezer and label them. Keep your freezer " +
					"organized so food doesn't get lost and then thrown out due to freezer burn.\n" +
					"4) Buy \"ugly\" foods.\nPurchasing imperfect food refers to misshaped or oddly shaped fruits or vegetables. They are just as fine to eat as normal-looking food. And " +
					"if you don't eat them, nobody else will, so they'll end up being thrown away by the supermarket.\n" +
					"5) Compost\nEven vegetable peels don't have to go to waste. Backyard composting is a great way to keep food waste out of the landfill and provide nutrition for your garden.",
			R.drawable.food_waste, picHeight1 = 300,
			R.drawable.food_waste_impact, picHeight2 = 550,
		),
	)
}

@Composable
fun ArticlePreviewUI(articlePreview: ArticlePreview, navController: NavController) {
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
				navController.navigate("article/${articlePreview.articleId}")
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

@Composable
fun ArticleScreen(articleId: Int, navController: NavHostController) {

	val selectedArticle = getDummyArticlePreviews().find { it.articleId == articleId }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(White),

	) {
		Header(title = "Article", navController)

		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(top = 20.dp, bottom = 15.dp, start = 27.dp, end = 27.dp)
				.verticalScroll(rememberScrollState()),
		)
		{
			Text(
				text = selectedArticle!!.heading,
				fontWeight = FontWeight.Bold,
				fontSize = 27.sp,
				color = Color.Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 4.dp, bottom = 15.dp)
			)
			Text(
				text = selectedArticle.introduction,
				fontSize = 16.sp,
				color = Color.Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp)
			)
			Image(
				painter = painterResource(id = selectedArticle.contentPicture1),
				contentDescription = null,
				modifier = Modifier
					.fillMaxWidth()
					.height((selectedArticle.picHeight1).dp)
					.padding(top = 15.dp, bottom = 20.dp)
			)
			Text(
				text = selectedArticle.subHeader1,
				fontSize = 20.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 16.dp)
			)
			Text(
				text = selectedArticle.paragraph1,
				fontSize = 16.sp,
				color = Color.Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp)
			)
			Text(
				text = selectedArticle.subHeader2,
				fontSize = 20.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 16.dp)
			)
			Text(
				text = selectedArticle.paragraph2,
				fontSize = 16.sp,
				color = Color.Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp)
			)
			Text(
				text = selectedArticle.subHeader3,
				fontSize = 20.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 16.dp)
			)
			Text(
				text = selectedArticle.paragraph3,
				fontSize = 16.sp,
				color = Color.Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp)
			)
			Text(
				text = selectedArticle.subHeader4,
				fontSize = 20.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 16.dp)
			)
			Text(
				text = selectedArticle.paragraph4,
				fontSize = 16.sp,
				color = Color.Black,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp)
			)
			Image(
				painter = painterResource(id = selectedArticle.contentPicture2),
				contentDescription = null,
				modifier = Modifier
					.fillMaxWidth()
					.height((selectedArticle.picHeight2).dp)
					.padding(top = 15.dp, bottom = 20.dp)
			)
		}
	}
}




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

	amountTaken = portionAmount!!

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
					.fillMaxWidth()
					.padding(7.dp),
				horizontalAlignment = Alignment.CenterHorizontally

			) {
				Text(
					text = "How much did you use?",
					lineHeight = 45.sp,
					fontWeight = FontWeight.Bold,
					fontSize = 25.sp,
					style = TextStyle(fontFamily = FontFamily.SansSerif),
					color = Color.Black,
					textAlign = TextAlign.Center,
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 15.dp),
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
							.fillMaxWidth(0.3f)
							.shadow(3.dp, RectangleShape, false),
						keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
						colors = TextFieldDefaults.colors(
							focusedTextColor = Black,
							unfocusedTextColor = Black,
							focusedContainerColor = White,
							unfocusedContainerColor = White,
							disabledContainerColor = White,
						),
						onValueChange = { newText: String ->
							amountTaken = newText
						},
						label = { Text(text = "Amount", color = Black) }
					)
					Text(
						text = entry.portionType!!,
						color = Black,
						fontSize = 20.sp,
						textAlign = TextAlign.Center
					)
				}

				Row(
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically
				) {
					Button(
						elevation = androidx.compose.material.ButtonDefaults.elevation(0.dp),
						onClick = {
							mainViewModel.dismissAskAmountDialog()
						},
						modifier = Modifier.padding(top = 20.dp),
						colors = androidx.compose.material.ButtonDefaults.buttonColors(Transparent)
					) {
						Text(text = "Cancel", color = Black)
					}
					Button(
						onClick = {
							val takenAmount = amountTaken.toFloatOrNull()

							if (takenAmount != null) {
								val remainingAmount =
									portionAmount?.toFloatOrNull()?.minus(takenAmount)

								if ((remainingAmount != null) && (remainingAmount >= 0)) {
									portionAmount = remainingAmount.toString()
									mainViewModel.dismissAskAmountDialog()

								} else {
									Toast.makeText(
										mContext,
										"Cannot take more than what is in the fridge!",
										Toast.LENGTH_SHORT
									).show()
								}

								if (remainingAmount == 0.0f) {
									isChecked = 1;
									val currentTime = LocalTime.now()
									timeStampChecked = currentTime.toString()
									mainViewModel.dismissAskAmountDialog()
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
						colors = androidx.compose.material.ButtonDefaults.buttonColors(
							BackgroundBlue
						)
					) {
						Text(text = "Save", color = White)
					}
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
