package org.itmo.mop.animalmap.presentation.screen.auth_flow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.databinding.FragmentEnterOtpCodeBinding

@AndroidEntryPoint
class AuthEnterOtpCodeFragment:BaseAuthFlowFragment<FragmentEnterOtpCodeBinding>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentEnterOtpCodeBinding.inflate(inflater, container, false)
    }

    override fun prepareView(): Unit = with(binding) {
        applyButton.setOnClickListener {
            viewModel.onButtonNextPageClicked()
        }
        enterOtpCodeInputEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onOtpCodeChanged(text.toString())
        }
    }

    override fun collectState(state: AuthState) = with(binding) {
        applyButton.isEnabled = state.isButtonNextPageEnabled
    }

    override fun onBeginCreation() {
        viewModel.onEnterOtpCodeScreenShowed()
    }
}