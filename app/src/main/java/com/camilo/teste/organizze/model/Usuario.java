package com.camilo.teste.organizze.model;

import com.camilo.teste.organizze.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String idUsuario;
    private Double receitaTotal;
    private Double despesaTotal;


    public Usuario() {
        receitaTotal = 0.00;
        despesaTotal = 0.00;
    }

    public Usuario(String nome, String email, String senha, String idUsuario, Double receitaTotal, Double despesaTotal) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.idUsuario = idUsuario;
        this.receitaTotal = receitaTotal;
        this.despesaTotal = despesaTotal;
    }

    public Double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(Double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(Double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Exclude, sendo um recurso do firebase, na hora de setar o valor no firebase, exclui as dados com exclude,
    // nesse caso, a função de um getter da classe
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    @Exclude
    public String getIdUsuario(){ return  idUsuario;}

    public void setIdUsuario(String idUsuario){this.idUsuario = idUsuario; }

    public void salvar(){
        DatabaseReference firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseDatabase.child("usuarios")
                .child(this.idUsuario)
                .setValue(this);


    }
}
