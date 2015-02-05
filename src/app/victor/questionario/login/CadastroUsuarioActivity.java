package app.victor.questionario.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import app.victor.questionario.bancodados.Usuarios;
import app.victor.questionario.bancodados.UsuariosORM;
import app.victor.questionarioandroid.R;

public class CadastroUsuarioActivity extends Activity {
    private EditText etUsusario;
    private EditText etSenha;
    private EditText etSenha2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrousuario_layout);
        etUsusario = (EditText) findViewById(R.id.et_username);
        etSenha = (EditText) findViewById(R.id.et_senha);
        etSenha2 = (EditText) findViewById(R.id.et_senha2);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cadastraUsuario(View view){
        String errosParse = "";
        //Faz a verificacao em camadas, para não apresentar todos os erros de uma vez sem necessidade
        int estagioVerificacao = 0;
        
        if (etUsusario.getText().toString().trim().equals("") && estagioVerificacao == 0) {
            errosParse += "Nome do usuário vazio.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            estagioVerificacao = 1;
        } 
        if (etSenha.getText().toString().trim().equals("") && estagioVerificacao == 0) {
            errosParse += "Senha vazia.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            estagioVerificacao = 1;
        }
        if (etSenha2.getText().toString().trim().equals("") && estagioVerificacao == 0) {
            errosParse += "Confirmação de senha vazia.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            estagioVerificacao = 1;
        }
        if ( !etSenha.getText().toString().trim().equals(etSenha2.getText().toString().trim()) && estagioVerificacao == 0 ) {
            errosParse += "Senhas não conferem. Senha e confirmação devem conter os mesmos dados.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            estagioVerificacao = 1;
        }
        if ( UsuariosORM.checkUsuarioExisteBanco(this, etUsusario.getText().toString().trim()) && estagioVerificacao == 0) {
            errosParse += "Nome de usuário já existente, digite outro.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
        }
        
        if (errosParse.equals("")) {
            Usuarios usuario = new Usuarios(etUsusario.getText().toString().trim(), etSenha.getText().toString().trim() );
            UsuariosORM.insertUsuario(this, usuario);
            
            Toast.makeText(getApplicationContext(), "Usuário: " + etUsusario.getText().toString().trim() + " cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent loginActivity = new Intent(this,LoginActivity.class);
            startActivity(loginActivity);
        } 

    }
}


