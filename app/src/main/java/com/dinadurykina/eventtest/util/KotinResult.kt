@file:Suppress("UNCHECKED_CAST", "RedundantVisibilityModifier")
package com.dinadurykina.eventtest.util
//package kotlin
// API:   https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/
// ENG:   https://gist.github.com/DinaDurykina/7ace19e138370c6a9b8ddf54f9d542c4
// Eng/RUS:   https://gist.github.com/DinaDurykina/4167a080d6d09c3ac5da1869ece421e2
// https://habr.com/ru/post/545926/

// 05.07.2021 https://t.me/kotlin_start/49848

// https://github.com/Kotlin/KEEP/blob/master/proposals/stdlib/result.md
// https://github.com/Kotlin/KEEP/blob/master/proposals/stdlib/result.md
/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 * Авторские права 2010-2018 JetBrains s.r.o. и авторы языка программирования Kotlin.
 * Использование этого исходного кода регулируется лицензией Apache 2.0, которую можно найти в license/LICENSE.txt файл.
 */

/**
 * Функции имеют понятные и понятные имена, которые следуют устоявшейся традиции стандартной библиотеки Kotlin
 * и устанавливают следующие дополнительные соглашения:
 *
 * Функции, которые могут генерировать ранее подавленное (захваченное) исключение,
 * имеют явный OrThrow суффикс, например getOrThrow.
 * Функции, которые захватывают выброшенное исключение и инкапсулируют его в Result экземпляр,
 * имеют явный Catching суффикс, например runCatching и mapCatching.
 * Традиционная map функция преобразования, которая работает с успешными случаями,
 * дополняется recover функцией, которая аналогичным образом преобразует исключительные случаи.
 * Внутри либо отказ map или recover превратить операцию Прерывает как традиционные функции,
 * но mapCatching и recoverCatchingEncapsulate неудача в преобразовании в результирующий Result.
 * Функции для запроса кейса имеют естественные имена isSuccess и isFailure.
 * Функции, которые действуют в случаях успеха или неудачи,
 * получают имена onSuccess и onFailure возвращают свой получатель
 * без изменений для дальнейшего связывания в соответствии с традицией,
 * установленной onEach расширением из Стандартной библиотеки.
 * Строковое представление Result значения ( toString) - это либо, Success(v)либо Failure(x)
 * где v и x являются строковыми представлениями соответствующего значения и исключения.
 * equals и hashCode реализуются естественным образом для типа результата,
 * сравнивая соответствующие значения или исключения.
 */



import kotlin.contracts.*
//import kotlin.internal.InlineOnly
import kotlin.jvm.JvmField
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName

/**
 * A discriminated union that encapsulates a successful outcome with a value of type [T]
 * or a failure with an arbitrary [Throwable] exception.
 * Дискриминируемое объединение, которое инкапсулирует успешный результат со значением типа [T]
 * или сбой с произвольным исключением [Throwable].
 * https://dev.to/mahendranv/kotlin-value-class-new-kid-in-town-3p9h
 */
