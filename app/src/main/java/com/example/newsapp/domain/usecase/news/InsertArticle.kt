package com.example.newsapp.domain.usecase.news

import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.domain.model.Article

class InsertArticle(private val newsDao: NewsDao) {

    suspend operator fun invoke(article: Article) {
        newsDao.insertArticle(article)
    }
}