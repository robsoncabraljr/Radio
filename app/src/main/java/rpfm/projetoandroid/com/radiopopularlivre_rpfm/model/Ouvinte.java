package rpfm.projetoandroid.com.radiopopularlivre_rpfm.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.config.ConfiguracaoFirebase;

public class Ouvinte {

    private String id, nome, email, senha;

    public Ouvinte() {}

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getDatabase();
        referenciaFirebase.child("ouvintes").child( getId() ).setValue(this);
    }

    @Exclude
    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
