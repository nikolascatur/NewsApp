package com.example.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.remote.CallNewsResponse
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.remote.NewsPagingSource
import com.example.newsapp.data.remote.dto.NewsResponse
import com.example.newsapp.domain.model.Article
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(private val newsApi: NewsApi) : NewsRepository {
    override fun getNews(sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                NewsPagingSource(sources = sources.joinToString(separator = ","),
                    dynamicNewsResponse = object : CallNewsResponse {
                        override suspend fun callNewsResponse(
                            searchQuery: String,
                            sources: String,
                            page: Int,
                        ): NewsResponse {
                            return newsApi.getNews(sources = sources, page = page)
                        }
                    })
            }).flow
    }

    override fun searchNews(searchQuery: String, sources: List<String>): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                NewsPagingSource(sources = sources.joinToString(separator = ","),
                    searchQuery = searchQuery,
                    dynamicNewsResponse = object : CallNewsResponse {
                        override suspend fun callNewsResponse(
                            searchQuery: String,
                            sources: String,
                            page: Int
                        ): NewsResponse {
                            return newsApi.getSearch(searchQuery, sources, page)
                        }
                    })
            }).flow
    }
}