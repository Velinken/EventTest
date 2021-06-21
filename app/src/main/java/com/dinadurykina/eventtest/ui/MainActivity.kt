package com.dinadurykina.eventtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinadurykina.eventtest.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
    }
}