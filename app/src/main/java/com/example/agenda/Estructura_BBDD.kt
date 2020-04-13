package com.example.sqliteprova

object Estructura_BBDD {
    /* Inner class that defines the table contents */ //public static class FeedEntry implements BaseColumns {
    const val TABLE_NAME = "GuardarLesNotes"
    const val COL_ID = "id"
    const val COL_DIA = "dia"
    const val COL_HORA = "hora"
    const val COL_TITOL = "titol"
    const val COL_CONTINGUT = "contingut"

    //}
    const val SQL_CREAR_TAULA = "CREATE TABLE " + TABLE_NAME + " (" +
            COL_ID + " TEXT PRIMARY KEY," +
            COL_CONTINGUT + " TEXT," +
            COL_TITOL + " TEXT," +
            COL_DIA + " DATE," +
            COL_HORA + " TIME)"

    const val SQL_ELIMINAR_TAULA = "DROP TABLE IF EXISTS $TABLE_NAME"
}