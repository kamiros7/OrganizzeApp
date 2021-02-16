package com.camilo.teste.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.camilo.teste.organizze.R;
import com.camilo.teste.organizze.config.ConfiguracaoFirebase;
import com.camilo.teste.organizze.helper.Base64Custom;
import com.camilo.teste.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        botaoCadastrar= findViewById(R.id.botaoCadastrar);


        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = campoNome.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                int validacao = validarCampos(nome, email, senha);
                if(validacao == 0 ) {
                    usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    cadastrarUsuario();
                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public int validarCampos(String nome, String email, String senha){
        int validacao = 0;

        if(nome.isEmpty()){
           validacao++;
        }
        else if(email.isEmpty()){
            validacao++;
        }
        else if(senha.isEmpty()){
            validacao++;
        }
        return validacao;
    }

    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();


                    finish(); //finalizar a excecução da activity
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao ="Esta conta já foi cadastrada";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        excecao ="Por favor, digite um email válido";
                    }catch (Exception e){
                        excecao="Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
