package com.example.newsapp.presentation.mainactivity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.usecase.appentry.AppEntryUseCase
import com.example.newsapp.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: AppEntryUseCase
) : ViewModel() {

    private val _splashCondition = mutableStateOf(true)
    val splashCondition: State<Boolean> = _splashCondition

    private val _startDestination = mutableStateOf(Route.AppStartNavigation.route)
    val startDestination: State<String> = _startDestination

    init {
        useCase.readAppEntry().onEach { shouldStartFromHome ->
            _startDestination.value = if (shouldStartFromHome) {
                Route.NewsNavigation.route
            } else {
                Route.AppStartNavigation.route
            }
            delay(200L)
            _splashCondition.value = false
        }.launchIn(viewModelScope)
    }
}