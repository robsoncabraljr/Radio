package rpfm.projetoandroid.com.radiopopularlivre_rpfm.model;

import com.google.firebase.database.Exclude;

public class Comentario {

    private String id, nome, data, descricao;

    public Comentario(){}

    @Exclude
    public void setId(String id) {this.id = id; }
    public String getId() {return this.id; }

    public void setNome(String nome) {this.nome = nome; }
    public String getNome() {return this.nome; }

    public String getData() {return data;}
    public void setData(String data) {this.data = data;}

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao(){ return this.descricao; }
}