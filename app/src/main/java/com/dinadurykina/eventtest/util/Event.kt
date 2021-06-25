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

// https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
// этот класс имеет смысл разместить в подпакет util или utils
// build.gradle(Module): dependencies.implementation 'androidx.fragment:fragment-ktx:1.3.5'
// Дальнейшее развитие со штатной lifecycle:lifecycle-runtime-ktx:2.4.0-alpha01
// https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055
// https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * Используется в качестве оболочки для данных, которые отображаются через живые данные, представляющие событие.
 * * Что такое событие?
 * В приложении  вы используете настраиваемый Event класс
 *  для LiveData представления одноразовых событий (таких как навигация или всплывающая закусочная).
 *  Event LiveData Наблюдается в Fragment.
 */

open class Event<out T>(private val content: T? = null) {
    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set
    // Returns the content and prevents its use again.
    // Возвращает содержимое и предотвращает его повторное использование.
    fun getContentIfNotHandled(): T? = if (hasBeenHandled) null  else content.also { hasBeenHandled = true }
    // Returns the content, even if it's already been handled.
    // Возвращает содержимое, даже если оно уже обработано.
  fun peekContent(): T? = content
}

/**
 * An [observeEvent] for [Event]s, simplifying the pattern of checking if the [Event]'s content has already been handled.
 * [Наблюдатель] для [события]s, упрощающий шаблон проверки того, было ли содержимое [события] уже обработано.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been handled.
 * [on Event Unhandled Content] вызывается *только* в том случае, если содержимое [события] не было обработано.
 * про жту функцию здесь: https://gist.github.com/JoseAlcerreca/e0bba240d9b3cffa258777f12e5c0ae9
 */
// этe функцию разместить здесь же или прямо в файле фрагмента
inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner) { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) }
}
// альтернативный вариант более понятный и длинный (можно использовать или fun см выше или class см ниже)
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

// https://progi.pro/kak-sozdat-livedata-kotoriy-ispuskaet-odno-sobitie-i-uvedomlyaet-tolko-poslednego-zaregistrirovannogo-nablyudatelya-9647541
// https://proandroiddev.com/android-singleliveevent-redux-with-kotlin-flow-b755c70bb055