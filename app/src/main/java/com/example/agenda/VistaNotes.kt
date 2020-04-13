package com.example.agenda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_vista_notes.*

class VistaNotes : AppCompatActivity() {

    var data: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_notes)

        val objIntent: Intent=intent
        data = objIntent.getStringExtra("data")

        tv_dia.setText(data)

        imageView.setOnClickListener{
            val i =  Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        eliminarNota.setOnClickListener{
            val i =  Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        editarNota.setOnClickListener {
            val i =  Intent(this, CrearNota::class.java)
            i.putExtra("data",data)
            startActivity(i)
        }
    }
}