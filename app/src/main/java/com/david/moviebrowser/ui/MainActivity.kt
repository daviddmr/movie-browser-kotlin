package com.david.moviebrowser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.david.moviebrowser.R
import com.david.moviebrowser.databinding.ActivityMainBinding
import com.david.moviebrowser.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = HomeFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_main_container,
                fragment,
                HomeFragment.TAG
            )
            .commit()
    }
}