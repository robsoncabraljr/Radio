package rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragment;

import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.adapter.ComentarioAdapter;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.config.ConfiguracaoFirebase;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.helper.Preferencias;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.model.Comentario;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.model.Ouvinte;

public class Frag2_Comentarios extends Fragment {

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Comentario> listaComentarios;
    private DatabaseReference database;
    private ValueEventListener valueEventListenerComentarios;
    private Random randomico = new Random();
    private SimpleDateFormat dateFormat;
    private Date data, data_atual;
    private Calendar cal;
    private String data_completa;

    /*private int page;
    public static Frag2_Comentarios newInstance(int page) {
        Frag2_Comentarios frag2_Comentarios = new Frag2_Comentarios();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        frag2_Comentarios.setArguments(args);
        return frag2_Comentarios;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }*/

    public Frag2_Comentarios() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        database.addValueEventListener( valueEventListenerComentarios );
        Log.i("ValueEventListener", "onStart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.tab_comentarios, container, false);

        FloatingActionButton fab = rootView.findViewById(R.id.fab_comentarios);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publicarComentario();
            }
        });

        listaComentarios = new ArrayList<>();
        listView = rootView.findViewById(R.id.lista_comentarios);
        arrayAdapter = new ComentarioAdapter(getActivity(), listaComentarios);
        listView.setAdapter(arrayAdapter);

        database = ConfiguracaoFirebase.getDatabase().child("comentarios");
            //Listener para recuperar os comentarios
        valueEventListenerComentarios = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //Limpar lista
                listaComentarios.clear();
                    //Listar comentários
                for (DataSnapshot dados: dataSnapshot.getChildren() ){
                    Comentario comentario = dados.getValue( Comentario.class );
                    listaComentarios.add( comentario );
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        return rootView;
    }

    private void publicarComentario() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            //Configuração do dialog
        alertDialog.setTitle("Nova Publicação");
        alertDialog.setMessage("Digite seu Comentário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(getActivity());
        alertDialog.setView(editText);

            //Configuração dos botões
        alertDialog.setPositiveButton("Postar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String nummeroRandomico = Integer.toString(randomico.nextInt(99999999 - 10000000) + 10000000);
                final String descricaoComentario = editText.getText().toString();
                    // Valida se foi preenchido o editText comentário
                if(descricaoComentario.isEmpty()) {
                    Toast.makeText(getContext(), "Você não digitou um comentário", Toast.LENGTH_SHORT).show();
                } else {
                        //Recuperar identificador usuario logado (base64)
                    Preferencias preferencias = new Preferencias(getActivity());
                    String identificadorOuvinteLogado = preferencias.getIdentificador();

                        // Gera o id para cada comentário
                    final String identifidadorComentario = identificadorOuvinteLogado + "=id=" + nummeroRandomico;

                        // Formata a data para exibição
                    dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                        // Pega a data e hora atual do sistema
                    data = new Date();
                    cal = Calendar.getInstance();
                    cal.setTime(data);
                    data_atual = cal.getTime();
                    data_completa = dateFormat.format(data_atual);

                        //Recuperar instância Firebase
                    database = ConfiguracaoFirebase.getDatabase().child("ouvintes").child(identificadorOuvinteLogado);
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                    //Recuperar dados do contato a ser adicionado
                                Ouvinte nomeOuvinte = dataSnapshot.getValue(Ouvinte.class);

                                database = ConfiguracaoFirebase.getDatabase();
                                database = database.child("comentarios")
                                        .child(identifidadorComentario);

                                Comentario comentario = new Comentario();
                                comentario.setNome(nomeOuvinte.getNome());
                                comentario.setData(data_completa);
                                comentario.setDescricao(descricaoComentario);

                                database.setValue(comentario);
                            } else {
                                Toast.makeText(getActivity(), "O comentário não pode ser postado", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
            }
        });
        alertDialog.create();
        alertDialog.show();
    }
    @Override
    public void onStop() {
        super.onStop();
        database.removeEventListener( valueEventListenerComentarios );
        Log.i("ValueEventListener", "onStop");
    }
}
