package com.dinadurykina.eventtest.util

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class EventTestSample {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun eventSampleInt() {
        val testContent = 123
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