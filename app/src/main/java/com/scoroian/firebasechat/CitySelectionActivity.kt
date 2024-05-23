package com.scoroian.firebasechat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.scoroian.firebasechat.databinding.ActivityCitySelectionBinding

class CitySelectionActivity : AppCompatActivity() {
    private val cities = listOf("Zaragoza", "Madrid", "Barcelona", "Valencia", "Sevilla", "Bilbao", "Murcia", "Málaga", "Palma", "Las Palmas")
    private val view by lazy { ActivityCitySelectionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val cityAdapter = CityAdapter(cities) { selectedCity ->
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("selectedCity", selectedCity)
            }
            startActivity(intent)
        }

        view.cityRecyclerView.layoutManager = LinearLayoutManager(this)
        view.cityRecyclerView.adapter = cityAdapter
    }
}
