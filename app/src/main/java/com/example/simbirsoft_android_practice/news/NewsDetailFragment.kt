package com.example.simbirsoft_android_practice.news

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import coil.load
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.NewsDetail
import com.example.simbirsoft_android_practice.databinding.FragmentNewsDetailBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.utils.DateUtils
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG = "NewsDetailFragment"

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    private val binding by viewBinding(FragmentNewsDetailBinding::bind)
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }
    private val newsRepository by lazy {
        (requireContext().applicationContext as RepositoryProvider).newsRepository
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation()
        loadNewsDetail()
        initClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun loadNewsDetail() {
        val selectedNewsId = newsPrefs.getSelectedNewsId()

        val disposable = newsRepository.getNewsFromCache()
            .doOnSubscribe {
                Log.d(TAG, "Subscribed to news on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.io())
            .flatMap { newsList ->
                val selectedNews = newsList.find { newsItem -> newsItem.id == selectedNewsId }
                    ?.let(NewsMapper::toNewsDetail)
                if (selectedNews != null) {
                    Observable.just(selectedNews)
                } else {
                    Observable.empty()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(
                    TAG,
                    "Binding detail on thread: ${Thread.currentThread().name}"
                )
            }
            .subscribe { newsDetail ->
                bindNewsDetails(newsDetail)
            }

        compositeDisposable.add(disposable)
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
