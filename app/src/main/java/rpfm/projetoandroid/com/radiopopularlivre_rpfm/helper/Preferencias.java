package rpfm.projetoandroid.com.radiopopularlivre_rpfm.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferencias;
    private String NOME_ARQUIVO = "rp_preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorOuvinteLogado";
    //private final String CHAVE_NOME = "nomeOuvinteLogado";

    public Preferencias(Context contextoParamentro) {

        contexto = contextoParamentro;
        preferencias = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferencias.edit();
    }

    public void salvarDados(String identificadorOuvinte) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadorOuvinte);
        //editor.putString(CHAVE_NOME, nomeOuvinte);
        editor.commit();
    }

    public String getIdentificador() {
        return preferencias.getString(CHAVE_IDENTIFICADOR, null);
    }
    //public String getNomeOuvinte(){ return preferencias.getString(CHAVE_NOME, null);}
}
