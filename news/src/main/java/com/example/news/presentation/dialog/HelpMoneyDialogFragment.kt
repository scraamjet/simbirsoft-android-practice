package com.example.news.presentation.dialog

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.core.di.MultiViewModelFactory
import com.example.core.utils.launchInLifecycle
import com.example.news.R
import com.example.news.databinding.DialogFragmentHelpMoneyBinding
import com.example.news.di.NewsComponentProvider
import javax.inject.Inject

class HelpMoneyDialogFragment : DialogFragment() {

    private var _binding: DialogFragmentHelpMoneyBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory
    private val viewModel: HelpMoneyViewModel by viewModels { viewModelFactory }

    private val newsId: Int by lazy {
        requireArguments().getInt(NEWS_ID)
    }

    private val newsTitle: String by lazy {
        requireArguments().getString(NEWS_TITLE).orEmpty()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.onEvent(HelpMoneyEvent.PermissionGranted)
        } else {
            val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.POST_NOTIFICATIONS
            )
            viewModel.onEvent(
                HelpMoneyEvent.PermissionDenied(shouldShowRationale = shouldShowRationale)
            )
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
            val amountEnum = when (defaultMoneySum.checkedButtonId) {
                R.id.buttonMoneySumOneHundred -> DonateAmount.ONE_HUNDRED
                R.id.buttonMoneySumFiveHundred -> DonateAmount.FIVE_HUNDRED
                R.id.buttonMoneySumOneThousand -> DonateAmount.ONE_THOUSAND
                R.id.buttonMoneySumTwoThousand -> DonateAmount.TWO_THOUSAND
                else -> null
            }
            amountEnum?.let { donateAmount ->
                viewModel.onEvent(HelpMoneyEvent.SelectPredefinedAmount(donateAmount))
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
            viewModel.onEvent(HelpMoneyEvent.OnCancelClicked)
        }

        buttonSend.setOnClickListener {
            viewModel.onEvent(HelpMoneyEvent.OnSendClicked)
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
                            val permission = Manifest.permission.POST_NOTIFICATIONS
                            when {
                                ContextCompat.checkSelfPermission(
                                    requireContext(), permission
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    viewModel.onEvent(HelpMoneyEvent.PermissionGranted)
                                }

                                else -> {
                                    requestPermissionLauncher.launch(permission)
                                }
                            }
                        } else {
                            viewModel.onEvent(HelpMoneyEvent.PermissionGranted)
                        }
                    }

                    HelpMoneyEffect.OpenNotificationSettings -> {
                        showSettingsDialog()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.notification_permission_required_title))
            .setMessage(getString(R.string.notification_permission_required_message))
            .setPositiveButton(R.string.open_settings_button) { _, _ -> openAppSettings() }
            .setNegativeButton(R.string.cancel_settings_button, null)
            .show()
    }

    private fun openAppSettings() {
        startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", requireContext().packageName, null)
            }
        )
    }

    companion object Keys {
        const val NEWS_ID = "newsId"
        const val NEWS_TITLE = "newsTitle"
    }
}
