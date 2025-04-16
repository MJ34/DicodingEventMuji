package com.rsjd.dicodingeventmuji.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A utility class to implement debounce for search functionality
 */
class Debounce {
    companion object {
        /**
         * Perform a debounced action
         * @param delayMillis Time in milliseconds to wait before executing the action
         * @param coroutineScope The coroutine scope to launch the action
         * @param action The action to perform
         * @return A Job that can be used to cancel the action
         */
        fun debounce(
            delayMillis: Long = 300L,
            coroutineScope: CoroutineScope,
            action: () -> Unit
        ): Job {
            var debounceJob: Job? = null

            return coroutineScope.launch {
                debounceJob?.cancel()

                debounceJob = launch {
                    delay(delayMillis)
                    action()
                }
            }
        }
    }
}