package com.example.news.di

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
import com.example.news.presentation.newsdetail.NewsDetailViewModel
import com.example.news.domain.usecase.NewsUseCase
import com.example.news.domain.usecase.NewsUseCaseImpl
import com.example.news.presentation.news.NewsViewModel
import com.example.news.domain.usecase.NewsDetailUseCase
import com.example.news.domain.usecase.NewsDetailUseCaseImpl
import com.example.news.presentation.HelpMoneyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface NewsModule {

    @Binds
    fun bindNewsUseCase(impl: NewsUseCaseImpl): NewsUseCase

    @Binds
    fun bindNewsDetailUseCase(impl: NewsDetailUseCaseImpl): NewsDetailUseCase

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailViewModel::class)
    fun bindNewsDetailViewModel(viewModel: NewsDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HelpMoneyViewModel::class)
    fun bindHelpMoneyViewModel(viewModel: HelpMoneyViewModel): ViewModel
}
