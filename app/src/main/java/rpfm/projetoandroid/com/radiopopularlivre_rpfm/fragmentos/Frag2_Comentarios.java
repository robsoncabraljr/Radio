package rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frag2_Comentarios extends Fragment {
    /*
    private int page;
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
    } */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_comentarios, container, false);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
