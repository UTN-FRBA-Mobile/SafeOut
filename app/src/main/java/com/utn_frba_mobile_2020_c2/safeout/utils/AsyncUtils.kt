package com.utn_frba_mobile_2020_c2.safeout.utils
import kotlinx.coroutines.*


object AsyncUtils {
    fun runInBackground(task: suspend () -> Unit) {
        val errorHandler = CoroutineExceptionHandler { _, throwable ->
            throw throwable
        }
        GlobalScope.launch(errorHandler) {
            var result: String = ""
            withContext(Dispatchers.IO) {
                task()
            }
        }
    }

    suspend fun updateUi(task: suspend () -> Unit) {
        withContext(Dispatchers.Main) {
            task()
        }
    }
}
