package br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.base.Conexao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.RendimentoClass;

public class RendimentoDao {

    private Conexao conexao;

    private SQLiteDatabase banco;

    public RendimentoDao (Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();
    }

    public long insert(RendimentoClass rendimento){
        ContentValues values = new ContentValues();
        values.put("DESCRICAOR", rendimento.getDescricaoRR());
        values.put("VALOR", rendimento.getValorRend());
        values.put("ID_REFERENCIA", String.valueOf(rendimento.getReferencia().getIdReferencia()));
      return  banco.insert("rendimento", null, values);
    }

    public void atualizar(RendimentoClass rendimento){
        ContentValues values = new ContentValues();
        values.put("DESCRICAOR", rendimento.getDescricaoRR());
        values.put("ID_REFERENCIA", String.valueOf(rendimento.getReferencia().getIdReferencia()));
         banco.update("rendimento", values, "IDRENDIMENTO = ?", new String[]{rendimento.getIdRendimento().toString()});
    }

    public List<RendimentoClass> findList(){
        List<RendimentoClass> rend = new ArrayList<>();

        Cursor cursor = banco.query("rendimento", new String[]{"DESCRICAOR","VALOR","IDRENDIMENTO"},
                null,null,null,null,"IDRENDIMENTO "+" DESC");
        while(cursor.moveToNext()){
            RendimentoClass r = new RendimentoClass();
            r.setDescricaoRR(cursor.getString(0));
            r.setValorRend(cursor.getDouble(1));
            r.setIdRendimento(cursor.getInt(2));
            rend.add(r);
        }
        return rend;
    }
    public void delete(RendimentoClass r){
         banco.delete("rendimento", "IDRENDIMENTO = ?", new String[]{r.getIdRendimento().toString()});

    }

}
