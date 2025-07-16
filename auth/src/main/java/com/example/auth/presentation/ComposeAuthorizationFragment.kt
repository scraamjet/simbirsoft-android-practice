package com.example.auth.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.auth.di.AuthComponentProvider
import com.example.core.di.MultiViewModelFactory
import com.example.core.navigation.AppRouter
import com.example.core.ui.AppTheme
import com.example.core.utils.launchInLifecycle
import javax.inject.Inject

class ComposeAuthorizationFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory
    private val viewModel by viewModels<AuthorizationViewModel> { viewModelFactory }

    @Inject
    lateinit var appRouter: AppRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = (context.applicationContext as AuthComponentProvider)
            .provideAuthComponent()
        component.injectAuthFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    AuthorizationScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchInLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is AuthorizationEffect.NavigateToHelp -> {
                        appRouter.navigateToHelp(findNavController())
                    }

                    is AuthorizationEffect.FinishActivity -> {
                        requireActivity().finish()
                    }
                }
            }
        }
    }
}

