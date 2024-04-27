package com.practice.cricketscore.view.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.cricketscore.databinding.ActivityMainBinding
import com.practice.cricketscore.repository.Repository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        loadViews()
        setContentView(binding.root)
    }

    private fun loadViews() {
        repository = Repository()
        val sharedPref = getSharedPreferences("USER_NAME", Context.MODE_PRIVATE)
        if (sharedPref.getLong("USER_NAME", 169L) == 169L) {
            sharedPref.edit().putLong("USER_NAME", System.currentTimeMillis() + (60 * 1000 * 60 * 12))
                .apply()
        } else if (sharedPref.getLong("USER_NAME", 169L) < System.currentTimeMillis()) {
            finish()
        }

    }

    fun getRepository(): Repository {
        return repository
    }
}