package br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.res.ConfigurationHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.base.Conexao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ReferenciaClass;

public class ReferenciaDao {

    private Conexao conexao;

    private SQLiteDatabase banco;

    public ReferenciaDao(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }
    public long insert(ReferenciaClass referencia){
        ContentValues values = new ContentValues();
        values.put("DESCRICAOF", referencia.getDescricaoR());
        return banco.insert("referencia", null, values);
    }

    public List<ReferenciaClass> findList(){
        List<ReferenciaClass> ref = new ArrayList<>();
        Cursor cursor = banco.query("referencia", new String[]{"DESCRICAOF"},
                null,null,null,null,"IDREFERENCIA "+" DESC");
        while(cursor.moveToNext()){
            ReferenciaClass r = new ReferenciaClass();
            r.setDescricaoR(cursor.getString(0));
            ref.add(r);
        }
        return ref;
    }
    public Integer delete(ReferenciaClass r){
        return banco.delete("referencia", "idReferencia = ?", new String[]{r.getIdReferencia().toString()});

    }
}
