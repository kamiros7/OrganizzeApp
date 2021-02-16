package com.camilo.teste.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String dataAtual(){

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String data = simpleDateFormat.format(date);

        return data;
    }

    public static String mesAnoEscolhida(String data){
        String dataModificada[] = data.split("/");
        String dia = dataModificada[0];
        String mes = dataModificada[1];
        String ano = dataModificada[2];

        String mesAno = mes +ano;
        return mesAno;
    }
}
