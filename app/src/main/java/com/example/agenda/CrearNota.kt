package com.example.agenda

import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteprova.BBDD_Helper
import com.example.sqliteprova.Estructura_BBDD
import kotlinx.android.synthetic.main.activity_crear_nota.*
import java.text.SimpleDateFormat
import java.util.*


class CrearNota : AppCompatActivity() {

    var data: String = ""
    var donve: String = ""
    var idNota: String = ""
    val helper = BBDD_Helper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);        setContentView(R.layout.activity_crear_nota)

        val objIntent: Intent =intent
        data = objIntent.getStringExtra("data")
        donve = objIntent.getStringExtra("donve")

        tv_MostraDia.setText(data)

        if (donve.equals("crear")) {

            btn_EscollirHora.setOnClickListener {
                val c = Calendar.getInstance()
                var hora = c.get(Calendar.HOUR_OF_DAY)
                var minuts = c.get(Calendar.MINUTE)

                val tpd = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                    c.set(Calendar.MINUTE,minute)
                    tv_MostraHora.setText(SimpleDateFormat("HH:mm").format(c.time))
                }

                TimePickerDialog(this,tpd,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show()
            }

            ivBtn_AfegirNota.setOnClickListener{

                val asa: String = et_TitolNota.text.toString()

                if (!et_TitolNota.text.isEmpty() && tv_MostraHora.text != "hh:mm" && !Eet_ContingutNota.text.isEmpty()) {

                    val db: SQLiteDatabase = helper.readableDatabase
                    val projection = arrayOf(Estructura_BBDD.COL_ID)
                    val selection = "${Estructura_BBDD.COL_ID} = ?"
                    val selectionArgs = arrayOf(data+asa)

                    val cursor = db.query(
                        Estructura_BBDD.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                    )

                    cursor.moveToFirst()
                    val count = cursor.count

                    if (count == 0) {

                        val db: SQLiteDatabase = helper.writableDatabase
                        val values = ContentValues().apply {
                            put(Estructura_BBDD.COL_ID,        data+asa)
                            put(Estructura_BBDD.COL_DIA,       data)
                            put(Estructura_BBDD.COL_HORA,      tv_MostraHora.text.toString())
                            put(Estructura_BBDD.COL_TITOL,     asa)
                            put(Estructura_BBDD.COL_CONTINGUT, Eet_ContingutNota.text.toString())
                        }

                        val nova = db?.insert(Estructura_BBDD.TABLE_NAME, null, values)
                        val toast = Toast.makeText(applicationContext, "Dades guardades", Toast.LENGTH_SHORT)
                        toast.show()
                        val i =  Intent(this, MainActivity::class.java)
                        startActivity(i)
                    } else {
                        val toast = Toast.makeText(applicationContext, "Titol duplicat", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val toast = Toast.makeText(applicationContext, "Camps buits", Toast.LENGTH_SHORT).show()
                }
            }
            ivBtn_EliminarNota.setOnClickListener{
                val i =  Intent(this, MainActivity::class.java)
                startActivity(i)
            }

        } else {
            idNota = objIntent.getStringExtra("idNota")

            val db: SQLiteDatabase = helper.readableDatabase
            val projection = arrayOf(Estructura_BBDD.COL_TITOL, Estructura_BBDD.COL_DIA, Estructura_BBDD.COL_HORA,Estructura_BBDD.COL_CONTINGUT)

            val selection = "${Estructura_BBDD.COL_ID} = ?"
            val selectionArgs = arrayOf(idNota)

            val cursor = db.query(
                Estructura_BBDD.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            cursor.moveToFirst()

            tv_MostraDia.setText(        cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_DIA)))
            et_TitolNota.setText(  cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_TITOL)))
            tv_MostraHora.setText(       cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_HORA)))
            Eet_ContingutNota.setText( cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_CONTINGUT)))


            btn_EscollirHora.setOnClickListener {

                val c = Calendar.getInstance()
                var hora = c.get(Calendar.HOUR_OF_DAY)
                var minuts = c.get(Calendar.MINUTE)

                val tpd = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    c.set(Calendar.HOUR_OF_DAY,hourOfDay)
                    c.set(Calendar.MINUTE,minute)
                    tv_MostraHora.setText(SimpleDateFormat("HH:mm").format(c.time))
                }
                TimePickerDialog(this,tpd,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show()
            }

            ivBtn_AfegirNota.setOnClickListener{

                val db: SQLiteDatabase = helper.writableDatabase
                val titol : String = et_TitolNota.text.toString()
                val values = ContentValues().apply {
                    put(Estructura_BBDD.COL_TITOL, titol)
                    put(Estructura_BBDD.COL_HORA, tv_MostraHora.text.toString())
                    put(Estructura_BBDD.COL_CONTINGUT, Eet_ContingutNota.text.toString())
                }

                val count = db.update(
                    Estructura_BBDD.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs)

                val toast = Toast.makeText(applicationContext, "Dades actualitzades", Toast.LENGTH_SHORT)
                toast.show()

                //finish()
                val i =  Intent(this, VistaNotes::class.java)
                startActivity(i)
            }

            ivBtn_EliminarNota.setOnClickListener{
                val i =  Intent(this, MainActivity::class.java)
                startActivity(i)
            }
        }
    }
}