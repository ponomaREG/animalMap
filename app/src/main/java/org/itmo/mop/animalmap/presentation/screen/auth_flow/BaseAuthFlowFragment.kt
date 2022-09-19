package org.itmo.mop.animalmap.presentation.screen.auth_flow

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
import org.itmo.mop.animalmap.MainGraphDirections
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.presentation.base.BaseFragment

abstract class BaseAuthFlowFragment<V : ViewBinding> :
    BaseFragment<V, AuthState, AuthEvent, AuthViewModel>() {

    protected abstract fun onBeginCreation()

    override val viewModel: AuthViewModel by hiltNavGraphViewModels(R.id.auth_graph)

    override fun collectEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.ShowToast -> Toast.makeText(
                requireContext(),
                event.message,
                Toast.LENGTH_SHORT
            ).show()
            is AuthEvent.ShowNextPage -> {
                findNavController().navigate(event.actionId)
            }
            AuthEvent.GoToMainGraph -> findNavController().navigate(MainGraphDirections.toRootGraph())
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