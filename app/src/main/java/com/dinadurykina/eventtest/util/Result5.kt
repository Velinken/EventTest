package com.dinadurykina.eventtest.util
// https://github.com/Kotlin/KEEP/blob/497c86126b7320f3651807eb070f5efe80bcdb3a/proposals/stdlib/result.md
/*

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import java.io.File

class Result5 {
    interface Continuation<in T> {
        fun resumeWith(result: Result<T>)
    }

    val deferreds: List<Unit> = List(n) {
        async {
            /* Do something that produces T or fails */
        }
    }
    val outcomes1: List<T> =
        deferreds.map { it.await() } // BAD -- crash on the first (by index) failure
    val outcomes2: List<T> = deferreds.awaitAll() // BAD -- crash on the earliest (by time) failure
    val outcomes3: List<Result<T>> =
        deferreds.map { runCatching { it.await() } } // !!! <= THIS IS THE ONE WE WANT

    fun readFileData(file: File): ContactsContract.Data {}

    fun readFilesCatching(files: List<File>): List<Result<Data>> =
        files.map {
            runCatching {
                readFileData(it)
            }
        }
    readFilesCatching (files) .map
    {
        result :  Result < Data > ->  // тип явно записан здесь для ясности
        result.map { it.doSomething() } // Работает в случае успеха, сохраняя неудачу
    }

    readFilesCatching (файлы) .map
    {
        result :  Result < Data > ->
        result.mapCatching { it.doSomething() }
    }

    // Например, рассмотрим этот фрагмент кода, который использует RxKotlin для асинхронной обработки.
    // Он вызывает, doSomethingAsyncкоторый возвращает Singleи обрабатывает потенциальную ошибку в функциональном стиле:

    doSomethingAsync ()
    .subscribe (
    { processData(it) },
    { showErrorDialog(it) }
    )

    // Работа с функцией, возвращающей Java, CompletableFuture визуально похожа:

    doSomethingAsync()
    .whenComplete
    {
        data, exception ->
        if (exception != null)
            showErrorDialog(exception)
        else
            processData(data)
    }

    try
    {
        val data = doSomethingSync()
        processData(data)
    } catch(e: Throwable)
    {
        showErrorDialog(e)
    }

    // Вместо этого мы хотели бы иметь возможность писать тот же код более функциональным способом:

    runCatching
    { doSomethingSync() }
    .onFailure
    { showErrorDialog(it) }
    .onSuccess
    { processData(it) }


    // Это решение было опробовано в экспериментальной версии сопрограмм Kotlin
    interface Continuation13<in T> {
        fun resume(value: T)
        fun resumeWithException(exception: Throwable)
    }

    // Один метод с двумя параметрами :
    interface Continuation14<in T> {
        fun resume(value: T?, exception: Throwable?)
    }

    // Один метод с Any? параметр :
    interface Continuation1ANY<in T> {
        fun resume(result: Any?) // result: T | Failure(Throwable)
    }

    // Тип значения, не допускающего значения NULL :
    val data: Data? = try {
        doSomethingSync()
    } catch (e: Throwable) {
        showErrorDialog(e)
        null
    }
    if (data != null)
    processData(data )

    // Тип значения, допускающего значение NULL :

    var data: Data? = null
    val success = try {
        data = doSomethingSync()
        true
    } catch (e: Throwable) {
        showErrorDialog(e)
        false
    }
    if (success)
    processData(data )
}
   // Следующий фрагмент дает сводку всех общедоступных API:
   @JvmInline
   value class Result<out T> /* internal constructor */ {
       val isSuccess: Boolean
       val isFailure: Boolean
       fun getOrNull(): T?
       fun exceptionOrNull(): Throwable?

       companion object {
           fun <T> success(value: T): Result<T>
           fun <T> failure(exception: Throwable): Result<T>
       }
   }

    inline fun <R> runCatching(block: () -> R): Result<R>
    inline fun <T, R> T.runCatching(block: T.() -> R): Result<R>

    fun <T> Result<T>.getOrThrow(): T
    fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R

    inline fun <R, T : R> Result<T>.getOrElse(onFailure: (exception: Throwable) -> R): R
    inline fun <R, T> Result<T>.fold(onSuccess: (value: T) -> R, onFailure: (exception: Throwable) -> R): R

    inline fun <R, T> Result<T>.map(transform: (value: T) -> R): Result<R>
    inline fun <R, T: R> Result<T>.recover(transform: (exception: Throwable) -> R): Result<R>

    inline fun <R, T> Result<T>.mapCatching(transform: (value: T) -> R): Result<R>
    inline fun <R, T: R> Result<T>.recoverCatching(transform: (exception: Throwable) -> R): Result<R>

    inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T>
    inline fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T>

    // legacy < 1.5
    fun findUserByName(name: String): Result<User> // ERROR: 'kotlin.Result' cannot be used as a return type
    fun foo(): Result<List<Int>> // ERROR
    fun foo(): Result<Int>? // ERROR
    var foo: Result<Int> // ERROR

    fun findIntResults(): List<Result<Int>> = arrayListOf(Result())// Ok
    fun receiveIntResult(result: Result<Int>){} // Ok

    private  val first :  Result < Int > = findIntResults (). first () // Хорошо, даже если `first` имеет тип Result <Int>
    private  var foo :  Result < Int > // Хорошо

    val r :  Result < String ?> = runCatching {readLine ()}
    println (r!!) // ОШИБКА

    @PublishedApi internal fun createFailure(exception: Throwable): Any{}
    @PublishedApi internal fun Result<*>.throwOnFailure(){}

    fun findUserByName(name: String): Result<User> // ERROR
    fun findUserByName(name: String): User? // Ok

    sealed class FindUserResult {
        data class Found(val user: User) : FindUserResult()
        data class NotFound(val name: String) : FindUserResult()
        data class MalformedName(val name: String) : FindUserResult()
        // other cases that need different business-specific handling code
    }

    fun findUserByName(name: String): FindUserResult

    fun findUserByName(name: String): User{}

/*
Если вызывающий эту функцию хочет выполнить несколько операций и впоследствии обработать их сбои (без прерывания при первом сбое),
 он всегда может использовать, runCatching { findUserByName(name) } чтобы явно указать,
  что сбой обнаруживается и инкапсулируется в Resultэ кземпляр.
 */

 */
