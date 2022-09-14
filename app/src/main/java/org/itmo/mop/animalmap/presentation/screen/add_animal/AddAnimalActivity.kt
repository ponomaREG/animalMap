package org.itmo.mop.animalmap.presentation.screen.add_animal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.databinding.ActivityAddAnimalBinding

@AndroidEntryPoint
class AddAnimalActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAddAnimalBinding

    private lateinit var mMap: GoogleMap

    private val pickPhotoContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val inputStream = contentResolver.openInputStream(it)
                if (inputStream != null) viewModel.onPhotoPicked(it, inputStream)
            }
        }

    private val viewModel: AddAnimalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareView()
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(this@AddAnimalActivity::collectState)
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.event.collect(this@AddAnimalActivity::collectEvent)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        map.setOnMapClickListener { latLng ->
            viewModel.onCoordinatesChanged(latLng)
        }
    }

    private fun prepareView() = with(binding) {

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.animal_google_map) as SupportMapFragment
        mapFragment.getMapAsync(this@AddAnimalActivity)

        addAvatarAnimalBtn.setOnClickListener {
            viewModel.onButtonPickPhotoClicked()
        }
        animalNameEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
        }
        animalDescriptionEt.doOnTextChanged { text, _, _, _ ->
            viewModel.onDescriptionChanged(text.toString())
        }
        animalButtonAdd.setOnClickListener {
            viewModel.onButtonAddClicked()
        }
    }

    private fun collectState(state: AddAnimalState) = with(binding) {
        progressIndicator.isInvisible = !state.isLoading
        animalButtonAdd.isEnabled = state.isButtonAddEnabled
        animalAvatarIv.setImageURI(state.imageUri)
        state.latLng?.let {
            mMap.clear()
            mMap.addMarker(
                MarkerOptions().apply {
                    position(it)
                    title("Ваша метка")
                }
            )
        }
    }

    private fun collectEvent(event: AddAnimalEvent) {
        when (event) {
            is AddAnimalEvent.CloseActivity -> {
                val data = Intent().putExtra(ARGS_NEW_MARKER, event.newMarker)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
            is AddAnimalEvent.ShowToast -> Toast.makeText(this, event.message, Toast.LENGTH_SHORT)
            is AddAnimalEvent.OpenPhotoPicker -> pickPhotoContract.launch("image/*")
        }
    }

    companion object {
        const val ARGS_NEW_MARKER = "ARGS_NEW_MARKER"
    }
}