package com.example.newsapp.domain.usecase.news

import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.domain.model.Article

class DeleteArticle(val newsDao: NewsDao) {

    suspend operator fun invoke(article: Article) {
        newsDao.deleteArticle(article = article)
    }
}