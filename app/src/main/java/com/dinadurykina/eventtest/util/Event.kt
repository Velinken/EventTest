/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dinadurykina.eventtest.util

// https://gist.github.com/JoseAlcerreca/e0bba240d9b3cffa258777f12e5c0ae9
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

open class Event<out T>(private val content: T? = null) {
    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set
    //  Returns the content and prevents its use again.
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) null  else content.also { hasBeenHandled = true }
    //  Returns the content, even if it's already been handled.
  fun peekContent(): T? = content
}

inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner) { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) }
}

// Event     https://gist.github.com/52e326bc01a8cfaf5e410f239cd801db
// ViewModel https://gist.github.com/Dzendo/ef2167d1572f5fb6af95d6700c879cce
// Fragment  https://gist.github.com/Dzendo/0eb95f9a8004ddd7a1ab98755a1e6d38

/**
 * Организация Событий в Blueprint архитектуре Jetpack
 *
 * Organizing Events in the Blueprint architecture of Jetpack
 *
 * Изучив эту замечательную статью и связанные публикации
 * Для организации единичного события из ViewModel в Fragment
 * Сделал следующим образом:
 *
 * Есть ли решение короче и правильнее?
 *
 * After studying this wonderful article and related publications
 * Organizing a single event from a view model to a fragment
 * Did the following:
 *
 * Is there a shorter and more correct solution?
 *
 *
 *
 */
/*
* After studying this wonderful article and related publications

 * Organizing a single event from a view model to a fragment

 * Did the following:

Event     https://gist.github.com/52e326bc01a8cfaf5e410f239cd801db

ViewModel https://gist.github.com/Dzendo/ef2167d1572f5fb6af95d6700c879cce

Fragment  https://gist.github.com/Dzendo/0eb95f9a8004ddd7a1ab98755a1e6d38

 * Is there a shorter and more correct solution?


 */

// https://progi.pro/kak-sozdat-livedata-kotoriy-ispuskaet-odno-sobitie-i-uvedomlyaet-tolko-poslednego-zaregistrirovannogo-nablyudatelya-9647541
// https://gist.github.com/JoseAlcerreca/e0bba240d9b3cffa258777f12e5c0ae9

/*
inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner, Observer { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) })
}
*/
/* ЗАМЕНЕН НА INLINE ИЗ COMMIT см выше Hyunwoo Park  eyeahs
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { onEventUnhandledContent(it) }
    }
}*/
// Удалить из GooglePlay
/* использовать оболочку событий см выше open class Even
class ListViewModel : ViewModel {
    private val _navigateToDetails = MutableLiveData<Event<String>>()
    val navigateToDetails : LiveData<Event<String>>
        get() = _navigateToDetails


    fun userClicksOnButton(itemId: String) {
        _navigateToDetails.value = Event(itemId)
         // Trigger the event by setting a new Event as a new value
         // Запустить   событие, установив новое событие как новое значение
    }
}
// в фрагменте
myViewModel.navigateToDetails.observe(this, Observer {
    it.getContentIfNotHandled()?.let {
    // Only proceed if the event has never been handled
    // Продолжаем, только если событие никогда не обрабатывалось
        startActivity(DetailsActivity...)
    }
})

        viewModel.openTaskEvent.observe(viewLifecycleOwner, EventObserver {
            openTaskDetails(it)
        })
        viewModel.newTaskEvent.observe(viewLifecycleOwner, EventObserver {
            navigateToAddNewTask()
        })

Преимущество этого подхода состоит в том, что пользователю необходимо указать намерение,
 используя getContentIfNotHandled() или peekContent().
  Этот метод моделирует события как часть состояния:
   теперь они просто сообщение, которое было использовано или нет.

 */

/* ОК: использовать SingleLiveEvent он ограничен одним наблюдателем
class ListViewModel : ViewModel {
    private val _navigateToDetails = SingleLiveEvent<Any>()
    val navigateToDetails : LiveData<Any>
        get() = _navigateToDetails

    fun userClicksOnButton() {
        _navigateToDetails.call()
    }
}
// в фрагменте:
myViewModel.navigateToDetails.observe ( this , Observer {
    startActivity ( DetailsActivity ...)
})
 */

/*
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}
 */
// https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055

/*
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * Используется в качестве оболочки для данных, которые отображаются через живые данные, представляющие событие.
 *
 * Что такое событие?
 * В приложении  вы используете настраиваемый Event класс
 *  для LiveData представления одноразовых событий (таких как навигация или всплывающая закусочная).
 *  Event LiveData Наблюдается в TasksFragment.
 * В этом случае символ newTaskEvent означает, что была нажата клавиша FAB с плюсом, и вы должны перейти к AddEditTaskFragment.
 *  Вы можете узнать больше о событиях
 *  https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 *   private val _navigateToDetails = SingleLiveEvent<Any>()
 *  https://codelabs.developers.google.com/codelabs/kotlin-android-training-live-data/index.html?index=..%2F..android-kotlin-fundamentals
 * Пока не встроена
 */

open class Event<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write Разрешить внешнее чтение, но не запись

    /**
     * Returns the content and prevents its use again.
     * Возвращает содержимое и предотвращает его повторное использование.
     * IfNotHandled Если Не Обработано
     */
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) null  else content.also { hasBeenHandled = true }

    /**
     * Returns the content, even if it's already been handled.
     * Возвращает содержимое, даже если оно уже было обработано.
     */
    // заглядывать в контент
    fun peekContent(): T = content
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has already been handled.
 * [Наблюдатель] для [события]s, упрощающий шаблон проверки того, было ли содержимое [события] уже обработано.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 * [on Event Unhandled Content] вызывается *только* в том случае, если содержимое [события] не было обработано.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}
 */