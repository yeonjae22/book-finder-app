package com.yeonberry.bookfinderapp.di

import com.yeonberry.bookfinderapp.api.SearchService
import com.yeonberry.bookfinderapp.data.repository.SearchRepository
import com.yeonberry.bookfinderapp.data.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        searchService: SearchService
    ): SearchRepository {
        return SearchRepositoryImpl(
            searchService
        )
    }
}