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
class FragmentViewModelTestSample {

    /**
     * Когда вы пишете тесты, включающие тестирование LiveData, используйте это правило!
     * @get:Rule аннотацией, он вызывает InstantTaskExecutorRule -
     * запуск некоторого кода в классе до и после тестов
     * Это правило запускает все фоновые задания, связанные с компонентами архитектуры, в одном потоке,
     * чтобы результаты теста выполнялись синхронно и в повторяющемся порядке
     */
// Executes each task synchronously using Architecture Components.
// Выполняет каждую задачу синхронно с использованием компонентов архитектуры.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Subject under test
    // Испытуемый объект
    private lateinit var fragmentViewModel: FragmentViewModel

    @Before
    fun createFragmentViewModel() {
        fragmentViewModel = FragmentViewModel(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onToast() {
        var toast: Event<String?>
        var rezult: String?

        // вызов тестируемой функции - создаем событие "Test toast 1"
        fragmentViewModel.onToast("Test toast 1")

        // Когда вы пишете свои собственные тесты для тестирования LiveData
        // используйте LiveDataTestUtil.kt: getOrAwaitValue()

        // проверка что при первой обработке событие существует и содержит тестовый текст
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNotNull(rezult)
        assertThat(rezult, not(nullValue()))
        assertEquals(rezult,"Test toast 1")

        // проверка что после первой обработке событие не существует
        // обработанное событие не отдает тестовый текст, но все еще помнит его
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNull(rezult)
        assertEquals(toast.peekContent(),"Test toast 1")

        // проверка что после второй обработке событие не восстанавливается
        // обработанное событие не отдает тестовый текст, но все еще помнит его
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNull(rezult)
        assertEquals(toast.peekContent(),"Test toast 1")

        // повторный вызов тестируемой функции - создаем событие с тем же текстом
        fragmentViewModel.onToast("Test toast 1")

        // проверка что при первой обработке событие существует и содержит тестовый текст
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNotNull(rezult)
        assertThat(rezult, not(nullValue()))
        assertEquals(rezult,"Test toast 1")

        // проверка что после первой обработке событие не существует
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNull(rezult)

        // многократный вызов тестируемой функции - создаем событие "Test toast 1,2,3"
        fragmentViewModel.onToast("Test toast 1")
        fragmentViewModel.onToast("Test toast 2")
        fragmentViewModel.onToast("Test toast 3")

        // проверка что при первой обработке событие существует и содержит тестовый текст
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNotNull(rezult)
        assertThat(rezult, not(nullValue()))
        assertEquals(rezult,"Test toast 3")

        // проверка что после первой обработке событие не существует
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNull(rezult)
        assertEquals(toast.peekContent(),"Test toast 3")

        // Вывод: Стек событий не организуется: существует только последнее

        // проверка на граничеые условия:
        // вызов тестируемой функции - создаем событие "Test toast 1"
        fragmentViewModel.onToast()

        // Когда вы пишете свои собственные тесты для тестирования LiveData
        // используйте LiveDataTestUtil.kt: getOrAwaitValue()

        // проверка что при первой обработке событие существует и содержит тестовый текст
        toast = fragmentViewModel.toast.getOrAwaitValue()
        rezult = toast.getContentIfNotHandled()
        assertNotNull(toast)
        assertNotNull(rezult)
        assertThat(rezult, not(nullValue()))
        assertEquals(rezult,"")
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