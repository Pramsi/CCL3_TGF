package com.cc221002.ccl3_tgf.ui.view

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
import com.cc221002.ccl3_tgf.ui.theme.FridgeBlue
import com.cc221002.ccl3_tgf.ui.theme.NavigationBlue
import com.cc221002.ccl3_tgf.ui.view_model.MainViewModel
import kotlinx.coroutines.delay
import java.io.File
import java.time.LocalDate
import java.util.Calendar
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
@RequiresApi(Build.VERSION_CODES.O)
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
					painter = painterResource(id = R.drawable.notification_icon),
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AllCategories(
	mainViewModel: MainViewModel,
	navController: NavHostController,
){
	val state by mainViewModel.mainViewState.collectAsState()
	val categories by mainViewModel.categories.collectAsState()
	val entriesForCategories by mainViewModel.entriesForCategory.collectAsState()
	Column(
		modifier = Modifier
			.background(White)
			.fillMaxSize(),
		verticalArrangement = Arrangement.SpaceEvenly,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {

	Header(mainViewModel,"Your Fridge")
		if(state.openAddDialog){
			AddingPopup(mainViewModel = mainViewModel)
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
						modifier = Modifier
							.fillMaxWidth()
							.background(
								if (entriesForCategories.isEmpty()) {
									BackgroundBlue
								} else {
									BackgroundLightBlue
								}
							)
							.clip(RoundedCornerShape(10.dp)),
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
										mainViewModel.getEntriesByCategory(it.id)
										navController.navigate(Screen.ShowCategoryEntries.route)
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
										mainViewModel.getEntriesByCategory(it.id)
										navController.navigate(Screen.ShowCategoryEntries.route)
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
								mainViewModel.getEntriesByCategory(it.id)
								navController.navigate(Screen.ShowCategoryEntries.route)
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
								mainViewModel.getEntriesByCategory(it.id)
								navController.navigate(Screen.ShowCategoryEntries.route)
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
									mainViewModel.getEntriesByCategory(it.id)
									navController.navigate(Screen.ShowCategoryEntries.route)
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
									mainViewModel.getEntriesByCategory(it.id)
									navController.navigate(Screen.ShowCategoryEntries.route)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun categoryEntries(navController: NavHostController,mainViewModel: MainViewModel){
	val state = mainViewModel.mainViewState.collectAsState()
	val entries = mainViewModel.entriesForCategory.collectAsState()
	val categories by mainViewModel.categories.collectAsState()
	Log.d("CategoryEntries", entries.value.toString())
	Log.d("CategoryEntries", categories.toString())

	Column {

	Header(mainViewModel,"category name")
	LazyColumn(
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.background(FridgeBlue),
	) {
		items(entries.value) { entry ->
			ItemUI(mainViewModel,entry = entry)
		}
	}
	if(state.value.openAddDialog){
		AddingPopup(mainViewModel = mainViewModel)
	}
	}
}

@Composable
fun Header(mainViewModel: MainViewModel,title:String){
		Row(
			modifier = Modifier
				.clip(shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 20.dp))
				.background(NavigationBlue)
				.fillMaxWidth()
				.height(75.dp),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = "$title",
				fontSize = 30.sp,
				fontWeight = FontWeight.Bold,
				color = Color.White
			)
			Spacer(modifier = Modifier.padding(50.dp,0.dp))
			Box(
				modifier = Modifier
					.size(35.dp)
					.clickable { mainViewModel.openAddDialog() }
			){
				Image(
					painter = painterResource(id = R.drawable.add_icon),
					contentDescription = "Fridge",
					contentScale = ContentScale.Fit,
					modifier = Modifier
						.size(35.dp)
				)
			}
		}
	}


@Composable
fun ItemUI(mainViewModel: MainViewModel,entry:SingleEntry) {
	var checkBoxState by remember { mutableStateOf(false) }

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
					.clickable { mainViewModel }
			){
				Checkbox(
					checked = checkBoxState,
					onCheckedChange = { checkBoxState = it },
					colors = CheckboxDefaults.colors(BackgroundLightBlue)
				)
			}
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
					.clickable { mainViewModel.deleteTrip(entry) }
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

	AlertDialog(
		onDismissRequest = {
			mainViewModel.dismissAddDialog()
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
			Text(text = "ADD",
				lineHeight = 45.sp,
				fontWeight = FontWeight.Bold,
				fontSize = 40.sp,
				style = TextStyle(fontFamily = FontFamily.SansSerif),
				color = Color.Black,
				textAlign = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth(),)


			TextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp)
					.shadow(3.dp, RectangleShape,false),
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
					Text(text ="Food Name", color = Black )}
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
						.shadow(3.dp, RectangleShape,false),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					trailingIcon ={Image(
						painter = painterResource(id = R.drawable.arrows_up_down_icon),
						contentDescription = "Calendar",
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
						Text(text ="#", color = Black  )}
				)

				PortionsDropDownMenu(mainViewModel = mainViewModel, selectedPortion = portionSelection){selectedCategory->
					portionSelection = selectedCategory
				}
			}


			Button(
				onClick = {
					mainViewModel.saveButton(SingleEntry(foodName.text, bbDate, categoryId, portionAmount.text.toFloat(), portionSelection, isChecked))
						  },
				modifier = Modifier.padding(top = 20.dp),
				colors = androidx.compose.material.ButtonDefaults.buttonColors(BackgroundBlue)
			) {
				Text(text = "Add", color = White )
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
					.shadow(3.dp, RectangleShape,false),
				label= { Text(text = "Categories", color = Black)},
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
					.shadow(3.dp, RectangleShape,false),
				label= { Text(text = "Portion(s)", color = Black)},
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
			.padding(top=20.dp)
			.shadow(3.dp, RectangleShape,false),
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
			label = { Text(text = "Best-Before Date", color = Black) },
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