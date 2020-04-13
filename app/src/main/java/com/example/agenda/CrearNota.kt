package com.example.agenda

import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteprova.BBDD_Helper
import com.example.sqliteprova.Estructura_BBDD
import kotlinx.android.synthetic.main.activity_crear_nota.*
import kotlinx.android.synthetic.main.activity_vista_notes.*
import java.text.SimpleDateFormat
import java.util.*


class CrearNota : AppCompatActivity() {

    var data: String = ""
    val helper = BBDD_Helper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_nota)

        val objIntent: Intent =intent
        data = objIntent.getStringExtra("data")

        textView3.setText(data)

        b_hora.setOnClickListener {
            val c = Calendar.getInstance()
            var hora = c.get(Calendar.HOUR_OF_DAY)
            var minuts = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                c.set(Calendar.MINUTE,minute)
                tv_hora.setText(SimpleDateFormat("HH:mm").format(c.time))
            }

            TimePickerDialog(this,tpd,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show()
        }

        iv_afegir.setOnClickListener{
            // Gets the data repository in write mode

            val asa: String = tv_titol.text.toString()

            val db: SQLiteDatabase = helper.writableDatabase

            // Create a new map of values, where column names are the keys
            //val values = ContentValues().apply {
                //put(Estructura_BBDD.COL_ID, data+et_TitolNota.text.toString())
               // put(Estructura_BBDD.COL_DIA, tv_dia.text.toString())
               // put(Estructura_BBDD.COL_HORA, tv_hora.text.toString())
             //   put(Estructura_BBDD.COL_TITOL, tv_titol.text.toString())
               // put(Estructura_BBDD.COL_CONTINGUT, Eet_Contingut.text.toString())
          //  }

            // Insert the new row, returning the primary key value of the new row
           // val newRowId = db?.insert(Estructura_BBDD.TABLE_NAME, null, values)

            val toast = Toast.makeText(applicationContext, asa, Toast.LENGTH_SHORT)
            toast.show()
        }

        iv_eliminar.setOnClickListener{
            val i =  Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
}
