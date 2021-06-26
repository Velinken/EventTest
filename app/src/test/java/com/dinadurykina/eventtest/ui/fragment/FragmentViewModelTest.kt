package com.dinadurykina.eventtest.ui.fragment

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
//@Config(manifest= Config.NONE)
class FragmentViewModelTest {
    private lateinit var fragmentViewModel: FragmentViewModel
    @Before
    fun setUpFragmentViewModel() {
        fragmentViewModel = FragmentViewModel()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onToast() {
        fragmentViewModel.onToast("Test 1")

    }

    @Test
    fun keybooardON() {
    }

    @Test
    fun keybooardOFF() {
    }

    @Test
    fun onSnackbar() {
    }

    @Test
    fun onNotify() {
    }
}