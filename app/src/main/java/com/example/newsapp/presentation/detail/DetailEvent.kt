package com.example.newsapp.presentation.detail

import com.example.newsapp.domain.model.Article

sealed class DetailEvent {

    data class InsertDeleteArticle(val article: Article): DetailEvent()
    object RemoveSideEffect : DetailEvent()
}