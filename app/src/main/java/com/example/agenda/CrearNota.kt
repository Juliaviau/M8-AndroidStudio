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

        tv_Dia.setText(data)

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

            val asa: String = et_TitolNota.text.toString()

            val db: SQLiteDatabase = helper.writableDatabase

            val values = ContentValues().apply {
                put(Estructura_BBDD.COL_ID,        data+asa)
                put(Estructura_BBDD.COL_DIA,       data)
                put(Estructura_BBDD.COL_HORA,      tv_hora.text.toString())

                put(Estructura_BBDD.COL_TITOL,     asa)
                put(Estructura_BBDD.COL_CONTINGUT, Eet_Contingut.text.toString())
            }

            val newRowId = db?.insert(Estructura_BBDD.TABLE_NAME, null, values)

            val toast = Toast.makeText(applicationContext, "Dades guardades", Toast.LENGTH_SHORT)
            toast.show()

            val i =  Intent(this, MainActivity::class.java)
            startActivity(i)
        }

        iv_eliminar.setOnClickListener{
            val i =  Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }
}
