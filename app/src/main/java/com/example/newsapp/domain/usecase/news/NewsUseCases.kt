package com.example.newsapp.domain.usecase.news

data class NewsUseCases(
    val getNews: GetNews,
    val searchNews: SearchNews,
    val deleteArticle: DeleteArticle,
    val getArticles: GetArticles,
    val insertArticle: InsertArticle,
    val getArticle: GetArticle
)
