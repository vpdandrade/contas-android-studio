package br.com.vg.controlededespesas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.TipoDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.TipoClass;


public class TiposActivity extends AppCompatActivity {

    private EditText tipo;
    private Button salvarTipo;
    private ListView listViewTipos;
    private List<TipoClass> listDescricaoTipo;
    private List<TipoClass> listaFiltradaTipo = new ArrayList<>();
    private TipoClass tip;

   private TipoDao tipoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos);

        tipo = findViewById(R.id.textTipoId);
        salvarTipo = findViewById(R.id.addTipoId);

        listViewTipos = findViewById(R.id.listTiposId);
        tipoDao = new TipoDao(this);

        listDescricaoTipo = tipoDao.findList();
        listaFiltradaTipo.addAll(listDescricaoTipo);
        ArrayAdapter<TipoClass> adaptador = new ArrayAdapter<TipoClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaFiltradaTipo);
        listViewTipos.setAdapter(adaptador);
        registerForContextMenu(listViewTipos);

        Intent it = getIntent();
        if(it.hasExtra("tipo")){
            tip = (TipoClass) it.getSerializableExtra("tipo");
            tipo.setText(tip.getDescricaoT());
        }
    }

    public void save(View view) {
        if(tip == null){
            TipoClass tip = new TipoClass();
            tip.setDescricaoT(tipo.getText().toString());
            long id = tipoDao.insert(tip);
            tip.setIdTipo((int) id); //Devolvido ID ao objeto criado
            Toast.makeText(this,"Tipo incluído com sucesso! ", Toast.LENGTH_LONG).show();

            startActivity( new Intent(TiposActivity.this, TiposActivity.class));
            finish();
        }else{
            tip.setDescricaoT(tipo.getText().toString());
            tipoDao.atualizar(tip);
            startActivity( new Intent(TiposActivity.this, TiposActivity.class));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //https://stackoverflow.com/questions/17689315/how-to-get-view-in-oncontextitemselected-event
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int posicao = info.position; //Pega elemento na lista que foi clicado
        int opcao =item.getItemId(); //Saber qual o id da opção clicada. Foi necessário colocar IDs para os itens.

        if (opcao == R.id.item_excluir) {
            Toast.makeText(this, "menu clicado:  Elemento para excluir na posição: " + posicao, Toast.LENGTH_SHORT).show();
            deletar(posicao);
            return super.onContextItemSelected(item);
        }else{
            atualizar(posicao);
            return super.onContextItemSelected(item);
        }
    }

    public void atualizar(final int posicao){
        TipoClass tip = listDescricaoTipo.get(posicao);
        Intent it = new Intent(this, TiposActivity.class );
        it.putExtra("tipo", tip);
        startActivity(it);
        Toast.makeText(TiposActivity.this,"Lista de tipos atualizado!  ", Toast.LENGTH_SHORT).show();

    }

    public void deletar(final int posicao){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("ATENÇÃO")
                .setMessage("Realmente deseja excluir o tipo de conta?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TipoClass tip = listDescricaoTipo.get(posicao);
                        Toast.makeText(TiposActivity.this,"Tipo excluído com sucesso  ", Toast.LENGTH_SHORT).show();
                        listaFiltradaTipo.remove(tip);
                        listDescricaoTipo.remove(tip);
                        tipoDao.delete(tip);
                        listViewTipos.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_busca_referencia, menu);
        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                findReference(s);
                return false;
            }
        });
        return true;
    }

    public void findReference(String tip){
        listaFiltradaTipo.clear();
        for(TipoClass b: listDescricaoTipo){
            if(b.getDescricaoT().toLowerCase().contains(tip.toLowerCase())){
                listaFiltradaTipo.add(b);
            }
        }
        listViewTipos.invalidateViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            listDescricaoTipo = tipoDao.findList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaFiltradaTipo.clear();
        listaFiltradaTipo.addAll(listDescricaoTipo);
        listViewTipos.invalidateViews();
    }
}