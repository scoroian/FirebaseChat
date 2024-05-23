package com.scoroian.firebasechat

import android.app.AlertDialog
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
            val options = arrayOf("Chat", "Comments")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select an option for $selectedCity")
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val intent = Intent(this, ChatActivity::class.java).apply {
                            putExtra("selectedCity", selectedCity)
                        }
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this, CityCommentsActivity::class.java).apply {
                            putExtra("selectedCity", selectedCity)
                        }
                        startActivity(intent)
                    }
                }
            }
            builder.show()
        }

        view.cityRecyclerView.layoutManager = LinearLayoutManager(this)
        view.cityRecyclerView.adapter = cityAdapter
    }
}
