package com.practice.cricketscore.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practice.cricketscore.databinding.ActivityMainBinding
import com.practice.cricketscore.localData.LocalData
import com.practice.cricketscore.repository.Repository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository : Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        loadViews()
        setContentView(binding.root)
    }

    private fun loadViews() {
        LocalData.getInstance(applicationContext)
        repository = Repository()
    }

    fun getRepository(): Repository {
        return repository
    }
}