package br.com.vg.controlededespesas;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

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

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.ReferenciaDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ReferenciaClass;

public class ReferenciasActivity extends AppCompatActivity {

    private EditText referencia;
    private Button salvarReferencia;
    private Button encaminharActConta;
    private Button encaminharActTipo;
    private Button encaminharRendimento;
    private ListView listView;
    private List<ReferenciaClass> listDescricao;
    private List<ReferenciaClass> listaFiltrada = new ArrayList<>();
    private ReferenciaClass ref;

    private ReferenciaDao referenciaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referencias);

        referencia = findViewById(R.id.textReferenciaId);
        salvarReferencia = findViewById(R.id.BotaoAdReferenciaId);
        encaminharActConta = findViewById(R.id.BotaoEncaminharContaId);
        encaminharActTipo = findViewById(R.id.BotaoEncaminharTipoId);
        encaminharRendimento = findViewById(R.id.botaoEncaminharAcRendimentoId);

        listView = findViewById(R.id.listReferenciaId);
        referenciaDao = new ReferenciaDao(this);

        listDescricao = referenciaDao.findList();
        listaFiltrada.addAll(listDescricao);
        ArrayAdapter<ReferenciaClass> adaptador = new ArrayAdapter<ReferenciaClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaFiltrada);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);

        Intent it = getIntent();
        if(it.hasExtra("referencia")){
            ref = (ReferenciaClass) it.getSerializableExtra("referencia");
            referencia.setText(ref.getDescricaoR());

        }

        encaminharActConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReferenciasActivity.this, ContasActivity.class));
            }
        });

        encaminharActTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReferenciasActivity.this, TiposActivity.class));

            }
        });

        encaminharRendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReferenciasActivity.this, Rendimento.class));

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ReferenciaClass ref = listaFiltrada.get(position);
                startActivity(new Intent(ReferenciasActivity.this, ReferenciasActivity.class));
            }
        });
    }

    public void save(View view) {
        if(ref == null){
        ReferenciaClass ref = new ReferenciaClass();
        ref.setDescricaoR(referencia.getText().toString());
        long id = referenciaDao.insert(ref);
        ref.setIdReferencia((int) id); //Devolvido ID ao objeto criado
        Toast.makeText(this,"Referencia incluida com sucesso " + id, Toast.LENGTH_LONG).show();

        startActivity( new Intent(ReferenciasActivity.this, ReferenciasActivity.class));
        finish();

        }else{
           ref.setDescricaoR(referencia.getText().toString());
           referenciaDao.atualizar(ref);
            startActivity( new Intent(ReferenciasActivity.this, ReferenciasActivity.class));
            finish();
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
            Toast.makeText(this, "menu clicado:  Elemento para excluir na posição: ", Toast.LENGTH_SHORT).show();
            deletar(posicao);
            return super.onContextItemSelected(item);
        }else{
            atualizar(posicao);
            return super.onContextItemSelected(item);
        }
    }

    public void atualizar(final int posicao){
        ReferenciaClass ref = listDescricao.get(posicao);
        Intent it = new Intent(this, ReferenciasActivity.class );
        it.putExtra("referencia", ref);
        startActivity(it);
        Toast.makeText(ReferenciasActivity.this,"Referencia atualizada com sucesso:  ", Toast.LENGTH_SHORT).show();
    }

    public void deletar(final int posicao){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("ATENÇÃO")
                .setMessage("Realmente deseja excluir a Referencia?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReferenciaClass ref = listDescricao.get(posicao);
                        Toast.makeText(ReferenciasActivity.this,"Referencia excluida com sucesso:  ", Toast.LENGTH_SHORT).show();
                        listaFiltrada.remove(ref);
                        listDescricao.remove(ref);
                        referenciaDao.delete(ref);
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
