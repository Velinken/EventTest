package com.dinadurykina.eventtest.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventTestUnit {
    private val _flag =  MutableLiveData<Event<String?>>()
    val flag: LiveData<Event<String?>>
        get() = _flag
    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
    }

    @Test
    fun eventUnitInit() {
        val rezult = flag
        _flag.value = Event("Red")
        assertEquals(rezult.value?.getContentIfNotHandled(),"Red")
        assertNull(rezult.value?.getContentIfNotHandled())
        _flag.value = Event("Wight")
        assertEquals(rezult.value?.getContentIfNotHandled(),"Wight")
        assertNull(rezult.value?.getContentIfNotHandled())
        assertEquals(rezult.value?.peekContent(),"Wight")
    }
    @Test
    fun eventSampleInt() {
        val rezult = flag
        _flag.value = Event("Red")

        class AboutFragment : Fragment() {
            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                rezult.observeEvent(viewLifecycleOwner) {
                    assertEquals(it, "Red")
                }
                rezult.observe(viewLifecycleOwner, EventObserver {
                    assertEquals(it, "Red")
                })
                return requireView()
            }
            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                 _flag.value = Event("Wight")
            }
        }
        val sky = AboutFragment()

      /*  val testContent = 123
        val event = Event(testContent)
        assertEquals(event.hasBeenHandled,false)
        assertEquals(event.peekContent(),testContent)

        val peekFirst = event.getContentIfNotHandled()
        assertEquals(peekFirst, testContent)
        assertEquals(peekFirst, 123)
        assertEquals(event.hasBeenHandled,true)
        assertEquals(event.peekContent(),testContent)

        val peekSecond = event.getContentIfNotHandled()
        assertEquals(peekSecond, null)
        assertNotEquals(peekSecond, 123)
        assertEquals(event.hasBeenHandled,true)
        assertEquals(event.peekContent(),testContent)

       */
    }

    @Test
    fun eventSampleString() {
        val testContent = "apple"
        val event = Event(testContent)
        assertEquals(event.hasBeenHandled,false)
        assertEquals(event.peekContent(),testContent)

        val peekFirst = event.getContentIfNotHandled()
        assertEquals(peekFirst, testContent)
        assertEquals(event.hasBeenHandled,true)
        assertEquals(event.peekContent(),testContent)

        val peekSecond = event.getContentIfNotHandled()
        assertEquals(peekSecond, null)
        assertEquals(event.hasBeenHandled,true)
        assertEquals(event.peekContent(),testContent)
    }

    @Test
    fun eventSampleNull() {
        val testContent = null
        val event = Event(testContent)
        assertEquals(event.hasBeenHandled,false)
        assertNull(event.peekContent())

        val peekFirst = event.getContentIfNotHandled()
        assertNull(peekFirst)
        assertEquals(event.hasBeenHandled,true)
        assertNull(event.peekContent())

        val peekSecond = event.getContentIfNotHandled()
        assertNull(peekSecond)
        assertEquals(event.hasBeenHandled,true)
        assertNull(event.peekContent())

    }

    @Test
    fun eventSampleListInt() {
        val testContent = arrayListOf(1, 2, 3, 4, 5)
        val event = Event(testContent)
        assertEquals(event.hasBeenHandled,false)
        assertEquals(event.peekContent(),testContent)

        val peekFirst = event.getContentIfNotHandled()
        assertEquals(peekFirst, testContent)
        assertEquals(peekFirst?.get(3), 4)
        assertEquals(event.hasBeenHandled,true)
        assertEquals(event.peekContent(),testContent)

        val peekSecond = event.getContentIfNotHandled()
        assertEquals(peekSecond, null)
        assertNotEquals(peekSecond, 123)
        assertEquals(event.hasBeenHandled,true)
        assertEquals(event.peekContent(),testContent)
    }
    // Хорошо бы передать класс, например data/enum ...
    // Сложные типы составные: лист классов, класс листов
    // Лямбду, функцию, надо подумать что из Котлина

    // Передать событию в качестве контента другое событие
    // На этом закончить и перейти EventObserverUnit
}