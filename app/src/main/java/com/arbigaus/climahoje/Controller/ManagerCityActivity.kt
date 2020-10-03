package com.arbigaus.climahoje.Controller

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbigaus.climahoje.Model.City
import com.arbigaus.climahoje.Model.DataStore
import com.arbigaus.climahoje.R
import com.arbigaus.climahoje.View.CityAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_manager_city.*

class ManagerCityActivity : AppCompatActivity() {
    private var adapter: CityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_city)

        rcvCities.layoutManager = LinearLayoutManager(this)
        adapter = CityAdapter(DataStore.cities)
        rcvCities.adapter = adapter

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent?) {
                super.onLongPress(e)

                e?.let {
                    val view = rcvCities.findChildViewUnder(e.x, e.y)

                    view?.let {
                        val position = rcvCities.getChildAdapterPosition(view)
                        val city = DataStore.getCity(position)

                        val dialog = AlertDialog.Builder(this@ManagerCityActivity)
                        dialog.setTitle("App Cidades")
                        dialog.setMessage("Tem certeza que deseja excluir esta cidade?")
                        dialog.setPositiveButton("Excluir", DialogInterface.OnClickListener { dialog, which ->
                            DataStore.removeCity(position, this@ManagerCityActivity)
                            adapter!!.notifyDataSetChanged()
                            Snackbar.make(layManagerCity, "Cidade excluída: ${city.name}", Snackbar.LENGTH_LONG).show()
                        })
                        dialog.setNegativeButton("Cancelar", null)
                        dialog.show()

                    }

                }
            }
        })

        rcvCities.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                TODO("Not yet implemented")
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y);
                return (child != null && gestureDetector.onTouchEvent(e))
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                TODO("Not yet implemented")
            }

        })

        insertButton.setOnClickListener {
            if (textInputCity.text.toString().isNotEmpty()) {
                val city = City(textInputCity.text.toString(), false)
                DataStore.addCity(city)
                adapter!!.notifyDataSetChanged()
                textInputCity.text?.clear()
            }
        }
    }
}