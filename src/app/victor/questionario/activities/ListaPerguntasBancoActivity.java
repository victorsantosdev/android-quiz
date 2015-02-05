package app.victor.questionario.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import app.victor.questionario.bancodados.Perguntas;
import app.victor.questionario.bancodados.PerguntasORM;
import app.victor.questionario.login.LoginActivity;
import app.victor.questionarioandroid.R;


@SuppressLint("NewApi")
public class ListaPerguntasBancoActivity extends Activity {

    // variáveis posicionadas de maneira global à classe OnCreate para terem
    // seus valores acessados pelas demais funções

    // lista com os cabecalhos das perguntas
    TextView indicePergunta;
    List<TextView> allIndicePergunta;
    TextView perguntaTexto;
    List<TextView> allPerguntaTexto;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaperguntasbanco_layout);
        setUserSessionData();

        List<Perguntas> perguntasList = PerguntasORM.getPerguntas(ListaPerguntasBancoActivity.this);
        if (perguntasList.size() == 0) {

            Intent intent = new Intent(this, BancoVazioActivity.class);
            startActivity(intent);
        }

        TableLayout tabelaLayout = (TableLayout) findViewById(R.id.tabela_perguntas);

        allIndicePergunta = new ArrayList<TextView>();
        allPerguntaTexto = new ArrayList<TextView>();

        // gera a quantidade de textViews e editTexts proporcionais ao número de
        // perguntas registradas no banco
        int tvIdcounter = 0;

        for (int i = 0; i < perguntasList.size(); i++) {
            TableRow tr = new TableRow(this);
            if(i%2!=0) tr.setBackgroundColor(Color.LTGRAY);
            tr.setId(i);

            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));

            // monta o indice das perguntas
            indicePergunta = new TextView(ListaPerguntasBancoActivity.this);
            allIndicePergunta.add(indicePergunta);
            indicePergunta.setText("Pergunta: "+i);
            indicePergunta.setId(tvIdcounter);
            tr.addView(indicePergunta);

            perguntaTexto = new TextView(ListaPerguntasBancoActivity.this);
            allPerguntaTexto.add(perguntaTexto);
            perguntaTexto.setText(perguntasList.get(i).getPerguntaTexto());
            perguntaTexto.setId(tvIdcounter++);
            tr.addView(perguntaTexto);  
            perguntaTexto.setOnClickListener(cabecalhosOnClickListener);

            // finally add this to the table row
            tabelaLayout.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }



    OnClickListener cabecalhosOnClickListener = new OnClickListener() {

        public void onClick(View v) {
            String clicked_pergunta = ((TextView)v).getText().toString();

            //int clicked_id = ((TextView)v).getId();
            Toast.makeText(getApplicationContext(), "Texto da pergunta clicada: "+clicked_pergunta, Toast.LENGTH_SHORT).show();
            //Passa o valor do ID para a activity de edição/apagar
            editPergunta(clicked_pergunta);
        }
    };

    //Função acessada externamente porque não dá pra chamar o intent de dentro do método onClickView
    public void editPergunta (String textoPergunta) {
        Intent intent = new Intent(this, AlteraPerguntasBancoActivity.class);
        intent.putExtra("pergunta_texto", textoPergunta);
        moveTaskToBack(true);
        startActivity(intent);
    }


    //Funções de callback dos botões
    public void btn_registarPerguntas(View view) {

        Intent intent = new Intent(this, QuantidadePerguntasActivity.class);    
        startActivity(intent);
    }


    public void setUserSessionData() {
        TextView sessionInfo = (TextView) findViewById(R.id.username_sessionData);
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);

        String usuarioLogado = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
        String usuarioUltimoLogin = sharedPreferences.getString(LoginActivity.ultimoLogin, "");
        if (!usuarioLogado.trim().equals("") && !usuarioUltimoLogin.trim().equals("")) {
            sessionInfo.setText("Usuário Logado: " + usuarioLogado + "\nÚltimo login: " + usuarioUltimoLogin);
        }
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.userPreferences, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        String usuarioLogadoCheck = sharedPreferences.getString(LoginActivity.usuarioLogado, "");
        if (!usuarioLogadoCheck.equals("")) {
            Toast.makeText(getApplicationContext(), "Logout do usuário " + usuarioLogadoCheck, Toast.LENGTH_LONG).show();
            editor.clear();
            editor.commit();
        }
        moveTaskToBack(true); 
        ListaPerguntasBancoActivity.this.finish();
        Intent goLogin = new Intent(this, LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
    }

    public void btn_voltar(View view) {
        Intent backHome = new Intent(this, HomeActivity.class);    
        startActivity(backHome);
    }
    
    @Override
    public void onBackPressed() {
        Intent stayHere = new Intent(this, ListaPerguntasBancoActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        stayHere.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(stayHere);
        super.onBackPressed();
    }

}


