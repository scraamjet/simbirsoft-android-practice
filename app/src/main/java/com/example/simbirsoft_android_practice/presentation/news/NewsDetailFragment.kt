package com.example.simbirsoft_android_practice.presentation.news

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
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.utils.DateUtils
import com.example.simbirsoft_android_practice.databinding.FragmentNewsDetailBinding
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.domain.model.NewsDetail
import com.example.simbirsoft_android_practice.launchInLifecycle
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    private val binding by viewBinding(FragmentNewsDetailBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val args: NewsDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<NewsDetailViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        observeState()
        observeEffect()
        viewModel.onEvent(NewsDetailEvent.Load(newsId = args.newsId))
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
                    is NewsDetailEffect.ShowErrorToast -> showToast(effect.messageResId)
                }
            }
        }
    }

    private fun showToast(
        @StringRes messageResId: Int,
    ) {
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
            viewModel.handleOnBackClicked()
        }
    }
}
