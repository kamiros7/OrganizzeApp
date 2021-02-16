package com.camilo.teste.organizze.helper;

import android.util.Base64;

public class Base64Custom {

    //As funções são static para não necessitar de instanciar elas em outras classes
    public static String codificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("\\n|\\r", "");
    }

    public static String decodificarBase64(String textoCodificado){
        return new String (Base64.decode(textoCodificado, Base64.DEFAULT));
    }
}
