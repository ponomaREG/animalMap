package org.itmo.mop.animalmap.presentation.screen.add_animal_flow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.databinding.FragmentAddAnimalPhotoBinding

@AndroidEntryPoint
class AddAnimalPhotoFragment : BaseAddAnimalFlowFragment<FragmentAddAnimalPhotoBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = FragmentAddAnimalPhotoBinding.inflate(inflater, container, false)
    }

    override fun prepareView() = with(binding) {
        animalPhotoPlaceholder.setOnClickListener {
            viewModel.onButtonPickPhotoClicked()
        }
        applyButton.setOnClickListener {
            viewModel.onButtonNextPageClicked()
        }
        skipButton.setOnClickListener {
            viewModel.onButtonSkipClicked()
        }
    }

    override fun collectState(state: AddAnimalState): Unit = with(binding) {
        applyButton.isEnabled = state.isButtonNextPageEnabled
        animalPhotoPlaceholder.isVisible = state.imageUri == null
        state.imageUri?.let {
            animalPhotoIv.setImageURI(it)
        }
    }

    override fun onBeginCreation() {
        viewModel.onAddAnimalPhotoShow()
    }
}