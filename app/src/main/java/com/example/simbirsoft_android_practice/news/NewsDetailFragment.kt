package com.example.simbirsoft_android_practice.news

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.databinding.FragmentNewsDetailBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.model.NewsDetail
import com.example.simbirsoft_android_practice.utils.DateUtils
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG_NEWS_DETAIL_FRAGMENT = "NewsDetailFragment"

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    private val binding by viewBinding(FragmentNewsDetailBinding::bind)
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }
    private val eventRepository by lazy {
        RepositoryProvider.fromContext(requireContext()).eventRepository
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation()

        loadNewsDetail()
        initClickListeners()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as? MainActivity)?.showBottomNavigation()
    }

    private fun loadNewsDetail() {
        val selectedNewsId = newsPrefs.getSelectedNewsId()

        viewLifecycleOwner.lifecycleScope.launch {
            eventRepository.getEvents(null)
                .flowOn(Dispatchers.IO)
                .map { newsList ->
                    newsList.find { newsItem -> newsItem.id == selectedNewsId }
                        ?.let(NewsMapper::eventToNewsDetail)
                }
                .filterNotNull()
                .catch { throwable ->
                    Log.e(
                        TAG_NEWS_DETAIL_FRAGMENT,
                        "Flow exception: ${throwable.localizedMessage}",
                        throwable,
                    )
                }
                .collect { newsDetail ->
                    bindNewsDetails(newsDetail)
                }
        }
    }

    private fun bindNewsDetails(news: NewsDetail) {
        with(binding) {
            textViewNewsDetailToolbarTitle.text = news.title
            textViewNewsDetailTitle.text = news.title
            textViewNewsDetailTime.text =
                DateUtils.formatEventDates(requireContext(), news.startDateTime, news.endDateTime)
            textViewNewsDetailOwner.text = news.owner
            textViewNewsDetailAddress.text = news.ownerAddress
            textViewNewsDetailContacts.text = news.ownerContacts
            textViewNewsDetailDescription.text = news.fullDescription
            listOf(
                imageViewNewsDetailMainImage,
                imageViewNewsDetailPrimaryImage,
                imageViewNewsDetailSecondaryImage,
            )
                .zip(news.picturesUrl) { imageView, url -> imageView.load(url) }
        }
    }

    private fun initClickListeners() {
        binding.buttonBackNewsDetail.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        fun newInstance() = NewsDetailFragment()
    }
}
