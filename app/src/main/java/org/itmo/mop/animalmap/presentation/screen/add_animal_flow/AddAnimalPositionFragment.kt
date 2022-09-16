package org.itmo.mop.animalmap.presentation.screen.add_animal_flow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.databinding.FragmentAddAnimalPositionBinding
import org.itmo.mop.animalmap.presentation.ext.moveToStartLocation

@AndroidEntryPoint
class AddAnimalPositionFragment : BaseAddAnimalFlowFragment<FragmentAddAnimalPositionBinding>(),
    OnMapReadyCallback {

    private lateinit var gMap: GoogleMap

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = FragmentAddAnimalPositionBinding.inflate(inflater, container, false)
    }

    override fun prepareView() = with(binding) {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.add_animal_map) as SupportMapFragment
        mapFragment.getMapAsync(this@AddAnimalPositionFragment)
        applyButton.setOnClickListener {
            viewModel.onButtonSaveAnimalClicked()
        }
    }

    override fun collectState(state: AddAnimalState): Unit = with(binding) {
        applyButton.isEnabled = state.isButtonNextPageEnabled
        addAnimalMapShimmer.isVisible = state.isLoading
        if (state.isLoading) addAnimalMapShimmer.startShimmer() else addAnimalMapShimmer.stopShimmer()
        state.latLng?.let {
            gMap.clear()
            gMap.addMarker(
                MarkerOptions().apply {
                    title("Ваша метка")
                    position(it)
                }
            )
        }
    }

    override fun onBeginCreation() {
        viewModel.onAddAnimalPositionShow()
    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map
        gMap.moveToStartLocation()
        viewModel.onMapReady()
        gMap.setOnMapClickListener {
            viewModel.onCoordinatesChanged(it)
        }
    }
}