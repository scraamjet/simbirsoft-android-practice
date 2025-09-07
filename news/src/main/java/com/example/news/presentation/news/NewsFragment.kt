package com.example.news.presentation.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.core.di.MultiViewModelFactory
import com.example.core.navigation.AppRouter
import com.example.core.ui.AppTheme
import com.example.core.utils.collectAsEffect
import com.example.news.R
import com.example.news.di.NewsComponentProvider
import javax.inject.Inject

class NewsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val viewModel by viewModels<NewsViewModel> { viewModelFactory }

    @Inject
    lateinit var appRouter: AppRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = (requireContext().applicationContext as NewsComponentProvider)
            .provideNewsComponent()
        component.injectNewsFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    val effect = viewModel.effect.collectAsEffect()

                    effect?.let { handledEffect -> handleEffect(handledEffect) }

                    NewsScreen(
                        state = uiState,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }

    private fun handleEffect(effect: NewsEffect) {
        when (effect) {
            is NewsEffect.NavigateToNewsDetail -> appRouter.navigateToNewsDetail(findNavController(), effect.newsId)
            is NewsEffect.NavigateToFilter -> appRouter.navigateToFilter(findNavController())
            is NewsEffect.ShowErrorToast -> Toast.makeText(requireContext(), R.string.news_load_error, Toast.LENGTH_SHORT).show()
        }
    }
}


