package com.dinadurykina.eventtest.ui.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dinadurykina.eventtest.getOrAwaitValue
import com.dinadurykina.eventtest.util.Event
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class FragmentViewModelAppTest {

    /**
     * Когда вы пишете тесты, включающие тестирование LiveData, используйте это правило!
     * @get:Rule аннотацией, он вызывает InstantTaskExecutorRule -
     * запуск некоторого кода в классе до и после тестов
     * Это правило запускает все фоновые задания, связанные с компонентами архитектуры, в одном потоке,
     * чтобы результаты теста выполнялись синхронно и в повторяющемся порядке
     */
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fragmentViewModelApp: FragmentViewModelApp

    @Before
    fun createFragmentViewModelApp() {
        fragmentViewModelApp = FragmentViewModelApp(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onToast() {
        var toast: Event<String?>
        var rezult: String?

        fragmentViewModelApp.onToast("Test 1")

        toast = fragmentViewModelApp.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNotNull(rezult)
        assertThat(rezult, not(nullValue()))
        assertEquals(rezult,"Test 1")

        toast = fragmentViewModelApp.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNull(rezult)

        toast = fragmentViewModelApp.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNull(rezult)

        fragmentViewModelApp.onToast("Test 1")

        toast = fragmentViewModelApp.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNotNull(rezult)
        assertThat(rezult, not(nullValue()))
        assertEquals(rezult,"Test 1")

        toast = fragmentViewModelApp.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNull(rezult)

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