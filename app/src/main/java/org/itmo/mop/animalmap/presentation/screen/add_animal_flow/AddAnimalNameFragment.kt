package org.itmo.mop.animalmap.presentation.screen.add_animal_flow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.databinding.FragmentAddAnimalNameBinding

@AndroidEntryPoint
class AddAnimalNameFragment : BaseAddAnimalFlowFragment<FragmentAddAnimalNameBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = FragmentAddAnimalNameBinding.inflate(inflater, container, false)
    }

    override fun onBeginCreation() {
        viewModel.onAddAnimalNameShow()
    }

    override fun collectState(state: AddAnimalState) = with(binding) {
        applyButton.isEnabled = state.isButtonNextPageEnabled

    }

    override fun prepareView(): Unit = with(binding) {
        nameFieldEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
        }
        descriptionFieldEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onDescriptionChanged(text.toString())
        }
        applyButton.setOnClickListener {
            viewModel.onButtonNextPageClicked()
        }
    }
}