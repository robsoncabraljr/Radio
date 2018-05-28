package rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.config.ConfiguracaoFirebase;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.helper.Base64Custom;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.helper.Preferencias;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.model.Ouvinte;

public class LoginActivity extends AppCompatActivity {

    private EditText email, senha;
    private Button btnLogar;
    private Ouvinte ouvinte;
    private FirebaseAuth autenticacao;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarLogin();

        email = findViewById(R.id.txt_login_emailId);
        senha = findViewById(R.id.txt_login_senhaId);
        btnLogar = findViewById(R.id.btn_logarId);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ouvinte = new Ouvinte();
                ouvinte.setEmail(email.getText().toString());
                ouvinte.setSenha(senha.getText().toString());
                validarLogin();
            }
        });
    }

    private void verificarLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null) {
            liberarAcesso();
        }
    }

    private void validarLogin() {
        //*** Defini e exibi mensagem da caixa de diálogo
        progressDialog = ProgressDialog.show(LoginActivity.this, "Validando Login",
                "Por favor, Aguarde ...");
        //*** Impede que o diálogo seja cancelado pressionando a tecla Voltar
        progressDialog.setCancelable(false);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                ouvinte.getEmail(),
                ouvinte.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    String identificadorOuvinteLogado = Base64Custom.codificarBase64(ouvinte.getEmail());;
                    preferencias.salvarDados(identificadorOuvinteLogado);
                    liberarAcesso();
                } else {
                    senha.setText("");
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthInvalidUserException e) {
                        erroExcecao = "E-mail incorreto ou não cadastrado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Senha incorreta";
                    } catch (Exception e) {
                        erroExcecao = "Ao efetuar o login";
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void liberarAcesso() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroOuvintesActivity.class);
        startActivity(intent);
    }

    public void recuperarSenha(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(LoginActivity.this));
            //Configuração do dialog
        alertDialog.setTitle("Recuperar senha");
        alertDialog.setMessage("Digite seu seu e-mail");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(LoginActivity.this);
        alertDialog.setView(editText);

            //Configuração dos botões
        alertDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String emailOuvinte = editText.getText().toString();
                    // Valida se foi preenchido o editText comentário
                if(emailOuvinte.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Você não digitou um comentário", Toast.LENGTH_SHORT).show();
                } else {
                    autenticacao.sendPasswordResetEmail(emailOuvinte)
                            .addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if( task.isSuccessful() ){
                                        Snackbar.make(findViewById(android.R.id.content), "Um link para recuperar a senha foi enviado para seu e-mal", Snackbar.LENGTH_LONG)
                                                .show();
                                    } else {
                                        Snackbar.make(findViewById(android.R.id.content), "E-mail não cadastrado, verifique o e-mail e tente novamente!", Snackbar.LENGTH_LONG)
                                                .show();
                                    }
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
