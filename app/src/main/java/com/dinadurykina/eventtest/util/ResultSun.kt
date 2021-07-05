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

/**
 * A generic class that holds a value with its loading status.
 * Универсальный класс, который содержит значение со статусом загрузки.
 * @param <T>
 *     https://github.com/Kotlin/KEEP/blob/497c86126b7320f3651807eb070f5efe80bcdb3a/proposals/stdlib/result.md
 *     https://github.com/Kotlin/KEEP/commit/497c86126b7320f3651807eb070f5efe80bcdb3a
 *     https://github.com/Kotlin/KEEP/blob/497c86126b7320f3651807eb070f5efe80bcdb3a/proposals/stdlib/result.md
 *     kotlin-stdlib-common-1.5.10-sources.jar!/kotlin/util/Result.kt
 *
 */

sealed class ResultSun<out R> {

    data class SuccessSun<out T>(val data: T) : ResultSun<T>()
    data class ErrorSun(val exception: Exception) : ResultSun<Nothing>()
    object LoadingSun : ResultSun<Nothing>()

    override fun toString(): String =
        when (this) {
            is SuccessSun<*> -> "Success[data=$data]"
            is ErrorSun -> "Error[exception=$exception]"
            LoadingSun -> "Loading"
        }

}

/**
 * `true` if [ResultSun] is of type [Success] & holds non-null [Success.data].
 * 'true', если [ResultSun] имеет тип [Success] и содержит ненулевые [Success.data].
 */
val ResultSun<*>.succeeded
    get() = this is ResultSun.SuccessSun && data != null

// Not obligatory
fun <T> ResultSun<T>.successOr(fallback: T): T = (this as? ResultSun.SuccessSun<T>)?.data ?: fallback


/*
 private suspend fun getTasks(forceUpdate: Boolean = false): ResultSun<List<Task>> {
  fun observeTasks(): LiveData<ResultSun<List<Task>>> {
        return tasksLocalDataSource.observeTasks()
private val observableTasks = MutableLiveData<ResultSun<List<Task>>>()
 override fun observeTasks(): LiveData<ResultSun<List<Task>>> {
        return observableTasks
override fun observeTask(taskId: String): LiveData<ResultSun<Task>> {
        return observableTasks.map { tasks ->
            when (tasks) {
                is ResultSun.Loading -> ResultSun.Loading
                is Error -> Error(tasks.exception)
                is Success -> {
                    val task = tasks.data.firstOrNull { it.id == taskId }
                        ?: return@map Error(Exception("Not found"))
                    Success(task)
                }
            }
        }
override suspend fun getTask(taskId: String): ResultSun<Task> {
        // Simulate network by delaying the execution.
        // Имитация сети путем задержки выполнения.
        delay(SERVICE_LATENCY_IN_MILLIS)
        TASKS_SERVICE_DATA[taskId]?.let {
            return Success(it)
        }
        return Error(Exception("Task not found"))
    }
 */