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

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.RendimentoDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.RendimentoClass;

public class ListaRendimentoActivity extends AppCompatActivity {

            private Button botaoRetornar;
            private ListView listView;
            private List<RendimentoClass> listaDescricao;
            private List<RendimentoClass> listaFiltrada = new ArrayList<>();

            private RendimentoDao rendimentoDao;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_lista_rendimento);

                listView = findViewById(R.id.listaRendimentoId);
                botaoRetornar = findViewById(R.id.botaoRetornarListaRendId);

                rendimentoDao = new RendimentoDao(this);

                listaDescricao = rendimentoDao.findList();
                listaFiltrada.addAll(listaDescricao);
                ArrayAdapter<RendimentoClass> adaptador = new ArrayAdapter<RendimentoClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaFiltrada);
                listView.setAdapter(adaptador);
                registerForContextMenu(listView);

                botaoRetornar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ListaRendimentoActivity.this, Rendimento.class));
                        finish();

                    }
                });
                }
     public void atualizar(final int posicao){
         RendimentoClass contas = listaDescricao.get(posicao);
        Intent it = new Intent(this,Rendimento.class );
        it.putExtra("contas", contas);
        startActivity(it);
        Toast.makeText(ListaRendimentoActivity.this,"Rendimento selecionado para atualizar  ", Toast.LENGTH_SHORT).show();
    }

    public void deletar(final int posicao){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("ATENÇÃO")
                .setMessage("Realmente deseja excluir o rendimeto?")
                .setNegativeButton("NÃO",null)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RendimentoClass rendimentoClass = listaDescricao.get(posicao);
                        Toast.makeText(ListaRendimentoActivity.this,"Rendimento excluido com sucesso  ", Toast.LENGTH_SHORT).show();
                        listaFiltrada.remove(rendimentoClass);
                        listaDescricao.remove(rendimentoClass);
                        rendimentoDao.delete(rendimentoClass);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
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
        for(RendimentoClass a: listaDescricao ){
            if(a.getDescricaoRR().toLowerCase().contains(ref.toLowerCase())){
                listaFiltrada.add(a);
            }
        }
        listView.invalidateViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        listaDescricao = rendimentoDao.findList();
        listaFiltrada.clear();
        listaFiltrada.addAll(listaDescricao);
        listView.invalidateViews();
    }
}


