package org.itmo.mop.animalmap.presentation.screen.add_animal_flow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.presentation.base.BaseFragment

abstract class BaseAddAnimalFlowFragment<V : ViewBinding> :
    BaseFragment<V, AddAnimalState, AddAnimalEvent, AddAnimalViewModel>() {

    private val photoPickerContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val inputStream = requireContext().contentResolver.openInputStream(it)
                if (inputStream != null) viewModel.onPhotoPicked(it, inputStream)
            }
        }

    protected abstract fun onBeginCreation()

    override val viewModel: AddAnimalViewModel by hiltNavGraphViewModels(R.id.add_animal_flow_graph)

    override fun collectEvent(event: AddAnimalEvent) {
        when (event) {
            is AddAnimalEvent.ShowToast -> Toast.makeText(
                requireContext(),
                event.message,
                Toast.LENGTH_SHORT
            ).show()
            is AddAnimalEvent.OpenPhotoPicker -> {
                photoPickerContract.launch("image/*")
            }
            is AddAnimalEvent.ShowNextPage -> {
                findNavController().navigate(event.actionId)
            }
            is AddAnimalEvent.FinishFlow -> {
                Log.e("newMarker", event.marker.toString())
                findNavController().popBackStack(R.id.maps_fragment, false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onBeginCreation()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}