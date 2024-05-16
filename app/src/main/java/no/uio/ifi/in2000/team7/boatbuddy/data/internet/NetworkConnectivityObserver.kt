package no.uio.ifi.in2000.team7.boatbuddy.data.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team7.boatbuddy.model.internet.Status
import javax.inject.Inject

class NetworkConnectivityObserver @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    // listen to internett access
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    suspend fun observe(): Flow<Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(Status.AVAILABLE) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(Status.LOSING) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(Status.LOST) }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch { send(Status.UNAVAILABLE) }
                }

            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

}
