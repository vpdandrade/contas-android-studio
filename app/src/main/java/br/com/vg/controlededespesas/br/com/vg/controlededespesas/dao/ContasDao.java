package br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.base.Conexao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ContasClass;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.TipoClass;

public class ContasDao {

    private Conexao conexao;
    private SQLiteDatabase db;
    private TipoDao tipoDao;
    private ReferenciaDao referenciaDao;

    public ContasDao(Context context){
        conexao = new Conexao(context);
        tipoDao = new TipoDao(context);
        referenciaDao = new ReferenciaDao(context);
        db = conexao.getWritableDatabase();
    }

    public long insert(ContasClass contas){
        ContentValues values = new ContentValues();
        values.put("DESCRICAOC", contas.getDescricaoC());
        values.put("PAGO", contas.getPago());
        values.put("VALOR", contas.getValor());
        values.put("ID_REFERENCIA", String.valueOf(contas.getReferencia().getIdReferencia()));
        values.put("ID_TIPO", String.valueOf(contas.getTipo().getIdTipo()));
        return db.insert("CONTAS", "PAGO" , values);
    }

    public void atualizar(ContasClass contas) {
        ContentValues values = new ContentValues();
        values.put("DESCRICAOC", contas.getDescricaoC());
        values.put("PAGO", contas.getPago());
        values.put("VALOR", contas.getValor());
        values.put("ID_REFERENCIA", String.valueOf(contas.getReferencia().getIdReferencia()));
        values.put("ID_TIPO", String.valueOf(contas.getTipo().getIdTipo()));
        db.update("CONTAS", values, "IDCONTAS = ?", new String[]{contas.getIdContas().toString()});
    }

    public List<ContasClass> findList(){
        List<ContasClass> conta = new ArrayList<>();

        Cursor cursor = db.query("CONTAS", new String[]{"DESCRICAOC","VALOR","IDCONTAS","ID_TIPO"},
                null,null,null,null,"IDCONTAS "+" DESC");
        while(cursor.moveToNext()){
            TipoClass tipo = tipoDao.find((cursor.getInt(3)));
            ContasClass c = new ContasClass();
            c.setDescricaoC(cursor.getString(0));
            c.setValor(cursor.getDouble(1));
            c.setIdContas(cursor.getInt(2));
            c.setTipo(tipo);
            conta.add(c);
        }
        return conta;
    }

    public void delete(ContasClass c){
        db.delete("CONTAS", "IDCONTAS = ?", new String[]{c.getIdContas().toString()});
    }
}
