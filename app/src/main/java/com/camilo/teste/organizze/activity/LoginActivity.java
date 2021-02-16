package com.camilo.teste.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.camilo.teste.organizze.R;
import com.camilo.teste.organizze.config.ConfiguracaoFirebase;
import com.camilo.teste.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        botaoEntrar = findViewById(R.id.botaoEntrar);
        campoEmail = findViewById(R.id.editEmailLogin);
        campoSenha = findViewById(R.id.editSenhaLogin);

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                int validacao = validarCampos(email, senha);
                if(validacao == 0 ) {
                      usuario = new Usuario();
                      usuario.setEmail(email);
                      usuario.setSenha(senha);

                      validarLogin();
                }else{
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public int validarCampos(String email, String senha){
        int validacao = 0;

        if(email.isEmpty()){
            validacao++;
        }
        else if(senha.isEmpty()){
            validacao++;
        }
        return validacao;
    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Sucesso ao efeturar o login", Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        excecao ="Email e senha não correspondem a um usuário cadastrado";
                    }catch(FirebaseAuthInvalidUserException e){
                        excecao = "Usuário não está cadastrado";
                    }catch (Exception e){
                        excecao="Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }
}
