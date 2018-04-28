package rpfm.projetoandroid.com.radiopopularlivre_rpfm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Robson Cabral on 08/04/2018.
 */
public class Frag3_Contato extends Fragment implements View.OnClickListener {

    private ImageView btnTelefone;
    private ImageView btnWhatsapp;
    private ImageView btnFacebook;
    private ImageView btnSite;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_contato, container, false);

        btnTelefone = view.findViewById(R.id.btnTelefoneId);
        btnWhatsapp = view.findViewById(R.id.btnWhatsappId);
        btnFacebook = view.findViewById(R.id.btnFacebookId);
        btnSite = view.findViewById(R.id.btnSiteId);

        btnTelefone.setOnClickListener(this);
        btnWhatsapp.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnSite.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnTelefoneId) {
            String telefone = "123456789";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel: " + telefone));
            PackageManager packageManager = getContext().getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                startActivity(intent);
            }

        } else if(v.getId() == R.id.btnWhatsappId){
            String whatsapp = "987654321";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, whatsapp);
            intent.setPackage("com.whatsapp");
            intent.setType("text/plain");

            PackageManager packageManager = getContext().getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                startActivity(intent);
            }

        } else if(v.getId() == R.id.btnFacebookId) {
            Uri face = Uri.parse("facebook://facebook.com/inbox");
            Intent intent = new Intent(Intent.ACTION_VIEW, face);

            PackageManager packageManager = getContext().getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                startActivity(intent);
            } else {
               face = Uri.parse("https://www.facebook.com/sharer/sharer.php?u=http://rp105.com/hotsite");
               intent = new Intent(Intent.ACTION_VIEW, face);

                packageManager = getContext().getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);
                isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(intent);
                }
            }

        }  else if(v.getId() == R.id.btnSiteId) {

            Uri uri = Uri.parse("http://www.rp105.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            PackageManager packageManager = getContext().getPackageManager();
            List<ResolveInfo> activitiesSite = packageManager.queryIntentActivities(intent, 0);
            boolean isIntentSafe = activitiesSite.size() > 0;
            if (isIntentSafe) {
                startActivity(intent);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
