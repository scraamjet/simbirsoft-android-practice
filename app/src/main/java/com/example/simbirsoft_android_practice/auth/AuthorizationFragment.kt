package com.example.simbirsoft_android_practice.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentAuthorizationBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.utils.textChangesFlow
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DRAWABLE_END_INDEX = 2

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding by viewBinding(FragmentAuthorizationBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<AuthorizationViewModel> { viewModelFactory }

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
        observePasswordVisibility()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {
        binding.imageViewFilterBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.buttonAuthorization.setOnClickListener {
            findNavController().navigate(R.id.action_authorization_to_help)
            (activity as? MainActivity)?.startAndBindNewsService()
        }

        binding.editTextAuthorizationPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawable =
                    binding.editTextAuthorizationPassword.compoundDrawables[DRAWABLE_END_INDEX]
                val rightDrawableX =
                    binding.editTextAuthorizationPassword.right - drawable.bounds.width()
                if (event.rawX >= rightDrawableX) {
                    viewModel.togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun observeInputFields() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    binding.editTextAuthorizationEmail.textChangesFlow()
                        .collectLatest { text ->
                            viewModel.onEmailChanged(text.toString())
                        }
                }

                launch {
                    binding.editTextAuthorizationPassword.textChangesFlow()
                        .collectLatest { text ->
                            viewModel.onPasswordChanged(text.toString())
                        }
                }

                launch {
                    viewModel.isFormValid.collectLatest { isFormValid ->
                        binding.buttonAuthorization.isEnabled = isFormValid
                    }
                }
            }
        }
    }

    private fun observePasswordVisibility() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isPasswordVisible.collectLatest { isVisible ->
                    val passwordField = binding.editTextAuthorizationPassword
                    passwordField.transformationMethod = if (isVisible) {
                        HideReturnsTransformationMethod.getInstance()
                    } else {
                        PasswordTransformationMethod.getInstance()
                    }

                    passwordField.setCompoundDrawablesWithIntrinsicBounds(
                        null, null,
                        ContextCompat.getDrawable(
                            requireContext(),
                            if (isVisible) R.drawable.ic_hide_password else R.drawable.ic_open_password
                        ),
                        null
                    )
                }
            }
        }
    }
}

