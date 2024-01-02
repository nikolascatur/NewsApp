package com.example.newsapp.di

import android.app.Application
import androidx.room.Room
import com.example.newsapp.BuildConfig
import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.local.NewsTypeConverter
import com.example.newsapp.data.manager.LocalUserManagerImpl
import com.example.newsapp.data.remote.NewsApi
import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.domain.manager.LocalUserManager
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecase.appentry.AppEntryUseCase
import com.example.newsapp.domain.usecase.appentry.ReadAppEntry
import com.example.newsapp.domain.usecase.appentry.SaveAppEntry
import com.example.newsapp.domain.usecase.news.DeleteArticle
import com.example.newsapp.domain.usecase.news.GetArticle
import com.example.newsapp.domain.usecase.news.GetArticles
import com.example.newsapp.domain.usecase.news.GetNews
import com.example.newsapp.domain.usecase.news.InsertArticle
import com.example.newsapp.domain.usecase.news.NewsUseCases
import com.example.newsapp.domain.usecase.news.SearchNews
import com.example.newsapp.util.Constants.BASE_URL
import com.example.newsapp.util.Constants.NEWS_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLocalUseManager(
        application: Application
    ): LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun providesAppEntry(
        localUserManager: LocalUserManager
    ): AppEntryUseCase = AppEntryUseCase(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )

    @Provides
    @Singleton
    fun providesOkhttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun providesNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun providesNewsRepository(
        newsApi: NewsApi
    ): NewsRepository = NewsRepositoryImpl(newsApi)

    @Provides
    @Singleton
    fun providesNewDataBase(application: Application): NewsDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = NewsDatabase::class.java,
            name = NEWS_DATABASE
        ).addTypeConverter(NewsTypeConverter()).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesNewsDao(newsDatabase: NewsDatabase): NewsDao {
        return newsDatabase.newsDao
    }

    @Provides
    @Singleton
    fun providesNewsUsesCase(
        newsRepository: NewsRepository,
        newsDao: NewsDao
    ): NewsUseCases =
        NewsUseCases(
            GetNews(newsRepository),
            SearchNews(newsRepository),
            DeleteArticle(newsDao),
            GetArticles(newsDao),
            InsertArticle(newsDao),
            GetArticle(newsDao)
        )
}