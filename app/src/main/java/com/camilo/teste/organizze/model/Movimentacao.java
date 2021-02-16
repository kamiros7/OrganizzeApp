package com.camilo.teste.organizze.model;

import com.camilo.teste.organizze.config.ConfiguracaoFirebase;
import com.camilo.teste.organizze.helper.Base64Custom;
import com.camilo.teste.organizze.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Movimentacao {

    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;
    private String keyId;

    public Movimentacao() {
    }

    public Movimentacao(String data, String categoria, String descricao, String tipo, double valor, String keyId) {
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.keyId = keyId;
    }


    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void salvar(String data){
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(firebaseAuth.getCurrentUser().getEmail());
        String mesAno = DateCustom.mesAnoEscolhida(data);

        DatabaseReference firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseDatabase.child("movimentacao")
                .child(idUsuario)
                .child(mesAno)
                .push()
                .setValue(this);
    }
}
