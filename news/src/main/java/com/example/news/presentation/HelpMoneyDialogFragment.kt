package com.example.news.presentation

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core.di.MultiViewModelFactory
import com.example.news.databinding.DialogFragmentHelpMoneyBinding
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.graphics.drawable.toDrawable
import com.example.core.utils.launchInLifecycle
import com.example.news.R
import com.example.news.di.NewsComponentProvider

class HelpMoneyDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentHelpMoneyBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory
    private val viewModel: HelpMoneyViewModel by viewModels { viewModelFactory }

    private val newsId: Int by lazy {
        requireArguments().getInt(DonateDialogKeys.NEWS_ID)
    }

    private val newsTitle: String by lazy {
        requireArguments().getString(DonateDialogKeys.NEWS_TITLE).orEmpty()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.onEvent(HelpMoneyEvent.PermissionGranted)
        } else {
            viewModel.onEvent(HelpMoneyEvent.PermissionDenied)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = (context.applicationContext as NewsComponentProvider)
            .provideNewsComponent()
        component.injectHelpMoneyDialog(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentHelpMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            val layoutWidth = (resources.displayMetrics.widthPixels * 0.9).toInt()
            window.setLayout(
                layoutWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(HelpMoneyEvent.Init(newsId, newsTitle))
        initListeners()
        observeState()
        observeEffect()
    }

    private fun initListeners() = with(binding) {
        defaultMoneySum.addOnButtonCheckedListener { _, _, _ ->
            val checkedId = defaultMoneySum.checkedButtonId
            val amount = when (checkedId) {
                R.id.buttonMoneySumOneHundred -> 100
                R.id.buttonMoneySumFiveHundred -> 500
                R.id.buttonMoneySumOneThousand -> 1000
                R.id.buttonMoneySumTwoThousand -> 2000
                else -> null
            }
            if (amount != null) {
                viewModel.onEvent(HelpMoneyEvent.SelectPredefinedAmount(amount))
                editTextMoneySum.setText("")
            }
        }

        editTextMoneySum.doAfterTextChanged { text ->
            viewModel.onEvent(
                HelpMoneyEvent.InputCustomAmount(
                    text = text?.toString().orEmpty()
                )
            )
        }

        buttonCancel.setOnClickListener {
            dismiss()
        }

        buttonSend.setOnClickListener {
            viewModel.onEvent(HelpMoneyEvent.SendClicked)
        }
    }

    private fun observeState() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            viewModel.state.collect { state ->
                binding.buttonSend.isEnabled = state.isValid
            }
        }
    }

    private fun observeEffect() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    HelpMoneyEffect.Dismiss -> dismiss()
                    HelpMoneyEffect.RequestNotificationPermission -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            viewModel.onEvent(HelpMoneyEvent.PermissionGranted)
                        }
                    }

                    HelpMoneyEffect.ShowPermissionDeniedMessage -> {
                        Toast.makeText(
                            requireContext(),
                            "Разрешение не предоставлено",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
