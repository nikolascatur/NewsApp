package com.example.newsapp.presentation.newsnavigator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.R
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.bookmark.BookmarkScreen
import com.example.newsapp.presentation.bookmark.BookmarkViewModel
import com.example.newsapp.presentation.detail.DetailScreen
import com.example.newsapp.presentation.detail.DetailViewModel
import com.example.newsapp.presentation.home.HomeScreen
import com.example.newsapp.presentation.home.HomeViewModel
import com.example.newsapp.presentation.navgraph.Route
import com.example.newsapp.presentation.newsnavigator.component.BottomNavigation
import com.example.newsapp.presentation.newsnavigator.component.NewsBottomNavigation
import com.example.newsapp.presentation.search.SearchScreen
import com.example.newsapp.presentation.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsNavigator() {
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigation(icon = R.drawable.ic_home, title = "home"),
            BottomNavigation(icon = R.drawable.ic_search, title = "search"),
            BottomNavigation(icon = R.drawable.ic_bookmark, title = "bookmark")
        )
    }
    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }

    val isBottomVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route ||
                backStackState?.destination?.route == Route.SearchScreen.route ||
                backStackState?.destination?.route == Route.BookmarkScreen.route
    }

    selectedItem = when (backStackState?.destination?.route) {
        Route.HomeScreen.route -> 0
        Route.SearchScreen.route -> 1
        Route.BookmarkScreen.route -> 2
        else -> 0
    }

    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        if (isBottomVisible) {
            NewsBottomNavigation(
                items = bottomNavigationItems,
                selectedItem = selectedItem,
                onItemClick = { index ->
                    when (index) {
                        0 -> navigateToTab(navController, Route.HomeScreen.route)
                        1 -> navigateToTab(navController, Route.SearchScreen.route)
                        2 -> navigateToTab(navController, Route.BookmarkScreen.route)
                    }
                })
        }
    }) {
        val bottomPadding = it.calculateBottomPadding()
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = bottomPadding)
        ) {
            composable(Route.HomeScreen.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                val articles = viewModel.getNews.collectAsLazyPagingItems()
                HomeScreen(
                    articles = articles,
                    navigateToSearch = { navigateToTab(navController, Route.SearchScreen.route) },
                    navigateToDetail = { article -> navigateToDetail(navController, article) }
                )
            }
            composable(Route.SearchScreen.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                val state = viewModel.state.value
                OnBackClickStateSaver(navController = navController)
                SearchScreen(
                    state = state,
                    event = viewModel::onEvent,
                    navigateToDetail = { article ->
                        navigateToDetail(navController, article)
                    })
            }
            composable(Route.BookmarkScreen.route) {
                val viewModel: BookmarkViewModel = hiltViewModel()
                val state = viewModel.state.value
                OnBackClickStateSaver(navController = navController)
                BookmarkScreen(
                    state = state,
                    navigateToDetail = { article ->
                        navigateToDetail(navController, article)
                    }
                )
            }
            composable(Route.DetailsScreen.route) {
                val viewModel: DetailViewModel = hiltViewModel()
                navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
                    ?.let { article ->
                        DetailScreen(
                            article = article,
                            event = viewModel::onEvent,
                            navigatorUp = { navController.navigateUp() },
                            sideEffect = viewModel.sideEffect
                        )
                    }

            }
        }
    }
}

@Composable
private fun OnBackClickStateSaver(navController: NavController) {
    BackHandler(true) {
        navigateToTab(navController = navController, route = Route.HomeScreen.route)
    }
}

private fun navigateToDetail(navController: NavController, article: Article) {
    with(navController) {
        currentBackStackEntry?.savedStateHandle?.set("article", article)
        navigate(route = Route.DetailsScreen.route)
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}