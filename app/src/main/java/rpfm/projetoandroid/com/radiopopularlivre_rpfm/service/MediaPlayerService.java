package rpfm.projetoandroid.com.radiopopularlivre_rpfm.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.fragment.Frag1_AoVivo;

public class MediaPlayerService extends Service {

    Frag1_AoVivo frag1_aoVivo = new Frag1_AoVivo();
    private Boolean estadoInicial = false;
    private Boolean verificar = false;
    public Boolean estadoImg = true;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startPlayer();
        return START_STICKY;
    }

    public void startPlayer() {
        new Thread(startPlayer).start();
    }

    //***Clique no botão, realiza as condições para startar e parar o streaming
    private Runnable startPlayer = new Runnable() {
        public void run() {
            if (estadoInicial) {
                mediaPlayer.pause();
                estadoInicial = false;
                frag1_aoVivo.setEstadoInicial(false);
                estadoImg = true;
                frag1_aoVivo.setEstadoImg(true);
            } else {
                if (mediaPlayer == null) {
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
                        frag1_aoVivo.setVerificar(false);
                        e.printStackTrace();
                        //Toast.makeText(MediaPlayerService.this, "Servidor Offline", Toast.LENGTH_SHORT).show();
                    }
                    onCompletionListener();
                    onPreparedListener();
                } else {
                    mediaPlayer.start();
                    estadoInicial = true;
                    frag1_aoVivo.setEstadoInicial(true);
                }
                estadoImg = false;
                frag1_aoVivo.setEstadoImg(false);
            }
        /* if(verificar) {
            frag1_aoVivo.checkIsPlaying();
        } */
        }
    };

    public void onCompletionListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i("Script", "onCompletion() chamado");
                //Stub de método gerado automaticamente
                estadoInicial = false;
                frag1_aoVivo.setEstadoInicial(false);
                //***Fecha título de exibição
                //progressDialog.dismiss();
                //*** set a imagem do botão para player
                //frag1_aoVivo.imgPlay();
                //*** Reseta a execução
                mediaPlayer.reset();
                //*** Para a execução
                mediaPlayer.stop();
                //*** Exibe mensagem para usuário
                //Toast.makeText(MediaPlayerService.this, "Transmissão encerrada, sem conexão com internet", Toast.LENGTH_LONG).show();
                stop();
            }
        });
    }

    public void onPreparedListener() {
        //*** Executar o streaming após liberado pelo prepareAsync
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mPlayer) {
                Log.i("Script", "onPrepared()");
                //*** Inicia o media player
                mPlayer.start();
                verificar = true;
                frag1_aoVivo.setVerificar(true);
                estadoInicial = true;
                frag1_aoVivo.setEstadoInicial(true);
                estadoImg = false;
                frag1_aoVivo.setEstadoImg(false);
                //***Fecha título de exibição
                //progressDialog.dismiss();
                //*** set a imagem do botão para stop
                //frag1_aoVivo.imgPause();
            }
        });
    }

    /*
    public void onProgressDialog() {
        //*** Defini e exibi mensagem da caixa de diálogo
        progressDialog = ProgressDialog.show(getApplicationContext(), "Por favor, Aguarde ...",
                "Carregando Streaming...");
        //*** Impede que o diálogo seja cancelado pressionando a tecla Voltar
        progressDialog.setCancelable(true);
        //*** Altera a imagem do botão do player
        //streamingGif();
    }
    */

    private void stop() {
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //*** Verifica se está atrinuido, caso sim, anula e libera memoria.
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
}
