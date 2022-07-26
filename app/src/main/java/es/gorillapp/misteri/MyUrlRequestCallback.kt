package es.gorillapp.misteri

import android.util.Log
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import org.json.JSONException
import org.json.JSONObject
import java.nio.ByteBuffer


private const val TAG = "MyUrlRequestCallback"

class MyUrlRequestCallback : UrlRequest.Callback() {

    companion object {
        var delegate: OnFinishRequest<JSONObject>? = null
    }

    var headers: String? = null
    var responseBody: String? = null
    var httpStatusCode = 0

    interface OnFinishRequest<JSONObject> {
        fun onFinishRequest(result: JSONObject)
    }

    fun MyUrlRequestCallback(onFinishRequest: OnFinishRequest<JSONObject>?) {
        //You should create a MyUrlRequestCallback.OnFinishRequest() and
        //override onFinishRequest.
        //We will send JSON String response to this interface and you can then
        //perform actions on the UI or otherwise based on the result.

        //All MyUrlRequestCallback functions send response to this.delegate
        //which provides it to the interface onFinishRequest which you use in
        //your activity or fragment.
        delegate = onFinishRequest
    }

    override fun onRedirectReceived(request: UrlRequest?, info: UrlResponseInfo?, newLocationUrl: String?) {
        Log.i(TAG, "onRedirectReceived method called.")
        // You should call the request.followRedirect() method to continue
        // processing the request.
        request?.followRedirect()
    }

    override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(TAG, "onResponseStarted method called.")
        // You should call the request.read() method before the request can be
        // further processed. The following instruction provides a ByteBuffer object
        // with a capacity of 102400 bytes to the read() method.
        request?.read(ByteBuffer.allocateDirect(102400))
    }

    override fun onReadCompleted(request: UrlRequest?, info: UrlResponseInfo?, byteBuffer: ByteBuffer?) {
        Log.i(TAG, "onReadCompleted method called.")
        // You should keep reading the request until there's no more data.
        request?.read(ByteBuffer.allocateDirect(102400))
        val statusCode = info!!.httpStatusCode
        this.httpStatusCode = statusCode

        val bytes: ByteArray
        if (byteBuffer!!.hasArray()) {
            bytes = byteBuffer.array()
        } else {
            bytes = ByteArray(byteBuffer.remaining())
            byteBuffer[bytes]
        }

        var responseBodyString = String(bytes) //Convert bytes to string

        //Properly format the response String
        responseBodyString = responseBodyString.trim { it <= ' ' }
            .replace("(\r\n|\n\r|\r|\n|\r0|\n0)".toRegex(), "")
        if (responseBodyString.endsWith("0")) {
            responseBodyString = responseBodyString.substring(0, responseBodyString.length - 1)
        }

        this.responseBody = responseBodyString

        val headers = info.allHeaders //get headers


        val reqHeaders: String = createHeaders(headers)

        val results = JSONObject()
        try {
            results.put("headers", reqHeaders)
            results.put("body", responseBodyString)
            results.put("statusCode", statusCode)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //Send to OnFinishRequest which we will override in activity to read results gotten.
        delegate?.onFinishRequest(results)
    }

    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
        Log.i(TAG, "onSucceeded method called.")
    }

    override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException?) {
        Log.e(TAG, "The request failed.", error)
    }

    private fun createHeaders(headers: Map<String, List<String>>): String {
        var accessToken = "null"
        var client = "null"
        var uid = "null"
        var expiry: Long = 0
        if (headers.containsKey("Access-Token")) {
            val accTok = headers["Access-Token"]!!
            if (accTok.size > 0) {
                accessToken = accTok[accTok.size - 1]
            }
        }
        if (headers.containsKey("Client")) {
            val cl = headers["Client"]!!
            if (cl.size > 0) {
                client = cl[cl.size - 1]
            }
        }
        if (headers.containsKey("Uid")) {
            val u = headers["Uid"]!!
            if (u.size > 0) {
                uid = u[u.size - 1]
            }
        }
        if (headers.containsKey("Expiry")) {
            val ex = headers["Expiry"]!!
            if (ex.size > 0) {
                expiry = ex[ex.size - 1].toLong()
            }
        }
        val currentHeaders = JSONObject()
        try {
            currentHeaders.put("access-token", accessToken)
            currentHeaders.put("client", client)
            currentHeaders.put("uid", uid)
            currentHeaders.put("expiry", expiry)
            return currentHeaders.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return currentHeaders.toString()
    }
}
