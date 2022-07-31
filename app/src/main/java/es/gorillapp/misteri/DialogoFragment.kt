package es.gorillapp.misteri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DialogoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialogoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        //downloadDialogoTask(param1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialogo, container, false)


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val originalTextView = view?.findViewById<View>(R.id.dialogo_texto_original) as TextView
        val translateTextView = view?.findViewById<View>(R.id.dialogo_traduccion) as TextView
        originalTextView.text = param1
        translateTextView.text = param2



    }

    private fun downloadDialogoTask(url: String?){

        val queue = VolleySingleton.getInstance(this.requireContext()).requestQueue


        val request = StringRequest(
            Request.Method.GET, url,
            { response ->

                //if(!isFirstView) Thread.sleep(2000)
                val data = response.toString()
                val jArray = JSONArray(data)
                val jsonData = jArray.getJSONObject(0)

                //Titulo
                val title: TextView? = activity?.findViewById(R.id.direct_title)
                title!!.text = jsonData.getString("titulo")

                //Texto original en valenciano
                val textoOriginal: TextView? = activity?.findViewById(R.id.dialogo_texto_original)
                textoOriginal!!.text = jsonData.getString("textoOriginal")

                //Traducción al castellamo
                val traduccion: TextView? = activity?.findViewById(R.id.dialogo_traduccion)
                traduccion!!.text = jsonData.getString("traduccion")

            },
            {error->
                Toast.makeText(this.requireContext(), "getVolleyError(error)", Toast.LENGTH_LONG).show()})
        queue.add(request)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DialogoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DialogoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}