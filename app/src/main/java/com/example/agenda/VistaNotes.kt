package com.example.agenda

import android.app.AlertDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.sqliteprova.BBDD_Helper
import com.example.sqliteprova.Estructura_BBDD
import kotlinx.android.synthetic.main.activity_vista_notes.*

class VistaNotes : AppCompatActivity() {

    var idNota: String = ""
    val helper = BBDD_Helper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_crear_nota)

        setContentView(R.layout.activity_vista_notes)

        //Rep l'id de la nota a veure i el guarda a idNota
        val objIntent: Intent=intent
        idNota = objIntent.getStringExtra("data")

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
        tv_MostrarDia.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_DIA)))
        tv_TitolNota.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_TITOL)))
        tv_MostrarHora.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_HORA)))
        tv_MostrarContingutNota.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_CONTINGUT)))

        //Al clicar el boto per anar enrere, torna a la pantalla que estava abans
        ivBtn_TornarEnrere.setOnClickListener{
            finish()
        }

        //Al clicar el boto d'eliminar la nota
        btn_EliminarNota.setOnClickListener{

            //Es mostra un quadre de text preguntant si vol eliminar totes les dades
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar la nota")
            builder.setMessage("Estas segur que vols eliminar la nota?")

            //Hi ha l'opcio daceptar, que al clicarla s'eliminen totes les dades
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                //Selecciona totes les entrades on l'id sigui igual al seleccionat
                val seleccio = "${Estructura_BBDD.COL_ID} LIKE ?"
                val argumentsSeleccio = arrayOf(idNota)

                //Elimina la nota de la base de dades
                val filesEliminades = baseDeDades.delete(Estructura_BBDD.TABLE_NAME, seleccio, argumentsSeleccio)

                //Notifica que s'ha eliminat la nota
                var toast = Toast.makeText(this, "Nota eliminada correctament", Toast.LENGTH_SHORT).show()

                //Es torna a l'inici
                val i =  Intent(this, MainActivity::class.java)
                startActivity(i)
            }

            //Hi ha l'opcio de cancelar, que no fa res
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(applicationContext, android.R.string.no, Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }

        //Al clicar el boto del llapis, es va a la pantalla de crear nota. Dient-li que vol editar. I passant-li l'id de la nota
        btn_EditarNota.setOnClickListener {
            val i =  Intent(this, CrearNota::class.java)
            i.putExtra("donve" , "editar")
            i.putExtra("idNota", idNota)
            i.putExtra("data"  , idNota)
            startActivity(i)
        }
    }
}