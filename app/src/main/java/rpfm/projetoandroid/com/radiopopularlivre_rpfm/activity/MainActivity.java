package rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.config.ConfiguracaoFirebase;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragment.Frag1_AoVivo;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragment.Frag2_Comentarios;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.helper.Preferencias;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.model.Comentario;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.model.Ouvinte;

public class MainActivity extends AppCompatActivity {

    private AbaAdapter abaAdapter;
    private String[] nomeAbas = {"AO VIVO", "COMENTARIOS"};
    private ViewPager mViewPager;
    private FirebaseAuth autenticacao;
    private DatabaseReference database;
    private FloatingActionButton fab;
    private SimpleDateFormat dateFormat, data_format_ordem;
    private String data_completa, identificadorComentario;
    private Date data, data_atual;
    private Calendar cal;
    private int posicao = 0;
    //ViewPager pager;

    /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("pagerPosition", pager.getCurrentItem());
        super.onSaveInstanceState(savedInstanceState);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        abaAdapter = new AbaAdapter( getSupportFragmentManager() );
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(abaAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorSeekBar));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(posicao == 0) {
                    Intent intent = new Intent(MainActivity.this, ContatosActivity.class);
                    startActivity(intent);
                }
                else if(posicao == 1) {
                    postarComentario();
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());
                posicao = tab.getPosition();
                Log.e("igr", "getItem: " + tab.getPosition() );
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //if (savedInstanceState != null)
            //pager.setCurrentItem(savedInstanceState.getInt("pagerPosition"));
    }

    int[] iconIntArray = {R.drawable.ic_contact_phone, R.drawable.commenttext};

    protected void animateFab(final int position) {
        fab.clearAnimation();
            // Diminuir a animação
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            // duração da animação em milissegundos
        shrink.setDuration(100);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));

                    // Escalar animação
                ScaleAnimation expand =  new ScaleAnimation(0.2f, 1f, 0.2f,
                        1f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    // Duração da animação em milissegundos
                expand.setDuration(63);
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_compartilhar) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Compartilhar meu App ");
            startActivity(intent);
            return true;
        }
        if (id == R.id.item_sobre) {
            startActivity(new Intent(getApplicationContext(), SobreActivity.class));
            return true;
        }
        if (id == R.id.item_sair) {
            deslogarOuvinte();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarOuvinte() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            //Configuração do dialog
        alertDialog.setTitle("Deslogar");
        alertDialog.setMessage("Desejar sair da sua conta?");
        alertDialog.setCancelable(false);
            //Configuração dos botões
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.create();
        alertDialog.show();
    }

    public class AbaAdapter extends FragmentStatePagerAdapter {
        private AbaAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Log.e("igr", "getItem: " + position );
            Fragment fragment = null;
            switch(position) {
                case 0:
                    //return Frag1_AoVivo.newInstance(0);
                    fragment = new Frag1_AoVivo();
                    break;
                case 1:
                    //return Frag2_Comentarios.newInstance(1);
                    fragment = new Frag2_Comentarios();
                    break;
                default:
            }
            return fragment;
        }
        @Override
        public int getCount() {
            // numero total abas.
            return nomeAbas.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return nomeAbas[position];
        }
    }

    public void postarComentario() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(MainActivity.this));
            //Configuração do dialog
        alertDialog.setTitle("Nova Publicação");
        alertDialog.setMessage("Digite seu Comentário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

            //Configuração dos botões
        alertDialog.setPositiveButton("Postar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String descricaoComentario = editText.getText().toString();
                    // Valida se foi preenchido o editText
                if(descricaoComentario.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Você não digitou um comentário", Toast.LENGTH_SHORT).show();
                } else {
                        //Recuperar identificador usuario logado (base64)
                    Preferencias preferencias = new Preferencias(MainActivity.this);
                    final String identificadorOuvinteLogado = preferencias.getIdentificador();
                        // Gera id do laço
                    final String identificadorOuvinteComentario = identificadorOuvinteLogado + "=" + identificadorComentario;

                        // Formata a data para exibição
                    dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
                        // Cria id numerico
                    data_format_ordem = new SimpleDateFormat("yyyyMMddHHmmss");
                        // Pega a data e hora atual do sistema
                    data = new Date();
                    cal = Calendar.getInstance();
                    cal.setTime(data);
                    data_atual = cal.getTime();
                    data_completa = dateFormat.format(data_atual);
                    identificadorComentario = data_format_ordem.format(data_atual);

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
                                        .child(identificadorOuvinteComentario);

                                Comentario comentario = new Comentario();
                                comentario.setId(identificadorComentario);
                                comentario.setNome(nomeOuvinte.getNome());
                                comentario.setData(data_completa);
                                comentario.setDescricao(descricaoComentario);

                                database.setValue(comentario);
                            } else {
                                Toast.makeText(MainActivity.this, "O comentário não pode ser postado", Toast.LENGTH_LONG)
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
}
