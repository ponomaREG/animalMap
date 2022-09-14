package org.itmo.mop.animalmap.presentation.screen.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.databinding.ActivityDetailBinding
import org.itmo.mop.animalmap.presentation.ext.loadByUrl
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @Inject
    internal lateinit var assistedFactory: DetailViewModel.DetailViewModelAssistedFactory

    private val viewModel: DetailViewModel by viewModels {
        val animalId = intent.getIntExtra(ARGS_ANIMAL_ID, -1)
        if (animalId == -1) error("")
        DetailViewModel.providesFactory(assistedFactory, animalId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(this@DetailActivity::collectState)
            }
        }
    }

    private fun collectState(state: DetailState) = with(binding) {
        progressIndicator.isVisible = state.isLoading
        state.animalInfo?.let {
            Log.e("Animal Info", it.toString())
            if (it.image != null) animalAvatarIv.loadByUrl(it.image)
            animalNameTv.text = it.name
            animalDescriptionTv.text = it.description
        }
    }

    companion object {
        private const val ARGS_ANIMAL_ID = "ARGS_ANIMAL_ID"

        fun startDetailActivity(context: Context, animalId: Int) {
            context.startActivity(
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(ARGS_ANIMAL_ID, animalId)
                }
            )
        }
    }
}