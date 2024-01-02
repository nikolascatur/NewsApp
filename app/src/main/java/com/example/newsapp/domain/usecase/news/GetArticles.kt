package com.example.newsapp.domain.usecase.news

import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.domain.model.Article
import kotlinx.coroutines.flow.Flow

class GetArticles(private val newsDao: NewsDao) {

    operator fun invoke(): Flow<List<Article>> = newsDao.getAllArticle()
}