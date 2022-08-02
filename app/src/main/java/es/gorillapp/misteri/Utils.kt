package es.gorillapp.misteri

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.android.volley.*
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import org.xmlpull.v1.XmlPullParserException
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException

fun Activity.getVolleyError(error: VolleyError): String {
    var errorMsg = ""
    if (error is NoConnectionError) {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetwork: NetworkInfo? = null
        activeNetwork = cm.activeNetworkInfo
        errorMsg = if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            "Server is not connected to the internet. Please try again"
        } else {
            "Your device is not connected to internet.please try again with active internet connection"
        }
    } else if (error is NetworkError || error.cause is ConnectException) {
        errorMsg = "Your device is not connected to internet.please try again with active internet connection"
    } else if (error.cause is MalformedURLException) {
        errorMsg = "That was a bad request please try again…"
    } else if (error is ParseError || error.cause is IllegalStateException || error.cause is JSONException || error.cause is XmlPullParserException) {
        errorMsg = "There was an error parsing data…"
    } else if (error.cause is OutOfMemoryError) {
        errorMsg = "Device out of memory"
    } else if (error is AuthFailureError) {
        errorMsg = "Failed to authenticate user at the server, please contact support"
    } else if (error is ServerError || error.cause is ServerError) {
        errorMsg = "Internal server error occurred please try again...."
    } else if (error is TimeoutError || error.cause is SocketTimeoutException || error.cause is ConnectTimeoutException || error.cause is SocketException || (error.cause!!.message != null && error.cause!!.message!!.contains(
            "Your connection has timed out, please try again"
        ))
    ) {
        errorMsg = "Your connection has timed out, please try again"
    } else {
        errorMsg = "An unknown error occurred during the operation, please try again"
    }
    return errorMsg
}

fun isTablet(context: Context): Boolean {
    val xlarge = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_XLARGE
    val large =
        context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE
    return xlarge || large
}
