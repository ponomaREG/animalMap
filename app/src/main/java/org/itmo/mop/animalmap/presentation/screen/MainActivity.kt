package org.itmo.mop.animalmap.presentation.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.MainGraphDirections
import org.itmo.mop.animalmap.R
import org.itmo.mop.animalmap.data.persistence.TokenPersistence
import org.itmo.mop.animalmap.databinding.FragmentMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private var isFirstOpened: Boolean = false

    @Inject
    internal lateinit var tokenPersistence: TokenPersistence //TODO: Желательно вынести

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        if(tokenPersistence.isTokenExists() && !isFirstOpened) {
            findNavController(R.id.nav_host_fragment).navigate(MainGraphDirections.toRootGraph())
        }
        isFirstOpened = true
    }
}