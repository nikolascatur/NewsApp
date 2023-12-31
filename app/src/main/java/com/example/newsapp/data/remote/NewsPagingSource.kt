package com.example.newsapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.remote.dto.NewsResponse
import com.example.newsapp.domain.model.Article

class NewsPagingSource(
    private val sources: String,
    private val searchQuery: String = "",
    private val dynamicNewsResponse: CallNewsResponse
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private var totalNewsCount = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val news =
                dynamicNewsResponse.callNewsResponse(
                    searchQuery,
                    sources,
                    page
                )
            totalNewsCount += news.articles.size
            val articles = news.articles.distinctBy { it.title }
            LoadResult.Page(
                data = articles,
                nextKey = if (totalNewsCount == news.totalResults) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}

interface CallNewsResponse {
    suspend fun callNewsResponse(
        searchQuery: String,
        sources: String,
        page: Int
    ): NewsResponse
}