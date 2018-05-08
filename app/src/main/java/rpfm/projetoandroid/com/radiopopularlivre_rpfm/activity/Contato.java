package rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;

public class Contato extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnTelefone;
    private ImageView btnWhatsapp;
    private ImageView btnFacebook;
    private ImageView btnSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        btnTelefone = findViewById(R.id.btnTelefoneId);
        btnWhatsapp = findViewById(R.id.btnWhatsappId);
        btnFacebook = findViewById(R.id.btnFacebookId);
        btnSite = findViewById(R.id.btnSiteId);

        btnTelefone.setOnClickListener(this);
        btnWhatsapp.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnSite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnTelefoneId) {
            String telefone = "123456789";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel: " + telefone));
            PackageManager packageManager = this.getPackageManager();
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

            PackageManager packageManager = this.getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                startActivity(intent);
            }

        } else if(v.getId() == R.id.btnFacebookId) {
            Uri face = Uri.parse("facebook://facebook.com/inbox");
            Intent intent = new Intent(Intent.ACTION_VIEW, face);

            PackageManager packageManager = this.getPackageManager();
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                startActivity(intent);
            } else {
                face = Uri.parse("https://www.facebook.com/sharer/sharer.php?u=http://rp105.com/hotsite");
                intent = new Intent(Intent.ACTION_VIEW, face);

                packageManager = this.getPackageManager();
                activities = packageManager.queryIntentActivities(intent, 0);
                isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(intent);
                }
            }

        }  else if(v.getId() == R.id.btnSiteId) {

            Uri uri = Uri.parse("http://www.rp105.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            PackageManager packageManager = this.getPackageManager();
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
