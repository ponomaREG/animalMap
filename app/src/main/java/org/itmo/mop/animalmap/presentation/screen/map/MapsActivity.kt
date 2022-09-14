package org.itmo.mop.animalmap.presentation.screen.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.databinding.ActivityMapsBinding
import org.itmo.mop.animalmap.domain.model.AnimalCoordinate
import org.itmo.mop.animalmap.presentation.screen.add_animal.AddAnimalActivity
import org.itmo.mop.animalmap.presentation.screen.detail.DetailActivity

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var binding: ActivityMapsBinding

    private val viewModel: MapsViewModel by viewModels()

    private val addAnimalActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val newMarker: AnimalCoordinate? =
                    it.data?.getParcelableExtra(AddAnimalActivity.ARGS_NEW_MARKER)
                if (newMarker != null) {
                    viewModel.onNewAnimalAdded(newMarker)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(this@MapsActivity::collectState)
            }
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.event.collect(this@MapsActivity::collectEvent)
            }
        }
        binding.addAnimalFab.setOnClickListener {
            viewModel.onAddAnimalClicked()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val saintPetersburg = LatLng(59.937500, 30.308611)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(saintPetersburg))
        viewModel.onMapReady()
        mMap?.setOnInfoWindowClickListener {
            viewModel.onMarkerClicked(it.tag as Int)
        }
    }

    override fun onStart() {
        super.onStart()
        if (mMap != null) viewModel.onStartAndMapReady()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    private fun collectState(state: MapsViewModelState) = with(binding) {
        progressIndicator.isVisible = state.isLoading
        state.coordinates.forEach { animalCoordinate ->
            mMap?.addMarker(
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

    private fun collectEvent(event: MapsEvent) {
        when (event) {
            is MapsEvent.ShowDetailActivity -> {
                DetailActivity.startDetailActivity(this, event.animalId)
            }
            MapsEvent.ShoAddAnimalActivity -> {
                addAnimalActivityResult.launch(Intent(this, AddAnimalActivity::class.java))
            }
        }
    }
}