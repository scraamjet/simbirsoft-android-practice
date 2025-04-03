package com.example.simbirsoft_android_practice.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import coil.load
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.data.NewsDetail
import com.example.simbirsoft_android_practice.databinding.FragmentNewsDetailBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.utils.DateUtils
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    private val binding by viewBinding(FragmentNewsDetailBinding::bind)
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(requireContext())) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation()
        loadNewsDetail()
        initClickListeners()
    }

    @SuppressLint("CheckResult")
    private fun loadNewsDetail() {
        val selectedNewsId = newsPrefs.getSelectedNewsId()

        newsRepository.getNews()
            .subscribeOn(Schedulers.io())
            .doOnNext { Log.d("RxJava", "Fetched news on thread: ${Thread.currentThread().name}") }
            .observeOn(Schedulers.computation())
            .mapNotNull { newsList ->
                newsList.find { it.id == selectedNewsId }?.let(NewsMapper::toNewsDetail)
            }
            .doOnNext {
                Log.d(
                    "RxJava",
                    "Mapped news detail on thread: ${Thread.currentThread().name}"
                )
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newsDetail ->
                newsDetail.let(::bindNewsDetails)
            }, { error ->
                Log.e("RxJava", "Error fetching news details", error)
            })
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
                imageViewNewsDetailSecondaryImage
            )
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

    private fun <T : Any, R : Any> Observable<T>.mapNotNull(transform: (T) -> R?) =
        flatMapIterable { value ->
            listOfNotNull(transform(value))
        }
}

