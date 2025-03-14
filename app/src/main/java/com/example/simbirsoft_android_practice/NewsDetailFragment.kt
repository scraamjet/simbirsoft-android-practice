package com.example.simbirsoft_android_practice

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.simbirsoft_android_practice.databinding.FragmentNewsDetailBinding
import com.example.simbirsoft_android_practice.utils.DateUtils
import com.google.gson.Gson
import dev.androidbroadcast.vbpd.viewBinding


class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val binding by viewBinding(FragmentNewsDetailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val news = getNewsFromPreferences()
        news?.let { bindNewsDetails(it) }
        binding.buttonBackNewsDetail.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun getNewsFromPreferences(): News? {
        val sharedPreferences = requireContext().getSharedPreferences("news_prefs", Context.MODE_PRIVATE)
        val newsJson = sharedPreferences.getString("selected_news", null)
        return newsJson?.let { Gson().fromJson(it, News::class.java) }
    }

    private fun bindNewsDetails(news: News) {
        with(binding) {
            textViewNewsDetailToolbarTitle.text = news.title
            textViewNewsDetailTitle.text = news.title
            textViewNewsDetailTime.text = DateUtils.formatEventDates(news.startDateTime, news.endDateTime)
            textViewNewsDetailOwner.text = news.owner
            textViewNewsDetailAddress.text = news.ownerAddress
            textViewNewsDetailContacts.text = news.ownerContacts
            textViewNewsDetailDescription.text = news.fullDescription

            loadImage(imageViewNewsDetailImage1, news.picturesUrl.getOrNull(0))
            loadImage(imageViewNewsDetailImage2, news.picturesUrl.getOrNull(1))
            loadImage(imageViewNewsDetailImage3, news.picturesUrl.getOrNull(2))
        }
    }

    private fun loadImage(imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(this)
                .load(it)
                .into(imageView)
        }
    }

    companion object {
        fun newInstance() = NewsDetailFragment()
    }
}