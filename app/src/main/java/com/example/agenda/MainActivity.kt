package com.example.agenda

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.sqliteprova.BBDD_Helper
import com.example.sqliteprova.Estructura_BBDD
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var diaSeleccionat = ""
    val notesXdia = null
    val helper = BBDD_Helper(this)

    val lineaSeparacio = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    val lp = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    /*
    private fun setAlarm(i: Int, timestamp: Long, ctx: Context) {
        val alarmManager =
            ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(ctx, AlarmReceiver::class.java)
        val pendingIntent: PendingIntent
        pendingIntent = PendingIntent.getBroadcast(
            ctx, i, alarmIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        alarmIntent.data = Uri.parse("custom://" + System.currentTimeMillis())
        alarmManager[AlarmManager.RTC_WAKEUP, timestamp] = pendingIntent
    }
    */

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        //Pantalla completa
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_crear_nota)
        setContentView(R.layout.activity_main)

        //menu per eliminar tots els registres

        val helper = BBDD_Helper(this)
        val baseDseDades: SQLiteDatabase = helper.readableDatabase

        //Al clicar el botó del menu
        opcions.setOnClickListener {

            val popupMenu = PopupMenu(this, it)

            //Al clicar el menu popup
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {

                    R.id.menuDades -> {
                        //Vista de totes les notes
                        val i =  Intent(this, TotesNotes::class.java)
                        startActivity(i)
                        true
                    }

                    //Al clicar l'opció "menuBorrar"
                    R.id.menuBorrar -> {

                        //Es mostra un quadre de text preguntant si vol eliminar totes les dades
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Eliminar les dades")
                        builder.setMessage("Estas segur que vols eliminar totes les dades?")

                        //Hi ha l'opcio daceptar, que al clicarla s'eliminen totes les dades
                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            baseDseDades.delete(Estructura_BBDD.TABLE_NAME, null, null);
                            var toast = Toast.makeText(this, "Totes les dades han estat eliminades", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, android.R.string.yes, Toast.LENGTH_SHORT).show()
                        }

                        //Hi ha l'opcio de cancelar, que no fa res
                        builder.setNegativeButton(android.R.string.no) { dialog, which ->
                            Toast.makeText(applicationContext, android.R.string.no, Toast.LENGTH_SHORT).show()
                        }

                        builder.show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.opcionsmenu)
            popupMenu.show()
        }
        //menu per eliminar tots els registres

        //Al clicar el boto dafegir una nota
        btn_AfegirNota.setOnClickListener {

            //Si hi ha un dia seleccionat
            if (!diaSeleccionat.equals("")) {

                //Va a crear la nota, passant-li que voldrà crear, i la data seleccionada
                val i = Intent(this, CrearNota::class.java)

                i.putExtra("donve", "crear")
                i.putExtra("data", diaSeleccionat)

                startActivity(i)

            } else {

                //Quan no hi ha un dia seleccionat, notifica de l'error
                var toast = Toast.makeText(this, "Selecciona un dia", Toast.LENGTH_SHORT).show()
            }
        }

        val instanciaCalendari = Calendar.getInstance()
        val anyCalendari = instanciaCalendari.get(Calendar.YEAR)
        val mesCalendari = instanciaCalendari.get(Calendar.MONTH)
        val diaCalendari = instanciaCalendari.get(Calendar.DAY_OF_WEEK)

        val baseDeDades: SQLiteDatabase = helper.readableDatabase

        //Al seleccionar una altre dat del calendari
        Calendari.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->

            //Per a seleccionar només un
            if (scrollBotons.childCount > 0)
                scrollBotons.removeAllViews();

            //Es guarda dins l variable la data en tipus string
            diaSeleccionat =
                dayOfMonth.toString() + "/" + (month + 1).toString() + "/" + year.toString()

            //Es guarda el que es vol que surti de la base de dades
            val resultats = arrayOf(Estructura_BBDD.COL_TITOL, Estructura_BBDD.COL_HORA)

            //Selecciona totes les entrades on el dia sigui igual al seleccionat
            val selecio = "${Estructura_BBDD.COL_DIA} = ?"
            val argumentsSeleccio = arrayOf(diaSeleccionat)

            //Es fa la consulta a la base de dades
            val cursor = baseDeDades.query(
                Estructura_BBDD.TABLE_NAME, //De la base de dades
                resultats,                  //Agafa el titol i l'hora
                selecio,                    //On el dia sigui igual
                argumentsSeleccio,          //Al dia seleccionat
                null,
                null,
                null
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

                //Per a cada valor guardat a l'array "hores"
                for (x in 0..hores.size - 1) {

                    //Crea una linea de separacio entre notes
                    val separacio = LinearLayout(this)
                    separacio.layoutParams = lineaSeparacio
                    separacio.setBackgroundColor(Color.BLACK)
                    scrollBotons.addView(separacio)

                    //Crea un botó amb el tiitol i l'hora de la nota
                    val lineaDeNota = Button(this)
                    lineaDeNota.layoutParams = lp
                    lineaDeNota.setBackgroundColor(Color.parseColor("#c4eada"))
                    lineaDeNota.text = titols.get(x) + "   |   " + hores.get(x)

                    //Al clicar el boto de la nota que te aquell dia
                    lineaDeNota.setOnClickListener {

                        //Va a la vista de les notes, passant-li el valor de l'id en la base de dades
                        val i = Intent(this, VistaNotes::class.java)
                        i.putExtra("data", diaSeleccionat + titols.get(x))
                        startActivity(i)
                    }

                    scrollBotons.addView(lineaDeNota)
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

                scrollBotons.addView(noResultat)
            }
        })
    }
}