package com.example.simbirsoft_android_practice.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import coil.load
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.AssetJsonReader
import com.example.simbirsoft_android_practice.data.NewsDetail
import com.example.simbirsoft_android_practice.databinding.FragmentNewsDetailBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.utils.DateUtils
import dev.androidbroadcast.vbpd.viewBinding

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    private val binding by viewBinding(FragmentNewsDetailBinding::bind)
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation()
        getNewsDetail()?.let(::bindNewsDetails)
        initClickListeners()
    }

    private fun getNewsDetail(): NewsDetail? {
        val selectedNewsId = newsPrefs.getSelectedNewsId()
        val newsList = AssetJsonReader(requireContext()).parseNews()
        val selectedNews = newsList.find { news -> news.id == selectedNewsId }
        return selectedNews?.let(NewsMapper::toNewsDetail)
    }

    private fun bindNewsDetails(news: NewsDetail) {
        with(binding) {
            textViewNewsDetailToolbarTitle.text = news.title
            textViewNewsDetailTitle.text = news.title
            textViewNewsDetailTime.text =
                DateUtils.formatEventDates(
                    requireContext(),
                    news.startDateTime,
                    news.endDateTime,
                )
            textViewNewsDetailOwner.text = news.owner
            textViewNewsDetailAddress.text = news.ownerAddress
            textViewNewsDetailContacts.text = news.ownerContacts
            textViewNewsDetailDescription.text = news.fullDescription
            listOf(imageViewNewsDetailImage1, imageViewNewsDetailImage2, imageViewNewsDetailImage3)
                .zip(news.picturesUrl) { imageView, url -> imageView.load(url) }
        }
    }

    private fun initClickListeners() {
        binding.buttonBackNewsDetail.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDetach() {
        super.onDetach()
        (activity as? MainActivity)?.showBottomNavigation()
    }

    companion object {
        fun newInstance() = NewsDetailFragment()
    }
}
