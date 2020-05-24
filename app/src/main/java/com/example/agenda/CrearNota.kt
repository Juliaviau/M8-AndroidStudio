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

        //Rep la data de la nota a veure i el guarda a data, i de quina pantalla ve
        val objIntent: Intent =intent
        data = objIntent.getStringExtra("data")
        donve = objIntent.getStringExtra("donve")

        //Al l'apartat del text del dia, es mostra la data sseleccionada al calendari
        tv_MostraDia.setText(data)

        //Si vol crear una nota
        if (donve.equals("crear")) {

            //Al clicar al rellotge
            btn_EscollirHora.setOnClickListener {
                val instanciaCalendari = Calendar.getInstance()
                var horaCalendari = instanciaCalendari.get(Calendar.HOUR_OF_DAY)
                var minutsCalendari = instanciaCalendari.get(Calendar.MINUTE)

                //S'escull l'hora i es mostra la seleccionada al lloc pertinent
                val seleccioHora = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    instanciaCalendari.set(Calendar.HOUR_OF_DAY,hourOfDay)
                    instanciaCalendari.set(Calendar.MINUTE,minute)
                    tv_MostraHora.setText(SimpleDateFormat("HH:mm").format(instanciaCalendari.time))
                }

                //Mostra per escollir l'hora
                TimePickerDialog(this,seleccioHora,instanciaCalendari.get(Calendar.HOUR_OF_DAY),instanciaCalendari.get(Calendar.MINUTE),true).show()
            }

            //Al clicar el +
            ivBtn_AfegirNota.setOnClickListener{

                val stringTitolNota: String = et_TitolNota.text.toString()

                //Si no hi ha cap valor buit
                if (!et_TitolNota.text.isEmpty() && tv_MostraHora.text != "hh:mm" && !Eet_ContingutNota.text.isEmpty()) {

                    val baseDeDades: SQLiteDatabase = helper.readableDatabase

                    //Es guarda el que es vol que surti de la base de dades
                    val resultats = arrayOf(Estructura_BBDD.COL_ID)

                    //Selecciona totes les entrades on l'id sigui igual al seleccionat
                    val seleccio = "${Estructura_BBDD.COL_ID} = ?"
                    val argumentsSeleccio = arrayOf(data+stringTitolNota)

                    //Es fa la consulta a la base de dades
                    val cursor = baseDeDades.query(
                        Estructura_BBDD.TABLE_NAME,
                        resultats,
                        seleccio,
                        argumentsSeleccio,
                        null,
                        null,
                        null
                    )

                    //Selecciona la primera posicio
                    cursor.moveToFirst()

                    //Conta quants resultats ha tingut la consulta
                    val count = cursor.count

                    //Si no hi ha cap resultat
                    if (count == 0) {

                        val baseDeDades: SQLiteDatabase = helper.writableDatabase

                        //Dona els valors corresponents per a insertar a la base de dades
                        val valors = ContentValues().apply {
                            put(Estructura_BBDD.COL_ID,        data+stringTitolNota)
                            put(Estructura_BBDD.COL_DIA,       data)
                            put(Estructura_BBDD.COL_HORA,      tv_MostraHora.text.toString())
                            put(Estructura_BBDD.COL_TITOL,     stringTitolNota)
                            put(Estructura_BBDD.COL_CONTINGUT, Eet_ContingutNota.text.toString())
                        }

                        //Inserta els valors a la base de dades
                        val nova = baseDeDades?.insert(Estructura_BBDD.TABLE_NAME, null, valors)

                        //Notifica que s'han guardat les dades
                        val toast = Toast.makeText(applicationContext, "Dades guardades", Toast.LENGTH_SHORT).show()

                        //Torna a la pantalla principal
                        val i =  Intent(this, MainActivity::class.java)
                        startActivity(i)

                    } else {
                        //Notifica que ja hi ha una nota amb el mateix titol
                        val toast = Toast.makeText(applicationContext, "Titol duplicat", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    //Notifica que hi ha camps buits
                    val toast = Toast.makeText(applicationContext, "Camps buits", Toast.LENGTH_SHORT).show()
                }
            }

            //Al clicar el boto deliminar
            ivBtn_EliminarNota.setOnClickListener{

                //Torna al menu principal
                val i =  Intent(this, MainActivity::class.java)
                startActivity(i)
            }

        } else { //Si vol editar

            //Es guarda l'id de la nota a editar
            idNota = objIntent.getStringExtra("idNota")

            val baseDeDades: SQLiteDatabase = helper.readableDatabase

            //Es guarda el que es vol que surti de la base de dades
            val resultats = arrayOf(Estructura_BBDD.COL_TITOL, Estructura_BBDD.COL_DIA, Estructura_BBDD.COL_HORA,Estructura_BBDD.COL_CONTINGUT)

            //Selecciona totes les entrades on l'id sigui igual al seleccionat
            val seleccio = "${Estructura_BBDD.COL_ID} = ?"
            val argumentsSeleccio = arrayOf(idNota)

            //Es fa la consulta a la base de dades
            val cursor = baseDeDades.query(
                Estructura_BBDD.TABLE_NAME,
                resultats,
                seleccio,
                argumentsSeleccio,
                null,
                null,
                null
            )

            //Selecciona la primera posicio
            cursor.moveToFirst()

            //Escriu dins de cada apartat de text el contingut corresponent
            tv_MostraDia.setText(      cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_DIA)))
            et_TitolNota.setText(      cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_TITOL)))
            tv_MostraHora.setText(     cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_HORA)))
            Eet_ContingutNota.setText( cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_CONTINGUT)))


            //Al clicar el rellotge
            btn_EscollirHora.setOnClickListener {

                val instanciaCalendari = Calendar.getInstance()
                var horaCalendari = instanciaCalendari.get(Calendar.HOUR_OF_DAY)
                var minutsCalendari = instanciaCalendari.get(Calendar.MINUTE)

                //S'escull l'hora i es mostra la seleccionada al lloc pertinent
                val seleccioHora = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    instanciaCalendari.set(Calendar.HOUR_OF_DAY,hourOfDay)
                    instanciaCalendari.set(Calendar.MINUTE,minute)
                    tv_MostraHora.setText(SimpleDateFormat("HH:mm").format(instanciaCalendari.time))
                }

                //Mostra per escollir l'hora
                TimePickerDialog(this,seleccioHora,instanciaCalendari.get(Calendar.HOUR_OF_DAY),instanciaCalendari.get(Calendar.MINUTE),true).show()
            }

            //Al clicar el boto dafegir una nota
            ivBtn_AfegirNota.setOnClickListener{

                val baseDeDades: SQLiteDatabase = helper.writableDatabase

                val titol : String = et_TitolNota.text.toString()

                //Dona valors a
                val valors = ContentValues().apply {
                    put(Estructura_BBDD.COL_TITOL, titol)
                    put(Estructura_BBDD.COL_HORA, tv_MostraHora.text.toString())
                    put(Estructura_BBDD.COL_CONTINGUT, Eet_ContingutNota.text.toString())
                }

                val contar = baseDeDades.update(
                    Estructura_BBDD.TABLE_NAME,
                    valors,
                    seleccio,
                    argumentsSeleccio)

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