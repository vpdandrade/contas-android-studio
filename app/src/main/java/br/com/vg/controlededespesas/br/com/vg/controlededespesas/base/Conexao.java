package br.com.vg.controlededespesas.br.com.vg.controlededespesas.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    private static final String name = "banco.db";

    private static final int version = 5;

    public Conexao(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE REFERENCIA(  \n" +
                "\tIDREFERENCIA INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tDESCRICAOF VARCHAR(100) NOT NULL\n" +
                ");");

        db.execSQL("CREATE TABLE TIPO(\n" +
                "\tIDTIPO INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tDESCRICAOT VARCHAR(100) NOT NULL\n" +
                " );");

        db.execSQL("CREATE TABLE CONTAS(\n" +
                "\tIDCONTAS INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tDESCRICAOC VARCHAR(100) NOT NULL,\n" +
                "\tPAGO BOOLEAN,\n" +
                "\tVALOR DOUBLE (10,2) NOT NULL,\n" +
                "\tID_REFERENCIA INT,\n" +
                "\tID_TIPO INT,\n" +
                "\tFOREIGN KEY (ID_REFERENCIA) REFERENCES REFERENCIA(IDREFERENCIA),\n" +
                "\tFOREIGN KEY (ID_TIPO) REFERENCES TIPO(IDTIPO)\n" +
                "\t\n" +
                ");");

        db.execSQL("CREATE TABLE RENDIMENTO(\n" +
                "\tIDRENDIMENTO INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tDESCRICAOR VARCHAR(100) NOT NULL,\n" +
                "\tVALOR DOUBLE (10,2) NOT NULL,\n" +
                "\tID_REFERENCIA INT,\n" +
                "\tFOREIGN KEY (ID_REFERENCIA) REFERENCES REFERENCIA(IDREFERENCIA)\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                 db.execSQL("drop table REFERENCIA;");
                 db.execSQL("drop table TIPO;");
                 db.execSQL("drop table CONTAS;");
                 db.execSQL("drop table RENDIMENTO;");
                 onCreate(db);
    }
}
