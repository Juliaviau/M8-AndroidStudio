package com.example.agenda

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        setContentView(R.layout.activity_crear_nota)

        setContentView(R.layout.activity_vista_notes)

        val objIntent: Intent=intent
        idNota = objIntent.getStringExtra("data")

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

        tv_MostrarDia.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_DIA)))
        tv_TitolNota.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_TITOL)))
        tv_MostrarHora.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_HORA)))
        tv_MostrarContingutNota.setText(cursor.getString(cursor.getColumnIndex(Estructura_BBDD.COL_CONTINGUT)))

        ivBtn_TornarEnrere.setOnClickListener{
            finish()
            /*val i =  Intent(this, MainActivity::class.java)
            startActivity(i)*/
        }
        btn_EliminarNota.setOnClickListener{

            val selection = "${Estructura_BBDD.COL_ID} LIKE ?"
            val selectionArgs = arrayOf(idNota)

            val deletedRows = db.delete(Estructura_BBDD.TABLE_NAME, selection, selectionArgs)
            var toast = Toast.makeText(this, "Nota eliminada correctament", Toast.LENGTH_SHORT).show()

            val i =  Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        btn_EditarNota.setOnClickListener {
            val i =  Intent(this, CrearNota::class.java)
            i.putExtra("donve" , "editar")
            i.putExtra("idNota", idNota)
            i.putExtra("data"  , idNota)
            startActivity(i)
        }
    }
}