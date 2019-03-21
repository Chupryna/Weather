package com.example.weather

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGetWeather.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val city = editCity.text
        isNetworkAvailable()
        println(city)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.allNetworkInfo
        if (networkInfo != null) {
            for (info in networkInfo)
                if (info.state == NetworkInfo.State.CONNECTED)
                    return true
        }
        return false
    }
}