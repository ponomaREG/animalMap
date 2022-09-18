package org.itmo.mop.animalmap.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

abstract class BaseFragment<B : ViewBinding, S, E : Event, V : BaseViewModel<S, E>> : Fragment() {

    protected var _binding: B? = null
    protected val binding: B
        get() = _binding!!

    protected abstract val viewModel: V

    protected abstract fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    )

    protected abstract fun prepareView()

    protected abstract fun collectState(state: S)

    protected abstract fun collectEvent(event: E)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state
                    .distinctUntilChanged { old, new ->
                        old == new
                    }
                    .collect(this@BaseFragment::collectState)
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.event.collect(this@BaseFragment::collectEvent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}