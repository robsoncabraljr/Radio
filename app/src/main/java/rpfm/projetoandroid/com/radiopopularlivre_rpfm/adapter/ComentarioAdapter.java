package rpfm.projetoandroid.com.radiopopularlivre_rpfm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.model.Comentario;

public class ComentarioAdapter extends ArrayAdapter<Comentario> {

    private ArrayList<Comentario> arrayComentarios;
    private Context context;

    public ComentarioAdapter(Context c, ArrayList<Comentario> objects) {
        super(c, 0, objects);
        this.arrayComentarios = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = null;
            // Verifica se a lista está vazia
        if(arrayComentarios != null) {
                // Inicializa a montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Monta a view do xml
            assert inflater != null;
            view = inflater.inflate(R.layout.lista_comentarios, parent, false);

                // Recupera os elementos para exibição
            TextView nomeComentario = (TextView) view.findViewById(R.id.tv_nome);
            TextView dataComentario = (TextView) view.findViewById(R.id.tv_data);
            TextView descricaoComentario = (TextView) view.findViewById(R.id.tv_comentario);

            Comentario comentario = arrayComentarios.get(position);
            nomeComentario.setText(comentario.getNome());
            dataComentario.setText(comentario.getData());
            descricaoComentario.setText(comentario.getDescricao());
        }
        return view;
    }
}
