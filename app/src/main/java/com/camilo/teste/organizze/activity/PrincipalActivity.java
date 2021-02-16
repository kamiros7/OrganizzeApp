package com.camilo.teste.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camilo.teste.organizze.R;
import com.camilo.teste.organizze.adapter.AdapterMovimentacao;
import com.camilo.teste.organizze.config.ConfiguracaoFirebase;
import com.camilo.teste.organizze.helper.Base64Custom;
import com.camilo.teste.organizze.model.Movimentacao;
import com.camilo.teste.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textoSaudacao, textoSaldo;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private Double despesaTotal;
    private Double receitaTotal;

    private Double resumoUsuario;

    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListener;
    private ValueEventListener valueEventListenerMovimentacoes;

    private RecyclerView recyclerView;
    private List<Movimentacao> movimentacaoList = new ArrayList<>();
    private AdapterMovimentacao adapterMovimentacao;
    private Movimentacao movimentacaoAux;

    private DatabaseReference movimentacaoRef;
    private String mesAnoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);

        //Configurando o firebase
        firebaseAuth =  ConfiguracaoFirebase.getFirebaseAutenticacao();
        firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();

        textoSaldo = findViewById(R.id.textSaldo);
        textoSaudacao = findViewById(R.id.textSaudacao);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimentos);

        configuraCalendario();
        swipe(); //função para deslizar itens do recyler view

        //configura o adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacaoList, this);


        //Configurar recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterMovimentacao);

    }

    public void funcTeste(){
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseDatabase.child("usuarios").child(idUsuario);
    }

    public void recuperarResumo(){

        //fazer uma funçãoi para os negocios a baixo, coloco as var abaixo como atributo "global"
        //a função é chamado apenas se usuario estiver logado

        if(firebaseAuth.getCurrentUser() != null){
            funcTeste();
        }/*
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseDatabase.child("usuarios").child(idUsuario); */

        valueEventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
                receitaTotal = usuario.getReceitaTotal();
                resumoUsuario = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String resultadoFormatado = decimalFormat.format(resumoUsuario);

                textoSaudacao.setText("Olá, " + usuario.getNome());
                textoSaldo.setText("R$ " +resultadoFormatado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Criação do menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //método inflate é para converter um xml em uma view
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuSair:

                firebaseAuth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarReceita(View view){
        startActivity(new Intent(this, ReceitasActivity.class));
    }

    public void adicionarDespesa(View view){
        startActivity(new Intent(this, DespesasActivity.class));
    }

    public void configuraCalendario(){

        CalendarDay data = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (data.getMonth()+1));

        mesAnoSelecionado = String.valueOf(mesSelecionado + "" + data.getYear());

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2020,1,1))
                .setMaximumDate(CalendarDay.from(2099, 1, 1));

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String mesSelecionado = String.format("%02d", (date.getMonth()+1));
                mesAnoSelecionado = String.valueOf(mesSelecionado + "" + date.getYear());

                //Como é adicionado um eventListiner no recuperar movimetnacoes, para não ficar criando em cascata
                //é removido o event lsitner, depois adcionado no recuperar movimentos
                movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
                recuperarMovimentacoes();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumo();
        recuperarMovimentacoes();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListener);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacoes);
    }

    public void recuperarMovimentacoes(){

        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);

        movimentacaoRef = firebaseDatabase.child("movimentacao")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        valueEventListenerMovimentacoes = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movimentacaoList.clear(); //limpa a lista

                for(DataSnapshot dados : snapshot.getChildren()){
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class); //dessa forma ele retorna como valor a classe movimentação
                    movimentacao.setKeyId(dados.getKey());
                    movimentacaoList.add(movimentacao);

                }
                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void excluirMovimentacoes(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura o alert dialog
        alertDialog.setTitle("Excluir movimentação da conta");
        alertDialog.setMessage("Você tem certeza que deseja excluir essa movimentação da sua conta ?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacaoAux = movimentacaoList.get(position);


                String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsuario);

                movimentacaoRef = firebaseDatabase.child("movimentacao")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);

                movimentacaoRef.child(movimentacaoAux.getKeyId()).removeValue();
                adapterMovimentacao.notifyItemRemoved(position);
                atualizarSaldo();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                adapterMovimentacao.notifyDataSetChanged();
            }

        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void atualizarSaldo(){

        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        usuarioRef = firebaseDatabase.child("usuarios").child(idUsuario);

        if(movimentacaoAux.getTipo().equals("r")){
            receitaTotal = receitaTotal - movimentacaoAux.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }
        else if(movimentacaoAux.getTipo().equals("d")){
            despesaTotal = despesaTotal - movimentacaoAux.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }


    }

    public void swipe() {
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacoes(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }
}
