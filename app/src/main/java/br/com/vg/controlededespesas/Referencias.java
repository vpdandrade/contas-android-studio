package br.com.vg.controlededespesas;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.ContasActivity;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.ReferenciaDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ReferenciaClass;

public class Referencias extends AppCompatActivity {

    private EditText referencia;
    private Button salvarReferencia;
    private ListView listView;
    private List<ReferenciaClass> listDescricao;
    private List<ReferenciaClass> listaFiltrada = new ArrayList<>();


    private ReferenciaDao referenciaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referencias);

        referencia = findViewById(R.id.textReferenciaId);
        salvarReferencia = findViewById(R.id.BotaoAdReferenciaId);

        listView = findViewById(R.id.listReferenciaId);
        referenciaDao = new ReferenciaDao(this);

        listDescricao = referenciaDao.findList();
        listaFiltrada.addAll(listDescricao);
        ArrayAdapter<ReferenciaClass> adaptador = new ArrayAdapter<ReferenciaClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaFiltrada);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            ReferenciaClass r = new ReferenciaClass();
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ReferenciaClass ref = listaFiltrada.get(position);
                // parei aqui.... Vini
                startActivity( new Intent(Referencias.this, ContasActivity.class));
            }
        });

    }

    public void save(View view){
        ReferenciaClass ref = new ReferenciaClass();
        ref.setDescricaoR(referencia.getText().toString());
        long id = referenciaDao.insert(ref);
        Toast.makeText(this,"Referencia inserida com id: " + id, Toast.LENGTH_LONG).show();

         startActivity( new Intent(Referencias.this, Referencias.class));
    }

    public void deletar(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final ReferenciaClass refDelete = listaFiltrada.get(menuInfo.position);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja excluir a Referencia?")
                .setNegativeButton("Não",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listaFiltrada.remove(refDelete);
                        listDescricao.remove(refDelete);
                        referenciaDao.delete(refDelete);
                        listView.invalidateViews();
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
         super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater i = getMenuInflater();
         i.inflate(R.menu.menu_contexto, menu);
    }

    public void findReference(String ref){
        listaFiltrada.clear();
        for(ReferenciaClass a: listDescricao ){
            if(a.getDescricaoR().toLowerCase().contains(ref.toLowerCase())){
                listaFiltrada.add(a);
            }
        }
        listView.invalidateViews();
    }

       @Override
    public void onResume(){
        super.onResume();
        listDescricao = referenciaDao.findList();
        listaFiltrada.clear();
        listaFiltrada.addAll(listDescricao);
        listView.invalidateViews();

    }



}
