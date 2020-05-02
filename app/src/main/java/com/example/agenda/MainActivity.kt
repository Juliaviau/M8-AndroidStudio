package com.example.agenda

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.CalendarView.OnDateChangeListener
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteprova.BBDD_Helper
import com.example.sqliteprova.Estructura_BBDD
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var dia = ""
    val helper = BBDD_Helper(this)
    val notesXdia = null;
    val llsep = LinearLayout.LayoutParams (
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    val lp = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        setContentView(R.layout.activity_crear_nota)
        setContentView(R.layout.activity_main)

        btn_AfegirNota.setOnClickListener {

            if (!dia.equals("")) {
                val i =  Intent(this, CrearNota::class.java)
                i.putExtra("donve", "crear")
                i.putExtra("data",dia)
                startActivity(i)
            } else {
                var toast = Toast.makeText(this, "Selecciona un dia", Toast.LENGTH_SHORT).show()
            }
        }

        val a = Calendar.getInstance()
        val y = a.get(Calendar.YEAR)
        val m = a.get(Calendar.MONTH)
        val d = a.get(Calendar.DAY_OF_WEEK)

        val db: SQLiteDatabase = helper.readableDatabase

        Calendari.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->

            if (scrollBotons.childCount > 0)
                scrollBotons.removeAllViews();

            dia = dayOfMonth.toString()+"/"+(month+1).toString()+"/"+year.toString()

            val projection = arrayOf(Estructura_BBDD.COL_TITOL, Estructura_BBDD.COL_HORA)

            //Selecciona totes les entrades on el dia sigui igual al seleccionat
            val selection = "${Estructura_BBDD.COL_DIA} = ?"
            val selectionArgs = arrayOf(dia)

            val cursor = db.query(
                Estructura_BBDD.TABLE_NAME, //De la base de dades
                projection,                 //Agafa el titol i l'hora
                selection,                  //On el dia sigui igual
                selectionArgs,              //Al dia seleccionat
                null,
                null,
                null
            )

            cursor.moveToFirst()

            //val nameColumnIndex: Int = cursor.getColumnIndex(Estructura_BBDD.COL_DIA)
            // bt_nota.setText(nameColumnIndex.toString())
            // val name: String = cursor.getString(nameColumnIndex)
            // bt_nota.setText(cursor.count.toString())

            if (cursor.count > 0) {
                val titols = ArrayList<String>()
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast) {
                            val name =
                                cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_TITOL))
                            titols.add(name)
                            cursor.moveToNext()
                        }
                }

                val hores = ArrayList<String>()
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast) {
                            val hora =
                                cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_HORA))
                            hores.add(hora)
                            cursor.moveToNext()
                        }
                    }

                for (x in 0..hores.size-1) {

                    val sepa = LinearLayout(this)
                    sepa.layoutParams = llsep
                    sepa.setBackgroundColor(Color.BLACK)

                    val button = Button(this)
                    button.layoutParams = lp
                    button.setBackgroundColor(Color.parseColor("#c4eada"))
                    button.text = titols.get(x) + "   |   " + hores.get(x)
                    button.setOnClickListener{
                        val i =  Intent(this, VistaNotes::class.java)
                        i.putExtra("data",dia+titols.get(x))
                        startActivity(i)
                    }
                    scrollBotons.addView(button)
                    scrollBotons.addView(sepa)
                }

            } else {

                val nohiha =  TextView (this)
                nohiha.layoutParams = lp
                nohiha.setBackgroundColor(Color.GRAY)
                nohiha.setPadding(0,308,0,308)
                nohiha.setTextColor(Color.WHITE)
                nohiha.setGravity(Gravity.CENTER or Gravity.BOTTOM)
                nohiha.text = "No hi ha cap nota per aquest dia"
                nohiha.textSize = 23f

                scrollBotons.addView(nohiha)
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
    //....................
    /*   val string_date = "12-December-2012"
       val f = SimpleDateFormat("dd-MMM-yyyy")
       val d: Date = f.parse(string_date)
       val milliseconds = d.time
       var toast = Toast.makeText(this, dia.toString(), Toast.LENGTH_SHORT).show()*/
    // calendari.setDate(1587513600000)
    //calendari.setDate(1586217600000)
    /*   val calendar = Calendar.getInstance()
       val dates: MutableList<Long> = ArrayList()

       calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
       dates.add(calendar.timeInMillis)

       calendar[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
       dates.add(calendar.timeInMillis)

       calendar[Calendar.DAY_OF_WEEK] = Calendar.FRIDAY
       dates.add(calendar.timeInMillis)

       for (x in dates) {
           calendari.setDate(dates)

       }*/

//.....................
}