package com.example.alarmmaps.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.alarmmaps.databinding.ActivityMainBinding
import com.example.alarmmaps.presentation.fragments.AlarmListFragment
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw Exception("ActivityMainBinding = null")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            val alarmListFragment = AlarmListFragment()
            supportFragmentManager.commit {
                add(binding.fragmentContainerView.id, alarmListFragment)
            }
        }
    }

    fun launchFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            addToBackStack(null)
            replace(binding.fragmentContainerView.id, fragment)
        }
    }

    companion object {
        const val MAPKIT_API_KEY = "10b4723f-3f09-4f4a-a0b0-c97a82626849"


    }
}