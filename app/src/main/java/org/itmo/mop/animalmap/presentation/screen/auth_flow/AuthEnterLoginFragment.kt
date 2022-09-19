package org.itmo.mop.animalmap.presentation.screen.auth_flow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.databinding.FragmentEnterLoginBinding

@AndroidEntryPoint
class AuthEnterLoginFragment: BaseAuthFlowFragment<FragmentEnterLoginBinding>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentEnterLoginBinding.inflate(inflater, container, false)
    }

    override fun prepareView(): Unit = with(binding) {
        applyButton.setOnClickListener {
            viewModel.onButtonNextPageClicked()
        }
        enterLoginInputEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onLoginChanged(text.toString())
        }
    }

    override fun collectState(state: AuthState) = with(binding) {
        enterLoginErrorTv.isVisible = state.errorMessage.isNotEmpty()
        enterLoginErrorTv.text = state.errorMessage
        applyButton.isEnabled = state.isButtonNextPageEnabled //TODO: Желательно проверять что загрузка идет сейчас
    }

    override fun onBeginCreation() {
        viewModel.onEnterLoginScreenShowed()
    }
}