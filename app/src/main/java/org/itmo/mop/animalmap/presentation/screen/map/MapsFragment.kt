package org.itmo.mop.animalmap.presentation.screen.map

import android.Manifest
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.databinding.FragmentMapsBinding
import org.itmo.mop.animalmap.presentation.base.BaseFragment
import org.itmo.mop.animalmap.presentation.ext.moveToStartLocation

@AndroidEntryPoint
class MapsFragment :
    BaseFragment<FragmentMapsBinding, MapsViewModelState, MapsEvent, MapsViewModel>(),
    OnMapReadyCallback {

    private var gMap: GoogleMap? = null

    override val viewModel: MapsViewModel by viewModels()

    private val locationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            gMap?.uiSettings?.isMyLocationButtonEnabled = isGranted
            gMap?.uiSettings?.isMapToolbarEnabled = isGranted
            gMap?.isMyLocationEnabled = isGranted
        }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (gMap != null) viewModel.onStartAndMapReady()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun prepareView() {
        val fragmentMap = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        fragmentMap.getMapAsync(this)
        binding.addAnimalFab.setOnClickListener {
            viewModel.onAddAnimalClicked()
        }
    }

    override fun collectState(state: MapsViewModelState) = with(binding) {
        loadingIndicator.root.isVisible = state.isLoading
        contentGroup.isInvisible = state.isLoading
        state.coordinates.forEach { animalCoordinate ->
            gMap?.addMarker(
                MarkerOptions().apply {
                    position(
                        LatLng(
                            animalCoordinate.latitude.toDouble(),
                            animalCoordinate.longitude.toDouble()
                        )
                    )
                    title(animalCoordinate.name)
                }
            ).also { marker ->
                marker?.let {
                    it.tag = animalCoordinate.id
                }
            }
        }
    }

    override fun collectEvent(event: MapsEvent) {
        when (event) {
            is MapsEvent.ShowDetailFragment -> {
                findNavController().navigate(
                    MapsFragmentDirections.actionMapsFragmentToDetailFragment(
                        event.animalId
                    )
                )
            }
            MapsEvent.ShoAddAnimalFragment -> {
                findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToAddFlowName())
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        gMap = map
        gMap?.moveToStartLocation()
        viewModel.onMapReady()
        locationPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        gMap?.setOnInfoWindowClickListener {
            viewModel.onMarkerClicked(it.tag as Int)
        }
    }
}