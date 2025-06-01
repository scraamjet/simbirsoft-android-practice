package com.example.simbirsoft_android_practice.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.simbirsoft_android_practice.AuthorizationViewModel
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentAuthorizationBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.utils.textChangesFlow
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val DRAWABLE_END_INDEX = 2

class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding by viewBinding(FragmentAuthorizationBinding::bind)
    private val viewModel: AuthorizationViewModel by viewModels()

    private var isPasswordVisible = false

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        observeInputFields()
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
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun observeInputFields() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.editTextAuthorizationEmail.textChangesFlow()
                .collectLatest { text ->
                    viewModel.onEmailChanged(text.toString())
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.editTextAuthorizationPassword.textChangesFlow()
                .collectLatest { text ->
                    viewModel.onPasswordChanged(text.toString())
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFormValid.collectLatest { isFormValid ->
                binding.buttonAuthorization.isEnabled = isFormValid
            }
        }
    }


    private fun togglePasswordVisibility() {
        val passwordField = binding.editTextAuthorizationPassword

        isPasswordVisible = !isPasswordVisible

        passwordField.transformationMethod =
            if (isPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }

        passwordField.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            ContextCompat.getDrawable(
                requireContext(),
                if (isPasswordVisible) {
                    R.drawable.ic_hide_password
                } else {
                    R.drawable.ic_open_password
                },
            ),
            null,
        )
    }
}

