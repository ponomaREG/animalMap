package org.itmo.mop.animalmap.presentation.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.itmo.mop.animalmap.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}