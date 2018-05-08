package rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.config.ConfiguracaoFirebase;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragmentos.Frag1_AoVivo;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragmentos.Frag2_Comentarios;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;

public class MainActivity extends AppCompatActivity {

    private AbaAdapter abaAdapter;
    private String[] nomeAbas = {"AO VIVO", "COMENTARIOS"};
    private ViewPager mViewPager;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        abaAdapter = new AbaAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(abaAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorSeekBar));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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
        if (id == R.id.action_compartilhar) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Compartilhar meu App ");
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_sobre) {
            startActivity(new Intent(getApplicationContext(), Sobre.class));
            return true;
        }
        if (id == R.id.item_sair) {
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
            autenticacao.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.item_logar) {
            //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
           // startActivity(intent);
            //startActivity(new Intent(getApplicationContext(), Login.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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
               /* case 2:
                    //return Frag3_Contato.newInstance(2);
                    fragment = new Frag3_Contato();
                    break; */
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
}
