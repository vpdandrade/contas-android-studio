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

import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.ReferenciaDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.dao.RendimentoDao;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.ReferenciaClass;
import br.com.vg.controlededespesas.br.com.vg.controlededespesas.model.RendimentoClass;

public class Rendimento extends AppCompatActivity {

    private Button salvarRendimento;
    private Button voltar;
    private EditText descricaoRendimento;
    private EditText valorRendimento;
    private ListView listaReferenciaRend;

    private List<ReferenciaClass> listaDescricao;
    private List<ReferenciaClass> listaReferenciaFiltrada = new ArrayList<>();

    private RendimentoDao rendimentoDao;
    private ReferenciaDao referenciaDao;
    private RendimentoClass rendimento;
    private Context context;
    private Integer refeSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rendimento);

        voltar =  findViewById(R.id.buttonVoltarRendID);
        salvarRendimento = (Button) findViewById(R.id.buttonAdicionarRendID);
        descricaoRendimento = findViewById(R.id.textoDescRendID);
        valorRendimento = findViewById(R.id.textoValorRendID);

        rendimentoDao = new RendimentoDao(this);

        listaReferenciaRend = findViewById(R.id.listReferenciaRendID);
        referenciaDao = new ReferenciaDao(this);
        listaDescricao = referenciaDao.findList();
        listaReferenciaFiltrada.addAll(listaDescricao);
        ArrayAdapter<ReferenciaClass> adaptador = new ArrayAdapter<ReferenciaClass>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaReferenciaFiltrada);
        listaReferenciaRend.setAdapter(adaptador);

        context = this;
        listaReferenciaRend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReferenciaClass ref = listaDescricao.get(position);
                refeSelecionada = ref.getIdReferencia();
                String nome = ref.getDescricaoR();
                Toast.makeText(context,"Selecionado mês: " + nome, Toast.LENGTH_SHORT).show();
            }
        });

        Intent it = getIntent();
        if (it.hasExtra("rendimento")) {
            rendimento = (RendimentoClass) it.getSerializableExtra("rendimento");
            descricaoRendimento.setText(rendimento.getDescricaoRR());
            valorRendimento.setText(Double.toString(rendimento.getValorRend()));
        }

        //botao ir para lista de rendimentos
//        salvarRendimento.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(Rendimento.this, ListaRendimentoActivity.class);
//                startActivity(it);
//            }
//        });

        //botao retornar para referencia
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View volta) {
                Intent it = new Intent(Rendimento.this, ReferenciasActivity.class);
                startActivity(it);
            }

        });

    }

    public void save(View view) {
        if (rendimento == null) {
            rendimento = new RendimentoClass();
            rendimento.setDescricaoRR(descricaoRendimento.getText().toString());
            rendimento.setValorRend(Double.valueOf(valorRendimento.getText().toString()));

            ReferenciaClass ref = new ReferenciaClass();
            ref.setIdReferencia(refeSelecionada);
            rendimento.setReferencia(ref);

            long id = rendimentoDao.insert(rendimento);
            Toast.makeText(this, "Rendimento incluído com sucesso!" + id, Toast.LENGTH_LONG).show();

            startActivity( new Intent(Rendimento.this, ListaRendimentoActivity.class));
            finish();
        } else {
            rendimento.setDescricaoRR(descricaoRendimento.getText().toString());
            rendimento.setValorRend(Double.valueOf(valorRendimento.getText().toString()));

            ReferenciaClass ref = new ReferenciaClass();
            ref.setIdReferencia(refeSelecionada);
            rendimento.setReferencia(ref);

            rendimentoDao.atualizar(rendimento);
            startActivity( new Intent(Rendimento.this, ListaRendimentoActivity.class));
            Toast.makeText(this, "Rendimento atualizado com sucesso!", Toast.LENGTH_LONG).show();
            finish();
        }

    }
}