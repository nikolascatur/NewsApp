package com.example.newsapp.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.usecase.appentry.AppEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val useCase: AppEntryUseCase
) : ViewModel() {

    fun onEvent(onBoardingEvent: OnBoardingEvent) {
        when(onBoardingEvent) {
            is OnBoardingEvent.SaveAppEntry -> {
                saveAppEntry()
            }
        }
    }


    private fun saveAppEntry() {
        viewModelScope.launch{
            useCase.saveAppEntry()
        }
    }

}