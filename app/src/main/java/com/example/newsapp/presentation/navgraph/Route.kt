package com.example.newsapp.presentation.navgraph

import androidx.navigation.NamedNavArgument

sealed class Route(
    val route: String,
    val navArgument: List<NamedNavArgument> = emptyList()
) {
    object OnBoardingScreen : Route("OnBoardingScreen")
    object HomeScreen : Route("HomeScreen")
    object SearchScreen : Route("SearchScreen")
    object BookmarkScreen : Route(route = "bookMarkScreen")
    object DetailsScreen : Route(route = "detailsScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object NewsNavigation : Route(route = "newsNavigation")
}
