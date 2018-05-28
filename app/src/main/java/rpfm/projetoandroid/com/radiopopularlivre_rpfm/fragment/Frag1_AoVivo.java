package rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragment;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.IOException;
import java.util.Objects;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity.ContatosActivity;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity.MainActivity;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.service.MediaPlayerService;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frag1_AoVivo extends Fragment {

    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private Boolean estadoInicial = false;
    private Boolean estadoImg = false;
    private Boolean verificar = false;
    private ImageView btnTocar;
    private MediaPlayer mediaPlayer;
    public ProgressDialog progressDialog;
    //MediaPlayerService service = new MediaPlayerService();

    /*private int page;
    public static Frag1_AoVivo newInstance(int page) {
        Frag1_AoVivo frag1_AoVivo = new Frag1_AoVivo();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        frag1_AoVivo.setArguments(args);
        return frag1_AoVivo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }*/

   public Frag1_AoVivo() {
       // Required empty public constructor
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_aovivo, container, false);
        if(savedInstanceState != null) {
            estadoInicial = savedInstanceState.getBoolean("estadoInicial");
            if(estadoInicial) {
                //playPause(null);
            }
        }
        volumeSeekbar = rootView.findViewById(R.id.seekBarVolumeId);
        btnTocar = rootView.findViewById(R.id.btnPlayPauseId);
        btnTocar.setImageResource(R.drawable.play);
        controleVolume();

        streamPlay();
        return rootView;
    }

    public void setEstadoInicial(Boolean estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public void setEstadoImg(Boolean estadoImg) {
        this.estadoImg = estadoImg;
    }

    public void setVerificar(Boolean verificar) {
        this.verificar = verificar;
    }

    private void playService() {
        Objects.requireNonNull(getActivity()).startService(new Intent(getActivity(), MediaPlayerService.class));
    }

    @Override
    public void onSaveInstanceState(Bundle output) {
        super.onSaveInstanceState(output);
        output.putBoolean("estadoInicial", estadoInicial);
    }

        //*** SeekBar de controle do volume
    private void controleVolume() {
        try {
            audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {}

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {}

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

        //*** Cria uma notificação
    private void notificacao() {
        //Intent intent = new Intent(getActivity(), MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

        Notification notificacao = new Notification.Builder(getActivity())
                .setContentTitle("RP 'Livre' como você!")
                .setContentText("Ao Vivo")
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_notificacao))
                .build();
        //.setContentIntent(pendingIntent)

        NotificationManager notifyManager = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.NOTIFICATION_SERVICE);
        notificacao.flags |= Notification.FLAG_AUTO_CANCEL;
        if(notifyManager != null) {
            notifyManager.notify(1, notificacao);
        }
    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

    private void streamPlay() {
        btnTocar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                playService();
                notificacao();
                checkIsPlaying();
                //onProgressDialog();

                /*if(mediaPlayer == null) {
                    onProgressDialog();
                }*/
            }
        });
    }

        //*** Verifica os estados das imagem play/stop
    public void checkIsPlaying(){
        if(estadoImg) {
            btnTocar.setImageResource(R.drawable.play);
        } else {
            btnTocar.setImageResource(R.drawable.stop);
        }
    }

    /*
        //***Clique no botão, realiza as condições para startar e parar o streaming
    public void playPause() {
        if (estadoInicial) {
            mediaPlayer.pause();
            estadoInicial = false;
            estadoImg = true;
        } else {
            if (mediaPlayer == null) {
                notificacao();
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        //*** Passa os dados ao mediaPlayer
                    mediaPlayer.setDataSource("http://mixmp3.crossradio.com.br:1102/live");
                        //*** Preparação assíncrona media player
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    verificar = false;
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Servidor Offline", Toast.LENGTH_SHORT).show();
                }
                onCompletionListener();
                onProgressDialog();
                onPreparedListener();
            } else {
                mediaPlayer.start();
                estadoInicial = true;
            }
            estadoImg = false;
        }
        if(verificar) {
            checkIsPlaying();
        }
    }

    public void onCompletionListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i("Script", "onCompletion() chamado");
                    //Stub de método gerado automaticamente
                estadoInicial = false;
                    //***Fecha título de exibição
                progressDialog.dismiss();
                    //*** set a imagem do botão para player
                btnTocar.setImageResource(R.drawable.play);
                    //*** Reseta a execução
                mediaPlayer.reset();
                    //*** Para a execução
                mediaPlayer.stop();
                    //*** Exibe mensagem para usuário
                Toast.makeText(getContext(), "Transmissão encerrada, sem conexão com internet", Toast.LENGTH_LONG).show();
                stop();
            }
        });
    }
    */

    public void onProgressDialog() {
            //*** Defini e exibi mensagem da caixa de diálogo
        progressDialog = ProgressDialog.show(getContext(), "Por favor, Aguarde ...",
                "Carregando Streaming...");
            //*** Impede que o diálogo seja cancelado pressionando a tecla Voltar
        progressDialog.setCancelable(true);
            //*** Altera a imagem do botão do player
        Glide.with(getActivity()).load(R.drawable.carregando).asGif().into(btnTocar);
    }

    /*
    public void onPreparedListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i("Script", "onCompletion() chamado");
                //Stub de método gerado automaticamente
                estadoInicial = false;
                //***Fecha título de exibição
                //progressDialog.dismiss();
                //*** set a imagem do botão para stop
                btnTocar.setImageResource(R.drawable.stop);
                //*** Reseta a execução
                mediaPlayer.reset();
                //*** Para a execução
                mediaPlayer.stop();
                //*** Exibe mensagem para usuário
                //Toast.makeText(MediaPlayerService.this, "Transmissão encerrada, sem conexão com internet", Toast.LENGTH_LONG).show();
                //stop();
            }
        });
                Log.i("Script", "onPrepared()");

    }


    private void stop() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(estadoInicial) {
            mediaPlayer.start();
            checkIsPlaying();
        } else {
           streamPlay();
        }
    }
    */

    @Override
    public void onDestroy() {
        super.onDestroy();
            //*** Verifica se está atrinuido, caso sim, anula e libera memoria.
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //service.onDestroy();
    }
}

