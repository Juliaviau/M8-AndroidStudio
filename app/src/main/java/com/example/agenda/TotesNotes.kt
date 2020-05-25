package com.example.agenda

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sqliteprova.BBDD_Helper
import com.example.sqliteprova.Estructura_BBDD
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_totes_notes.*
import kotlinx.android.synthetic.main.activity_totes_notes.ivBtn_TornarEnrere
import kotlinx.android.synthetic.main.activity_vista_notes.*

class TotesNotes : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_totes_notes)

        val helper = BBDD_Helper(this)
        val baseDeDades: SQLiteDatabase = helper.readableDatabase

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        //Per a seleccionar només un
        if (scrollNotes.childCount > 0)
            scrollNotes.removeAllViews();

        //Es guarda el que es vol que surti de la base de dades
        val resultats = arrayOf(Estructura_BBDD.COL_TITOL, Estructura_BBDD.COL_HORA,  Estructura_BBDD.COL_ID,  Estructura_BBDD.COL_DIA)

        //Es fa la consulta a la base de dades DE TOTES les dades
        val cursor = baseDeDades.query(
            Estructura_BBDD.TABLE_NAME, //De la base de dades
            resultats,                  //Agafa el titol,dia i l'hora
            null,
            null,
            null,
            null,
            Estructura_BBDD.COL_DIA +" DESC"
        )

        //Selecciona la primera posicio
        cursor.moveToFirst()

        //Si loa consulta ha tingut resultats
        if (cursor.count > 0) {

            //Guarda a titols tots els titols de la base de dades que han sortit de la consulta
            val titols = ArrayList<String>()
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val titol =
                        cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_TITOL))
                    titols.add(titol)
                    cursor.moveToNext()
                }
            }

            //Guarda a hores totes les hores de la base de dades que han sortit de la consulta
            val hores = ArrayList<String>()
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val hora = cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_HORA))
                    hores.add(hora)
                    cursor.moveToNext()
                }
            }

            //Guarda a dies tots els dies de la base de dades que han sortit de la consulta
            val dies = ArrayList<String>()
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val dia = cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_DIA))
                    dies.add(dia)
                    cursor.moveToNext()
                }
            }

            //Guarda a ids tots els id de la base de dades que han sortit de la consulta
            val ids = ArrayList<String>()
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast) {
                    val id =
                        cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_ID))
                    ids.add(id)
                    cursor.moveToNext()
                }
            }

            //Per a cada valor guardat a l'array "hores"
            for (x in 0..hores.size - 1) {

                //Crea un botó amb el tiitol i l'hora de la nota
                val lineaDeNota = Button(this)
                lineaDeNota.layoutParams = lp
                lineaDeNota.setBackgroundColor(Color.parseColor("#fcf3ca"))
                lineaDeNota.text = titols.get(x) + "   |   " + dies.get(x) + "   |   " + hores.get(x)

                //Al clicar el boto de la nota que te aquell dia
                lineaDeNota.setOnClickListener {

                    //Va a la vista de les notes, passant-li el valor de l'id en la base de dades
                    val i = Intent(this, VistaNotes::class.java)
                    i.putExtra("data", ids.get(x).toString()) //possible error
                    startActivity(i)
                }

                scrollNotes.addView(lineaDeNota)
            }

        } else { //Si no hi ha hagut cap valor resultant en la consulta

            //Es crea un text view notificant que no hi ha cap nota per aquell dia
            val noResultat = TextView(this)
            noResultat.layoutParams = lp
            noResultat.setBackgroundColor(Color.GRAY)
            noResultat.setPadding(0, 308, 0, 308)
            noResultat.setTextColor(Color.WHITE)
            noResultat.setGravity(Gravity.CENTER or Gravity.BOTTOM)
            noResultat.text = "No hi ha cap nota per aquest dia"
            noResultat.textSize = 20f

            scrollNotes.addView(noResultat)
        }

        //Al clicar el boto per anar enrere, torna a la pantalla que estava abans
        ivBtn_TornarEnrere.setOnClickListener{
            finish()
        }
    }
}