@ExperimentalContracts
@SinceKotlin("1.3")
@JvmInline
public value class Result<out T> @PublishedApi internal constructor(
    @PublishedApi
    internal val value: Any?
) : java.io.Serializable {    // Serializable
    // discovery открытие

    /**
     * Returns `true` if this instance represents a successful outcome.
     * In this case [isFailure] returns `false`.
     * Возвращает `true", если этот экземпляр представляет собой успешный результат.
     * В этом случае [isFailure] возвращает `false`.
     */
    public val isSuccess: Boolean get() = value !is Failure

    /**
     * Returns `true` if this instance represents a failed outcome.
     * In this case [isSuccess] returns `false`.
     * Возвращает `true", если этот экземпляр представляет собой неудачный результат.
     * В этом случае [isSuccess] возвращает `false`.
     */
    public val isFailure: Boolean get() = value is Failure

    // value & exception retrieval - поиск значений и исключений

    /**
     * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or `null`
     * if it is [failure][Result.isFailure].
     * Возвращает инкапсулированное значение, если этот экземпляр представляет [success][Result.isSuccess] или `null`
     * если это [сбой][Result.isFailure].
     *
     * This function is a shorthand for `getOrElse { null }` (see [getOrElse]) or
     * `fold(onSuccess = { it }, onFailure = { null })` (see [fold]).
     * Эта функция является сокращением для "getOrElse { null}" (см. [getOrElse]) или
     * `fold(onSuccess = { it }, OnFailure = { null }) ' (см. [fold]).
     */
    //@InlineOnly
    public inline fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> value as T
        }

    /**
     * Returns the encapsulated [Throwable] exception if this instance represents [failure][isFailure] or `null`
     * if it is [success][isSuccess].
     *  Возвращает инкапсулированное исключение [Throwable], если этот экземпляр представляет собой [failure][isFailure] или "null".
     * если это [успех][isSuccess].
     *
     * This function is a shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
     * Эта функция является сокращение для "fold(onSuccess = { null }, OnFailure = { it})" (см. [fold]).
     */
    public fun exceptionOrNull(): Throwable? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    /**
     * Returns a string `Success(v)` if this instance represents [success][Result.isSuccess]
     * where `v` is a string representation of the value or a string `Failure(x)` if
     * it is [failure][isFailure] where `x` is a string representation of the exception.
     * Возвращает строку " Success(v)", если этот экземпляр представляет [success][Result.isSuccess]
     * где " v "- строковое представление значения или строка " Failure(x)", если
     * это [failure][isFailure], где " x " - строковое представление исключения.
     */
    public override fun toString(): String =
        when (value) {
            is Failure -> value.toString() // "Failure($exception)"
            else -> "Success($value)"
        }

    // companion with constructors - компаньон с конструкторами

    /**
     * Companion object for [Result] class that contains its constructor functions
     * [success] and [failure].
     *  Сопутствующий объект для класса [Result], который содержит его функции конструктора
     * [успех] и [неудача].
     */
    public companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         * Возвращает экземпляр, который инкапсулирует данное [значение] в качестве успешного значения.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        //@InlineOnly
        @JvmName("success")
        public inline fun <T> success(value: T): Result<T> =
            Result(value)

        /**
         * Returns an instance that encapsulates the given [Throwable] [exception] as failure.
         * Возвращает экземпляр, который инкапсулирует данное [Throwable] [исключение] как сбой.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        //@InlineOnly
        @JvmName("failure")
        public inline fun <T> failure(exception: Throwable): Result<T> =
            Result(createFailure(exception))
    }

    internal class Failure(
        @JvmField
        val exception: Throwable
    ) : java.io.Serializable {  // Serializable
        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Failure($exception)"
    }
}

/**
 * Creates an instance of internal marker [Result.Failure] class to
 * make sure that this class is not exposed in ABI.
 * Создает экземпляр внутреннего маркера [Результат.Failure] class to
 * убедитесь, что этот класс не отображается в ABI.
 */
@ExperimentalContracts
@PublishedApi
@SinceKotlin("1.3")
internal fun createFailure(exception: Throwable): Any =
    Result.Failure(exception)

/**
 * Throws exception if the result is failure. This internal function minimizes
 * inlined bytecode for [getOrThrow] and makes sure that in the future we can
 * add some exception-augmenting logic here (if needed).
 * Создает исключение, если результатом является сбой. Эта внутренняя функция сводит к минимуму
 * встроенный байт-код для [getorthow] и гарантирует, что в будущем мы сможем
 * добавьте сюда некоторую логику увеличения исключений (если это необходимо).
 */
@ExperimentalContracts
@PublishedApi
@SinceKotlin("1.3")
internal fun Result<*>.throwOnFailure() {
    if (value is Result.Failure) throw value.exception
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 * Вызывает указанную функцию [блок] и возвращает ее инкапсулированный результат, если вызов был успешным.,
 * перехват любого исключения [Throwable] , которое было вызвано выполнением функции [block], и инкапсуляция его как сбоя.
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R> runCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 * Вызывает указанную функцию [блок] со значением "this" в качестве получателя и возвращает ее инкапсулированный результат, если вызов был успешным.,
 * перехват любого исключения [Throwable] , которое было вызвано выполнением функции [block], и инкапсуляция его как сбоя.
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <T, R> T.runCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

// -- extensions ---
// -- расширения ---

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or throws the encapsulated [Throwable] exception
 * if it is [failure][Result.isFailure].
 * Возвращает инкапсулированное значение, если этот экземпляр представляет [успех][Result.is Успех] или вызывает инкапсулированное исключение [Throwable]
 * если это [сбой][Result.is Неудача].
 *
 * This function is a shorthand for `getOrElse { throw it }` (see [getOrElse]).
 * Эта функция является сокращением для "getOrElse { throw it}" (см. [getOrElse]).
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <T> Result<T>.getOrThrow(): T {
    throwOnFailure()
    return value as T
}

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or the
 * result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 * Возвращает инкапсулированное значение, если этот экземпляр представляет [success][Result.isSuccess] или
 * результат функции [OnFailure] для инкапсулированного исключения [Throwable], если это [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onFailure] function.
 * Обратите внимание, что эта функция переосмысливает любое исключение [Throwable], вызванное функцией [OnFailure].
 *
 * This function is a shorthand for `fold(onSuccess = { it }, onFailure = onFailure)` (see [fold]).
 * Эта функция является сокращением для "fold(onSuccess = { it }, OnFailure = OnFailure)" (см. [fold]).
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R, T : R> Result<T>.getOrElse(onFailure: (exception: Throwable) -> R): R {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> value as T
        else -> onFailure(exception)
    }
}

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or the
 * [defaultValue] if it is [failure][Result.isFailure].
 * Возвращает инкапсулированное значение, если этот экземпляр представляет [success][Result.isSuccess] или
 * [Значение по умолчанию], если это [сбой][Результат.isFailure].
 *
 * This function is a shorthand for `getOrElse { defaultValue }` (see [getOrElse]).
 * Эта функция является сокращением для "getOrElse { defaultValue}" (см. [getOrElse]).
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R {
    if (isFailure) return defaultValue
    return value as T
}

