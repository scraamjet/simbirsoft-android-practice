package com.example.simbirsoft_android_practice.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentAuthorizationBinding
import com.example.simbirsoft_android_practice.help.HelpFragment
import com.example.simbirsoft_android_practice.main.MainActivity
import com.jakewharton.rxbinding4.widget.textChanges
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

private const val KEY_EMAIL = "key_email"
private const val KEY_PASSWORD = "key_password"
private const val MIN_INPUT_LENGTH = 6
private const val DRAWABLE_END_INDEX = 2


class AuthorizationFragment : Fragment(R.layout.fragment_authorization) {
    private val binding by viewBinding(FragmentAuthorizationBinding::bind)
    private val compositeDisposable = CompositeDisposable()
    private var passwordVisibility = VisibilityPassword.OPEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation()

        initClickListeners()
        observeInputFields()
        restoreInputState(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListeners() {
        binding.imageViewFilterBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.buttonAuthorization.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, HelpFragment.newInstance())
                .commit()
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
        val emailObservable = binding.editTextAuthorizationEmail.textChanges()
            .map { email -> email.length >= MIN_INPUT_LENGTH }

        val passwordObservable = binding.editTextAuthorizationPassword.textChanges()
            .map { password -> password.length >= MIN_INPUT_LENGTH }

        val combinedDisposable = Observable.combineLatest(emailObservable, passwordObservable)
        { isEmailValid, isPasswordValid -> isEmailValid && isPasswordValid }
            .subscribe { isFormValid ->
                binding.buttonAuthorization.isEnabled = isFormValid
            }

        compositeDisposable.add(combinedDisposable)
    }

    private fun togglePasswordVisibility() {
        val passwordField = binding.editTextAuthorizationPassword
        val cursorPosition = passwordField.selectionStart

        passwordVisibility = passwordVisibility.toggle()

        passwordField.transformationMethod = if (passwordVisibility.isVisible) {
            PasswordTransformationMethod.getInstance()
        } else {
            HideReturnsTransformationMethod.getInstance()
        }

        passwordField.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            ContextCompat.getDrawable(requireContext(), passwordVisibility.iconRes),
            null
        )

        passwordField.setSelection(cursorPosition)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EMAIL, binding.editTextAuthorizationEmail.text.toString())
        outState.putString(KEY_PASSWORD, binding.editTextAuthorizationPassword.text.toString())
    }

    private fun restoreInputState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            binding.editTextAuthorizationEmail.setText(bundle.getString(KEY_EMAIL, ""))
            binding.editTextAuthorizationPassword.setText(bundle.getString(KEY_PASSWORD, ""))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onDetach() {
        super.onDetach()
        (activity as? MainActivity)?.showBottomNavigation()
    }

    companion object {
        fun newInstance() = AuthorizationFragment()
    }
}
