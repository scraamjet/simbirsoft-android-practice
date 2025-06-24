package com.example.simbirsoft_android_practice.news

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentNewsDetailBinding
import com.example.simbirsoft_android_practice.domain.model.NewsDetail
import com.example.simbirsoft_android_practice.utils.DateUtils
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListeners()
        observeState()
        observeEffect()
        viewModel.onEvent(NewsDetailEvent.LoadNewsDetail(newsId = args.newsId))
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state: NewsDetailState? ->
                    when (state) {
                        is NewsDetailState.Result -> showResult(newsDetail = state.newsDetail)
                        is NewsDetailState.Error -> showError(message = state.message)
                        null -> Unit
                    }
                }
            }
        }
    }

    private fun observeEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect: NewsDetailEffect ->
                    when (effect) {
                        is NewsDetailEffect.NavigateBack -> findNavController().navigateUp()
                    }
                }
            }
        }
    }

    private fun showResult(newsDetail: NewsDetail) {
        with(binding) {
            textViewNewsDetailToolbarTitle.text = newsDetail.title
            textViewNewsDetailTitle.text = newsDetail.title
            textViewNewsDetailTime.text =
                DateUtils.formatEventDates(
                    requireContext(),
                    newsDetail.startDateTime,
                    newsDetail.endDateTime
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

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun initClickListeners() {
        binding.buttonBackNewsDetail.setOnClickListener {
            viewModel.onBackClicked()
        }
    }
}