/**
 * Returns the result of [onSuccess] for the encapsulated value if this instance represents [success][Result.isSuccess]
 * or the result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 * Возвращает результат [onSuccess] для инкапсулированного значения, если этот экземпляр представляет [success][Result.isSuccess]
 * или результат функции [OnFailure] для инкапсулированного исключения [Throwable], если это [сбой][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onSuccess] or by [onFailure] function.
 * Обратите внимание, что эта функция переосмысливает любое исключение [Throwable], вызванное функцией [onSuccess] или функцией [OnFailure].
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> onSuccess(value as T)
        else -> onFailure(exception)
    }
}

// transformation
// трансформация

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][Result.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 * Возвращает инкапсулированный результат данной функции [transform], примененной к инкапсулированному значению
 * если этот экземпляр представляет [success][Result.isSuccess] или
 * исходное инкапсулированное исключение [Throwable], если это [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
 * See [mapCatching] for an alternative that encapsulates exceptions.
 * Обратите внимание, что эта функция переосмысливает любое исключение [Throwable], вызванное функцией [transform].
 * Альтернативу, инкапсулирующую исключения, см. в разделе [mapCatching].
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R, T> Result<T>.map(transform: (value: T) -> R): Result<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when {
        isSuccess -> Result.success(transform(value as T))
        else -> Result(value)
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][Result.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 * Возвращает инкапсулированный результат данной функции [transform], примененной к инкапсулированному значению
 * если этот экземпляр представляет [success][Result.isSuccess] или
 * исходное инкапсулированное исключение [Throwable], если это [failure][Result.isFailure].
 *
 * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
 * Эта функция улавливает любое исключение [Throwable] , вызванное функцией [transform], и инкапсулирует его как сбой.
 * See [map] for an alternative that rethrows exceptions from `transform` function.
 * См. [карта] для альтернативы, которая переосмысливает исключения из функции " преобразование`.
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R, T> Result<T>.mapCatching(transform: (value: T) -> R): Result<R> {
    return when {
        isSuccess -> runCatching { transform(value as T) }
        else -> Result(value)
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
 * if this instance represents [failure][Result.isFailure] or the
 * original encapsulated value if it is [success][Result.isSuccess].
 * Возвращает инкапсулированный результат данной функции [transform], примененной к инкапсулированному исключению [Throwable] .
 * если этот экземпляр представляет [failure][Result.isFailure] или
 * исходное инкапсулированное значение, если это [success][Result.isSuccess].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
 * See [recoverCatching] for an alternative that encapsulates exceptions.
 * Обратите внимание, что эта функция переосмысливает любое исключение [Throwable], вызванное функцией [transform].
 * Альтернативу, инкапсулирующую исключения, см. в разделе [recoverCatching].
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R, T : R> Result<T>.recover(transform: (exception: Throwable) -> R): Result<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> Result.success(transform(exception))
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
 * if this instance represents [failure][Result.isFailure] or the
 * original encapsulated value if it is [success][Result.isSuccess].
 * * Возвращает инкапсулированный результат данной функции [transform], примененной к инкапсулированному исключению [Throwable] .
 * если этот экземпляр представляет [failure][Result.isFailure] или
 * исходное инкапсулированное значение, если это [success][Result.isSuccess].
 *
 * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
 * See [recover] for an alternative that rethrows exceptions.
 * Эта функция улавливает любое исключение [Throwable] , вызванное функцией [transform], и инкапсулирует его как сбой.
 * См. [восстановление] для альтернативы, которая переосмысливает исключения.
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <R, T : R> Result<T>.recoverCatching(transform: (exception: Throwable) -> R): Result<R> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> runCatching { transform(exception) }
    }
}

// "peek" onto value/exception and pipe
// "заглянуть" в значение/исключение и канал

/**
 * Performs the given [action] on the encapsulated [Throwable] exception if this instance represents [failure][Result.isFailure].
 * Returns the original `Result` unchanged.
 * Выполняет заданное [действие] над инкапсулированным значением, если этот экземпляр представляет [success][Result.isSuccess].
 * Возвращает исходный " Результат` без изменений.
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    exceptionOrNull()?.let { action(it) }
    return this
}

/**
 * Performs the given [action] on the encapsulated value if this instance represents [success][Result.isSuccess].
 * Returns the original `Result` unchanged.
 * Выполняет заданное [действие] над инкапсулированным значением, если этот экземпляр представляет [success][Result.isSuccess].
 * Возвращает исходный " Результат` без изменений.
 */
@ExperimentalContracts
//@InlineOnly
@SinceKotlin("1.3")
public inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isSuccess) action(value as T)
    return this
}

// -------------------
