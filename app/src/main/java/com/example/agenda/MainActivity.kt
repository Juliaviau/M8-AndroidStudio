package com.example.agenda

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var dia = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_nota1.setOnClickListener {
            if (!dia.equals("")) {
                val i =  Intent(this, VistaNotes::class.java)
                i.putExtra("data",dia)
                startActivity(i)
            } else {
                var toast = Toast.makeText(this, "Selecciona un dia", Toast.LENGTH_SHORT).show()
            }
        }

        button_afegirNota.setOnClickListener {

            //si ha seleccionat un dia, fa, sino mostra un toast dient que seleccioni dia
            if (!dia.equals("")) {
                val i =  Intent(this, CrearNota::class.java)
                i.putExtra("data",dia)
                startActivity(i)
            } else {
                var toast = Toast.makeText(this, "Selecciona un dia", Toast.LENGTH_SHORT).show()
            }
        }

        /*calendari.setOnDateChangeListener(CalendarView.OnDateChangeListener() {
            overridefun onSelectedDayChange( calendarView: CalendarView, int i, int i1, int i2) {
                val date = (i1+1)+"/"+i2+"/"+i
                Log.d(Tag, "onSelectedDayChange: mm/dd/yyy:" + date)
            }
        })*/

        val a = Calendar.getInstance()
        val y = a.get(Calendar.YEAR)
        val m = a.get(Calendar.MONTH)
        val d = a.get(Calendar.DAY_OF_WEEK)

        calendari.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            dia = dayOfMonth.toString()+"/"+(month+1).toString()+"/"+year.toString()
        })
        /*button2.setOnClickListener {
            val da = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                textView3.setText("" + dayOfMonth + "/" + month + "/" + year)
            }, y, m, d)
            da.show()
        }*/

        /*calendari.setOnDateChangeListener( CalendarView.OnDateChangeListener() {
        })*/
    }

   /* private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }
    */
}