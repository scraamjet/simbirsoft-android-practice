package com.example.simbirsoft_android_practice.presentation.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.simbirsoft_android_practice.AppRouter
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.utils.textChangesFlow
import com.example.simbirsoft_android_practice.databinding.FragmentAuthorizationBinding
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.core.utils.launchInLifecycle
import com.example.simbirsoft_android_practice.presentation.main.MainViewModel
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

private const val DRAWABLE_END_INDEX = 2

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding by viewBinding(FragmentAuthorizationBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val authorizationViewModel by viewModels<AuthorizationViewModel> { viewModelFactory }
    private val mainViewModel by activityViewModels<MainViewModel> { viewModelFactory }

    @Inject
    lateinit var appRouter: AppRouter

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
        observeInputFields()
        observeState()
        observeEffect()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {
        binding.imageViewFilterBack.setOnClickListener {
            authorizationViewModel.onEvent(AuthorizationEvent.BackClicked)
        }

        binding.buttonAuthorization.setOnClickListener {
            authorizationViewModel.onEvent(AuthorizationEvent.SubmitClicked)
        }

        binding.editTextAuthorizationPassword.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                val drawableEnd =
                    binding.editTextAuthorizationPassword.compoundDrawables[DRAWABLE_END_INDEX]
                val drawableX =
                    binding.editTextAuthorizationPassword.right - drawableEnd.bounds.width()
                if (motionEvent.rawX >= drawableX) {
                    authorizationViewModel.onEvent(AuthorizationEvent.TogglePasswordVisibility)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun observeInputFields() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            binding.editTextAuthorizationEmail.textChangesFlow()
                .collectLatest { editable ->
                    authorizationViewModel.onEvent(
                        AuthorizationEvent.EmailChanged(editable.toString())
                    )
                }
        }

        launchInLifecycle(Lifecycle.State.STARTED) {
            binding.editTextAuthorizationPassword.textChangesFlow()
                .collectLatest { editable ->
                    authorizationViewModel.onEvent(
                        AuthorizationEvent.PasswordChanged(editable.toString())
                    )
                }
        }
    }

    private fun observeState() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            authorizationViewModel.state.collectLatest { state ->
                binding.buttonAuthorization.isEnabled = state.isFormValid

                binding.editTextAuthorizationPassword.transformationMethod =
                    if (state.isPasswordVisible) {
                        HideReturnsTransformationMethod.getInstance()
                    } else {
                        PasswordTransformationMethod.getInstance()
                    }

                val iconResId =
                    if (state.isPasswordVisible) {
                        R.drawable.ic_hide_password
                    } else {
                        R.drawable.ic_open_password
                    }

                binding.editTextAuthorizationPassword.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(requireContext(), iconResId),
                    null,
                )
            }
        }
    }

    private fun observeEffect() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            authorizationViewModel.effect.collectLatest { effect ->
                when (effect) {
                    is AuthorizationEffect.NavigateToHelp -> {
                        appRouter.navigateToHelp(findNavController())
                    }

                    is AuthorizationEffect.StartEventService -> {
                        mainViewModel.requestStartEventService()
                    }

                    is AuthorizationEffect.FinishActivity -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }
}
