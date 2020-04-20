package com.example.agenda

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.widget.CalendarView.OnDateChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteprova.BBDD_Helper
import com.example.sqliteprova.Estructura_BBDD
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var dia = ""
    val helper = BBDD_Helper(this)
    val notesXdia = null;

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bt_nota1.setOnClickListener {
            if (!dia.equals("")) {//si ha seleccionat un dia
                val i =  Intent(this, VistaNotes::class.java)
                i.putExtra("data",dia)
                startActivity(i)
            } else {
                var toast = Toast.makeText(this, "Selecciona un dia", Toast.LENGTH_SHORT).show()
            }
        }

        button_afegirNota.setOnClickListener {

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

        val db: SQLiteDatabase = helper.readableDatabase

        calendari.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            dia = dayOfMonth.toString()+"/"+(month+1).toString()+"/"+year.toString()


            val projection = arrayOf(Estructura_BBDD.COL_TITOL, Estructura_BBDD.COL_HORA)

            val selection = "${Estructura_BBDD.COL_DIA} = ?"
            val selectionArgs = arrayOf(dia)

            val cursor = db.query(
                Estructura_BBDD.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            bt_nota.setText(cursor.count.toString())

            for (x in 0..cursor.count) {
            }

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