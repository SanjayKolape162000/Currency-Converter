package com.sanjay.currencyconverter

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private  var save_code:String =""
    private lateinit var spinner: Spinner
    private lateinit var today_date:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
          today_date=findViewById(R.id.today_date)
        spinner=findViewById(R.id.spinner)
        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, option)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                save_code = "select option"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                save_code = option_code[position]

            }

        }

    }

    fun get(view: View){
        val downloadData = download()

        try {

            val url =
                "http://data.fixer.io/api/latest?access_key=0d3041a50c5eb9bb7fccd407d759b48d"

            downloadData.execute(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    inner class download : AsyncTask<String, Void, String>() {


        override fun doInBackground(vararg params: String?): String {

            var result = ""
            var url: URL
            var httpURLConnection: HttpURLConnection

            try {
                url = URL(params[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)


                var data = inputStreamReader.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character

                    data = inputStreamReader.read()
                }

                return result
            } catch (e: Exception) {
                e.printStackTrace()

                return result
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObject = JSONObject(result)
                Log.d("MainActivity", jsonObject.toString())
                val base = jsonObject.getString("base")

                val date = jsonObject.getString("date")
                val rate = jsonObject.getString("rates")
                val newjsonObject = JSONObject(rate)
                val inr = newjsonObject.getString(save_code)

                Log.d("MainActivity", base)
                Log.d("MainActivity", date)
                Log.d("MainActivity", inr)


                today_date.text=date
                inrText.text = "1 EURO =$inr $save_code"



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }



    private val option = arrayOf(
        "Afghanistan", "Argentina", "Australia", "Bangladesh", "Brazil",
        "Canada", "China", "France", "Germany", "India", "Italy", "Japan",
        "Mexico", "NewZealand", "Pakistan", "Qatar", "Russia", "Saudi Arabia",
        "South Africa", "Spain", "Sri Lanka", "Switzerland", "USA"
    )

   private val option_code = arrayOf(
        "AFN", "ARS", "AUD", "BDT", "BRL",
        "CAD", "CNY", "EUR", "EUR", "INR", "EUR", "JPY",
        "MXN", "NZD", "PKR", "QAR", "RUB", "SAR",
        "ZAR", "EUR", "LKR", "CHW", "USD"
    )
}
