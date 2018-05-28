package rpfm.projetoandroid.com.radiopopularlivre_rpfm.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import rpfm.projetoandroid.com.radiopopularlivre_rpfm.R;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.config.ConfiguracaoFirebase;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.helper.Base64Custom;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.helper.Preferencias;
import rpfm.projetoandroid.com.radiopopularlivre_rpfm.model.Ouvinte;

public class CadastroOuvintesActivity extends AppCompatActivity {

    private EditText id, nome, email, senha;
    private Button btn_cadastrar;
    private Ouvinte ouvinte;
    private FirebaseAuth autenticacao;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ouvinte);

        nome = findViewById(R.id.txt_cadastro_nomeId);
        email = findViewById(R.id.txt_cadastro_emailId);
        senha = findViewById(R.id.txt_cadastro_senhaId);
        btn_cadastrar = findViewById(R.id.btn_cadastrarId);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ouvinte = new Ouvinte();
                ouvinte.setNome( nome.getText().toString() );
                ouvinte.setEmail(email.getText().toString());
                ouvinte.setSenha(senha.getText().toString());
                cadastrarOuvinte();
            }
        });
    }

    private void cadastrarOuvinte() {
            //*** Defini e exibi mensagem da caixa de diálogo
        progressDialog = ProgressDialog.show(CadastroOuvintesActivity.this, "Validando Cadastro",
                "Por favor, Aguarde ...");
            //*** Impede que o diálogo seja cancelado pressionando a tecla Voltar
        progressDialog.setCancelable(false);
            // Valida dados
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                ouvinte.getEmail(),
                ouvinte.getSenha()
        ).addOnCompleteListener(CadastroOuvintesActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if( task.isSuccessful() ) {
                    String identificadorOuvinte = Base64Custom.codificarBase64(ouvinte.getEmail());
                    ouvinte.setId( identificadorOuvinte );
                    ouvinte.salvar();

                    Preferencias preferencias = new Preferencias(CadastroOuvintesActivity.this);
                    preferencias.salvarDados(identificadorOuvinte);

                    abrirLoginOuvinte();
                } else {
                    senha.setText("");
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Senha fraca, crie uma senha mais forte, contendo letras e número";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O E-mail digitado está incorreto, informe um e-mail valido";
                    } catch(FirebaseAuthUserCollisionException e) {
                        erroExcecao = "E-mail já cadastrado";
                    }catch (Exception e) {
                        erroExcecao = "Erro ao efetuar cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroOuvintesActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void abrirLoginOuvinte() {
        Intent intent = new Intent(CadastroOuvintesActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
