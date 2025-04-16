package com.rsjd.dicodingeventmuji.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * Interface for observing network connectivity changes
 */
interface ConnectivityObserver {

    /**
     * Different states of network connectivity
     */
    enum class Status {
        AVAILABLE, UNAVAILABLE, LOSING, LOST
    }

    /**
     * Observe network status changes
     * @return Flow of Status
     */
    fun observe(): Flow<Status>
}

/**
 * Implementation of ConnectivityObserver using NetworkCallback
 */
class NetworkConnectivityObserver(
    private val context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Observe network status changes using NetworkCallback
     * @return Flow of Status
     */
    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(ConnectivityObserver.Status.AVAILABLE) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(ConnectivityObserver.Status.LOSING) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(ConnectivityObserver.Status.LOST) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(ConnectivityObserver.Status.UNAVAILABLE) }
                }
            }

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, callback)

            // Check current state and emit it
            val currentState = getCurrentConnectivityState()
            launch { send(currentState) }

            // When the flow collection is cancelled, unregister the callback
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    /**
     * Get the current network connectivity state
     * @return Current Status
     */
    private fun getCurrentConnectivityState(): ConnectivityObserver.Status {
        val network = connectivityManager.activeNetwork ?: return ConnectivityObserver.Status.UNAVAILABLE
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return ConnectivityObserver.Status.UNAVAILABLE

        return if (
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        ) {
            ConnectivityObserver.Status.AVAILABLE
        } else {
            ConnectivityObserver.Status.UNAVAILABLE
        }
    }
}