package br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.base.Conexao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.TipoClass;

public class TipoDao {

    private Conexao conexao;
    private SQLiteDatabase banco;

    public TipoDao(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long insert(TipoClass tipo){
        ContentValues values = new ContentValues();
        values.put("DESCRICAOT", tipo.getDescricaoT());
        return  banco.insert("tipo", null, values);
    }

    public void atualizar(TipoClass tipo){
        ContentValues values = new ContentValues();
        values.put("DESCRICAOT", tipo.getDescricaoT());
        banco.update("tipo", values, "IDTIPO = ?", new String[]{tipo.getIdTipo().toString()});
    }

    public TipoClass find(Integer id){
        Cursor csr = banco.rawQuery("SELECT * FROM  tipo WHERE IDTIPO = ? ", new String[]{id.toString()});
        while(csr.moveToNext()){
            TipoClass t = new TipoClass();
            t.setDescricaoT(csr.getString(1));
            t.setIdTipo(csr.getInt(0));
            return t;
        }
        return null;
    }

    public List<TipoClass> findList(){
        List<TipoClass> tip = new ArrayList<>();

        Cursor cursor = banco.query("tipo", new String[]{"DESCRICAOT","IDTIPO"},
                null,null,null,null,"IDTIPO "+" DESC");
        while(cursor.moveToNext()){
            TipoClass t = new TipoClass();
            t.setDescricaoT(cursor.getString(0));
            t.setIdTipo(cursor.getInt(1));
            tip.add(t);
        }
        return tip;
    }

    public void delete(TipoClass t){
        banco.delete("tipo", "IDTIPO = ?", new String[]{t.getIdTipo().toString()});
    }
}
