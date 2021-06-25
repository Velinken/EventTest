package com.dinadurykina.eventtest.ui.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dinadurykina.eventtest.util.Event
/**
 * The [FragmentViewModel] that is attached to the [Fragment].
 * Модель [Fragmentviewмодель], которая прикреплена к [Fragment].
 */
/**
 * При паттерне MVVM в техгологии JetPack это ViewModel, которая создана к [Fragment]:
 *
 * XML берет данные из ViewModel для высветки их на экране (односторонний binding)
 * XML заносит данные в переменные ViewModel и они там используются (двусторонний)
 * XML вызывает функции из ViewModel, а они возвращают XML результат для высветки
 * ........
 * ViewModel не должна обращаться к XML - ничего оттуда не читать и не заносить
 * ViewModel не должно обращаться к Fragment - ничего оттуда не читать и не заносить
 * ........
 * ViewModel может общаться с адаптерами
 * ViewModel и только она может обращаться к репозиторию
 * ViewModel может общаться с утилитами
 *
 * правильная ViewModel спасает при разрушения фрагмента от поворота смартфона
 */
class FragmentViewModel : ViewModel() {

    // стандартное объявление Trigger что надо высветить TOAST
    private val _toast =  MutableLiveData<Event<String?>>()
    val toast: LiveData<Event<String?>>
        get() = _toast

    // при нажатии на кнопку TOAST из XML вызывается эта функция:
    fun onToast(text:String){
        // Поднимается Trigger toast - ему присваивается новое событие - Trigger взведен
        // т.е. создается новое событие и присваивается флажку toast отчего он изменяется
        // а этот Trigger слушается из фрагмента observeEvent-ом на изменение
        // toast.observeEvent увидев изменение засвечивает TOAST из фрагмента, что и требуется
        _toast.value = Event(text)
        // это все стандартная система флажков, a особенность EVENT не в этом -
        // ПОСЛЕ СРАБОТКИ Trigger ЕГО НЕ НАДО ОПУСКАТЬ - это произведется автоматически
        // т.е. не нужна функция опускания Trigger и ее вызов из фрагмента
        // Другими словами Event + observeEvent фактически это SingleEventObserver
        // второй раз наблюдатель сработать на флажке не может - Trigger уже опущен
    }

    // Объявление флажка что надо высветить keyBoard
    private val _keyBoard =  MutableLiveData<Event<Boolean?>>()
    val keyBoard: LiveData<Event<Boolean?>>
        get() = _keyBoard

    fun keybooardON(){
        _keyBoard.value = Event(true)
    }

    fun keybooardOFF(){
        _keyBoard.value = Event(false)
    }

    // Объявление Trigger что надо высветить Snackbar
    private val _snackbar =  MutableLiveData<Event<String?>>()
    val snackbar: LiveData<Event<String?>>
        get() = _snackbar

    // при нажатии на кнопку Snackbar из XML вызывается эта функция:
    fun onSnackbar(){
        _snackbar.value = Event("Snackbar text")
    }
}