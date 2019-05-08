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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.ContasDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ContasClass;

public class ListaContasActivity extends AppCompatActivity {

    private Button botaoRetornar;
    private ListView listView;
    private List<ContasClass> listaDescricao;
    private List<ContasClass> listaFiltrada = new ArrayList<>();

    private ContasDao contasDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contas);

        botaoRetornar = findViewById(R.id.botaoRetornarListaContasId);
        listView = findViewById(R.id.listaRendimentoId);

        contasDao = new ContasDao(this);

        listaDescricao = contasDao.findList();
        listaFiltrada.addAll(listaDescricao);
        ArrayAdapter<ContasClass> adaptador = new ArrayAdapter<ContasClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaFiltrada);
        listView.setAdapter(adaptador);
        registerForContextMenu(listView);

        botaoRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListaContasActivity.this, ReferenciasActivity.class));
                finish();
            }
        });
    }

    public void atualizar(final int posicao){
        ContasClass contas = listaDescricao.get(posicao);
        Intent it = new Intent(this,ContasActivity.class );
        it.putExtra("contas", contas);
        startActivity(it);
        Toast.makeText(ListaContasActivity.this,"Conta selecionada para atualizar  ", Toast.LENGTH_SHORT).show();
    }

    public void deletar(final int posicao){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("ATENÇÃO")
                .setMessage("Realmente deseja excluir a Conta?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContasClass contas = listaDescricao.get(posicao);
                        Toast.makeText(ListaContasActivity.this,"Conta excluida com sucesso  ", Toast.LENGTH_SHORT).show();
                        listaFiltrada.remove(contas);
                        listaDescricao.remove(contas);
                        contasDao.delete(contas);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto_contas, menu);
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
        for(ContasClass a: listaDescricao ){
            if(a.getDescricaoC().toLowerCase().contains(ref.toLowerCase())){
                listaFiltrada.add(a);
            }
        }
        listView.invalidateViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        listaDescricao = contasDao.findList();
        listaFiltrada.clear();
        listaFiltrada.addAll(listaDescricao);
        listView.invalidateViews();
    }
}
