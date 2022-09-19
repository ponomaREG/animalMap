package org.itmo.mop.animalmap.presentation.screen.auth_flow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.databinding.FragmentEnterPhoneBinding

@AndroidEntryPoint
class AuthEnterPhoneFragment: BaseAuthFlowFragment<FragmentEnterPhoneBinding>() {

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
    }

    override fun prepareView(): Unit = with(binding) {
        applyButton.setOnClickListener {
            viewModel.onButtonNextPageClicked()
        }
        enterPhoneInputEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onPhoneChanged(text.toString())
        }
    }

    override fun collectState(state: AuthState) = with(binding) {
        applyButton.isEnabled = state.isButtonNextPageEnabled
    }

    override fun onBeginCreation() {
        viewModel.onEnterPhoneScreenShowed()
    }
}