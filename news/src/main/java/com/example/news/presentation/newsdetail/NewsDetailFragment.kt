package com.example.news.presentation.newsdetail

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.core.di.MultiViewModelFactory
import com.example.core.utils.launchInLifecycle
import com.example.news.utils.DateUtils
import com.example.news.R
import com.example.news.databinding.FragmentNewsDetailBinding
import com.example.news.di.NewsComponentProvider
import com.example.core.model.NewsDetail
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

private const val NEWS_ID_KEY = "newsId"

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    private val binding by viewBinding(FragmentNewsDetailBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val newsId: Int by lazy {
        requireArguments().getInt(NEWS_ID_KEY)
    }

    private val viewModel by viewModels<NewsDetailViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = (context.applicationContext as NewsComponentProvider)
            .provideNewsComponent()
        component.injectNewsDetailFragment(this)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        observeState()
        observeEffect()
        viewModel.onEvent(NewsDetailEvent.Load(newsId = newsId))
    }

    private fun observeState() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            viewModel.state.collect { state ->
                when (state) {
                    is NewsDetailState.Idle -> Unit
                    is NewsDetailState.Result -> showResult(newsDetail = state.newsDetail)
                    is NewsDetailState.Error -> hideContentOnError()
                }
            }
        }
    }

    private fun observeEffect() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is NewsDetailEffect.NavigateBack -> findNavController().navigateUp()
                    is NewsDetailEffect.ShowErrorToast -> showToast(R.string.news_detail_load_error)
                }
            }
        }
    }

    private fun showToast(@StringRes messageResId: Int) {
        Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show()
    }

    private fun hideContentOnError() {
        binding.frameLayoutNewsDetailContainer.isVisible = false
    }

    private fun showResult(newsDetail: NewsDetail) {
        with(binding) {
            frameLayoutNewsDetailContainer.isVisible = true
            textViewNewsDetailToolbarTitle.text = newsDetail.title
            textViewNewsDetailTitle.text = newsDetail.title
            textViewNewsDetailTime.text =
                DateUtils.formatEventDates(
                    requireContext(),
                    newsDetail.startDateTime,
                    newsDetail.endDateTime,
                )
            textViewNewsDetailOwner.text = newsDetail.owner
            textViewNewsDetailAddress.text = newsDetail.ownerAddress
            textViewNewsDetailContacts.text = newsDetail.ownerContacts
            textViewNewsDetailDescription.text = newsDetail.fullDescription

            listOf(
                imageViewNewsDetailMainImage,
                imageViewNewsDetailPrimaryImage,
                imageViewNewsDetailSecondaryImage,
            ).zip(newsDetail.picturesUrl) { imageView, url -> imageView.load(url) }
        }
    }

    private fun initClickListeners() {
        binding.buttonBackNewsDetail.setOnClickListener {
            viewModel.onEvent(NewsDetailEvent.OnBackClicked)
        }
    }
}
