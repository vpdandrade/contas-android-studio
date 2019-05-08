package br.com.vg.controlededespesas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.ContasDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.ReferenciaDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.TipoDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ContasClass;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ReferenciaClass;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.TipoClass;

public class ContasActivity extends AppCompatActivity {

    private EditText descricaoRecebida;
    private EditText valorRecebido;
    private ListView listaTipo;
    private ListView listaReferencia;
    private Button botaSalvarConta;
    private Button botaoIrAcContas;

    private List<ReferenciaClass> listaDescricao;
    private List<ReferenciaClass> listaReferenciaFiltrada = new ArrayList<>();

    private List<TipoClass> listaDescricaoTipo;
    private List<TipoClass> listaTipoFiltrada = new ArrayList<>();

    private ContasDao contasDao;
    private ReferenciaDao referenciaDao;
    private TipoDao tipoDao;

    private ContasClass conta;
    private Integer tipoSelecionado;
    private Context context;
    private Integer referenciaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas);

        descricaoRecebida = findViewById(R.id.textoDescricaoContaId);
        valorRecebido = findViewById(R.id.textValorContaId);
        botaSalvarConta = findViewById(R.id.salvarContaId);
        botaoIrAcContas = findViewById(R.id.botaoContasIrAcId);

        contasDao = new ContasDao(this);

        listaTipo = findViewById(R.id.listaTipoContaId);
        tipoDao = new TipoDao(this);
        listaDescricaoTipo = tipoDao.findList();
        listaTipoFiltrada.addAll(listaDescricaoTipo);
        ArrayAdapter<TipoClass> adaptador1 = new ArrayAdapter<TipoClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaTipoFiltrada);
        listaTipo.setAdapter(adaptador1);

        listaReferencia = findViewById(R.id.listaReferenciaContaId);
        referenciaDao = new ReferenciaDao(this);
        listaDescricao = referenciaDao.findList();
        listaReferenciaFiltrada.addAll(listaDescricao);
        ArrayAdapter<ReferenciaClass> adaptador = new ArrayAdapter<ReferenciaClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaReferenciaFiltrada);
        listaReferencia.setAdapter(adaptador);

        context = this;
        listaTipo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 TipoClass tipo = listaDescricaoTipo.get(position);
                tipoSelecionado = tipo.getIdTipo();
                String nome = tipo.getDescricaoT();
                Toast.makeText(context,"Selecionado Tipo: " + nome, Toast.LENGTH_SHORT).show();
        }

        });

        listaReferencia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReferenciaClass ref = listaDescricao.get(position);
                referenciaSelecionada = ref.getIdReferencia();
                String nome = ref.getDescricaoR();
                Toast.makeText(context,"Selecionado mês: " + nome, Toast.LENGTH_SHORT).show();
            }
        });

        Intent it = getIntent();
        if(it.hasExtra("contas")){
            conta = (ContasClass) it.getSerializableExtra("contas");
            descricaoRecebida.setText(conta.getDescricaoC());
            valorRecebido.setText(Double.toString(conta.getValor()));
        }

        botaoIrAcContas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(ContasActivity.this, ListaContasActivity.class));
                finish();
            }
        });
    }

       public void save(View view) {
        if(conta == null){
            conta = new ContasClass();
            conta.setDescricaoC(descricaoRecebida.getText().toString());
            conta.setValor(Double.valueOf(valorRecebido.getText().toString()));

            TipoClass tipo = new TipoClass();
            tipo.setIdTipo(tipoSelecionado);
            conta.setTipo(tipo);

            ReferenciaClass ref = new ReferenciaClass();
            ref.setIdReferencia(referenciaSelecionada);
            conta.setReferencia(ref);

            long id = contasDao.insert(conta);
            conta.setIdContas((int) id); //Devolvido ID ao objeto criado
            Toast.makeText(this,"Conta incluida com sucesso ", Toast.LENGTH_LONG).show();

            startActivity( new Intent(ContasActivity.this, ContasActivity.class));
            finish();

        }else{
            conta.setDescricaoC(descricaoRecebida.getText().toString());
            conta.setValor(Double.valueOf(valorRecebido.getText().toString()));

            TipoClass tipo = new TipoClass();
            tipo.setIdTipo(tipoSelecionado);
            conta.setTipo(tipo);

            ReferenciaClass ref = new ReferenciaClass();
            ref.setIdReferencia(referenciaSelecionada);
            conta.setReferencia(ref);

            contasDao.atualizar(conta);
            startActivity( new Intent(ContasActivity.this, ContasActivity.class));
            Toast.makeText(this,"Conta atualizada com sucesso ", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
