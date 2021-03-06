package app.victor.questionario.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import app.victor.questionario.login.LoginActivity;
import app.victor.questionarioandroid.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity_layout);
        setUserSessionData();
        String nome_usuario = "";
        
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);
        String usuarioLogadoCheck = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            nome_usuario = usuarioLogadoCheck.toString().trim();
        }
        if (!nome_usuario.equals("admin") ) {
            TableRow linhaCadastro = (TableRow) findViewById(R.id.linhaCadastro);
            linhaCadastro.setVisibility(View.INVISIBLE); 
            TableRow linhaEditar = (TableRow) findViewById(R.id.linhaEditar);
            linhaEditar.setVisibility(View.INVISIBLE); 
        }
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

//    @Override
//    protected void onResume() {
//        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);
//        Editor editor = sharedPreferences.edit();
//
//        String usuarioLogadoCheck = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
//        if (!usuarioLogadoCheck.equals("")) {
//            Intent homeActivity = new Intent(this, HomeActivity.class);
//            startActivity(homeActivity);        
//        } else {
//            editor.clear();
//            editor.commit();
//            Intent loginActivity = new Intent(this, LoginActivity.class);
//            startActivity(loginActivity);
//        }
//        super.onResume();
//    }
    
    
    //Funções de callback dos botões
    public void btn_registarPerguntas(View view) {

        Intent intent = new Intent(this, QuantidadePerguntasActivity.class);    
        startActivity(intent);
    }

    public void btn_editarPerguntas(View view) {

        Intent intent = new Intent(this, ListaPerguntasBancoActivity.class);    
        startActivity(intent);
    }
    
    public void btn_responderQuestionario(View view) {

        Intent intent = new Intent(this, RespondeQuestionarioActivity.class);
        startActivity(intent);
    }   
    

    public void infoAutor(View view) {
        Toast.makeText(getApplicationContext(), "Autor: Victor Santos\nemail: victor.inboxfx@gmail.com\nContato: (48) 8411-9029", Toast.LENGTH_LONG).show();
    } 
    
    public void setUserSessionData() {
        TextView sessionInfo=(TextView)findViewById(R.id.username_sessionData); 
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.equals("") && !usuarioUltimoLogin.equals("")) {
            sessionInfo.setText("Usuário Logado: " + usuarioLogado +"\nÚltimo login: " + usuarioUltimoLogin);
        }
    }

    public void logout(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String usuarioLogadoCheck = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            Toast.makeText(getApplicationContext(), "Logout do usuário "+ usuarioLogadoCheck, Toast.LENGTH_LONG).show();
        editor.clear();
        editor.commit();      
        }

        moveTaskToBack(true); 
        HomeActivity.this.finish();
        Intent goLogin = new Intent(this, LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
     }
    
}
