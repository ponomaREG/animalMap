package org.itmo.mop.animalmap.presentation.screen.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.itmo.mop.animalmap.databinding.FragmentDetailBinding
import org.itmo.mop.animalmap.presentation.ext.loadByUrl
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BottomSheetDialogFragment() {

    init {
        this.isCancelable = true
    }

    @Inject
    internal lateinit var assistedFactory: DetailViewModel.DetailViewModelAssistedFactory

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.providesFactory(assistedFactory, args.animalId)
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect(this@DetailFragment::collectState)
            }
        }
    }

    private fun collectState(state: DetailState): Unit = with(binding) {
        animalShimmer.isVisible = state.isLoading
        animalContent.isInvisible = state.isLoading
        if (state.isLoading) animalShimmer.startShimmer() else animalShimmer.stopShimmer()
        state.animalInfo?.let {
            if (it.image != null) animalAvatarIv.loadByUrl(it.image)
            animalNameTv.text = it.name
            animalDescriptionTv.text = it.description
        }
    }
}