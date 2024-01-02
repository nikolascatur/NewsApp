package com.example.newsapp.presentation.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.usecase.news.NewsUseCases
import com.example.newsapp.presentation.onboarding.component.newsAppPreview
import com.example.newsapp.util.UIComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val newsUseCases: NewsUseCases
) : ViewModel() {

    var sideEffect by mutableStateOf<UIComponent?>(null)
        private set

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.InsertDeleteArticle -> {
                viewModelScope.launch {
                    val tmpArticle = newsUseCases.getArticle(url = event.article.url)
                    if (tmpArticle == null) {
                        newsUseCases.insertArticle(article = event.article)
                    } else {
                        newsUseCases.deleteArticle(tmpArticle)
                    }
                }
            }

            is DetailEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private suspend fun deleteArticle(article: Article) {
        newsUseCases.deleteArticle(article)
        sideEffect = UIComponent.Toast("Article Delete")
    }

    private suspend fun insertArticle(article: Article) {
        newsUseCases.insertArticle(article)
        sideEffect = UIComponent.Toast("Insert Article")
    }

}