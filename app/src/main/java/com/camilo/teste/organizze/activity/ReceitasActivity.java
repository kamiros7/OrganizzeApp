package com.camilo.teste.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.camilo.teste.organizze.R;
import com.camilo.teste.organizze.config.ConfiguracaoFirebase;
import com.camilo.teste.organizze.helper.Base64Custom;
import com.camilo.teste.organizze.helper.DateCustom;
import com.camilo.teste.organizze.model.Movimentacao;
import com.camilo.teste.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoDescricao, campoCategoria;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoData = findViewById(R.id.editDataReceita);
        campoDescricao = findViewById(R.id.editDescricaoReceita);
        campoCategoria = findViewById(R.id.editCategoriaReceita);
        campoValor = findViewById(R.id.editValorReceita);

        campoData.setText(DateCustom.dataAtual());

        recuperarReceitasTotal();
    }

    public void salvarReceita(View view){
        if(validarCamposReceita()){
            String data = campoData.getText().toString();
            String categoria = campoCategoria.getText().toString();
            String descricao = campoDescricao.getText().toString();
            String valor = campoValor.getText().toString();

            movimentacao = new Movimentacao();

            movimentacao.setCategoria(categoria);
            movimentacao.setDescricao(descricao);
            movimentacao.setData(data);
            movimentacao.setValor(Double.parseDouble(valor));
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + (Double.parseDouble(valor));
            atualizarReceitas(receitaAtualizada);

            movimentacao.salvar(data);

            finish();
        }


    }
    public boolean validarCamposReceita(){
        int validacao = 0;
        String data = campoData.getText().toString();
        String categoria = campoCategoria.getText().toString();
        String descricao = campoDescricao.getText().toString();
        String valor = campoValor.getText().toString();

        if(valor.isEmpty()){
            validacao++;
        }
        else if(data.isEmpty()){
            validacao++;
        }
        else if(categoria.isEmpty()){
            validacao++;
        }
        else if(descricao.isEmpty()){
            validacao++;
        }

        if(validacao != 0){
            Toast.makeText(ReceitasActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void recuperarReceitasTotal(){
        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usarioRef = firebaseDatabase.child("usuarios").child(idUsuario);

        usarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarReceitas(Double receita){

        String emailUsuario = firebaseAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usarioRef = firebaseDatabase.child("usuarios").child(idUsuario);

        usarioRef.child("receitaTotal").setValue(receita);
    }
}
