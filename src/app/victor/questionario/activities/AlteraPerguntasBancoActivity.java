package app.victor.questionario.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;
import app.victor.questionario.bancodados.Perguntas;
import app.victor.questionario.bancodados.PerguntasORM;
import app.victor.questionario.login.LoginActivity;
import app.victor.questionarioandroid.R;

@SuppressLint("NewApi")
public class AlteraPerguntasBancoActivity extends Activity{

    EditText perguntaTexto;
    Spinner respostaEscolha;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alteraperguntasbanco_layout);
        setUserSessionData();

        TableLayout tabelaLayout = (TableLayout) findViewById(R.id.tabela_editPergunta);
        tabelaLayout.setColumnShrinkable(1,true);

        // Recebe o ID da pergunta passado pelo Intent
        Bundle extras = getIntent().getExtras();
        String pergunta_texto = extras.getString("pergunta_texto");
        Perguntas toEditPergunta;
        toEditPergunta = PerguntasORM.getPergunta(this, pergunta_texto);

        /* cabecalho da pergunta: linha da tabela */
        //mais conveniente utilizar só para debug, o usuário não precisa saber o ID da pergunta
//        TableRow tr_cabecalho = new TableRow(this);
//        tr_cabecalho.setId(0);
//
//        TextView idPergunta = new TextView(AlteraPerguntasBancoActivity.this);
//        idPergunta.setText("PERGUNTA ID: " + toEditPergunta.getId());
//        idPergunta.setId(0);
//        tr_cabecalho.addView(idPergunta);
//        tr_cabecalho.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 1));

        /* conteúdo (texto) da pergunta */
        TableRow tr_textoPergunta = new TableRow(this);
        tr_textoPergunta.setId(0);

        TextView textoPergunta = new TextView(AlteraPerguntasBancoActivity.this);
        textoPergunta.setText("Texto da pergunta: ");
        textoPergunta.setId(1);
        tr_textoPergunta.addView(textoPergunta);

        perguntaTexto = new EditText(AlteraPerguntasBancoActivity.this);
        perguntaTexto.setId(0);
        perguntaTexto.setText(toEditPergunta.getPerguntaTexto());
        tr_textoPergunta.addView(perguntaTexto);
        tr_textoPergunta.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 0.50));

        /* tipo da resposta */
        TableRow tr_tipoResposta = new TableRow(this);
        tr_tipoResposta.setId(1);

        TextView tipoResposta = new TextView(AlteraPerguntasBancoActivity.this);        
        tipoResposta.setText("Tipo da resposta: ");
        tipoResposta.setId(2);
        tr_tipoResposta.addView(tipoResposta);

        respostaEscolha = new Spinner(AlteraPerguntasBancoActivity.this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.tiporesposta_array));
        respostaEscolha.setAdapter(adapter);

        //o logcat indica um erro quando se usa diferentes Views com o mesmo ID em um único layout
        respostaEscolha.setId(3); 

        if (toEditPergunta.getTipoResposta().trim().equals("Sim/Não")) {
            respostaEscolha.setSelection(1, true);
        } else respostaEscolha.setSelection(0, true);
        tr_tipoResposta.addView(respostaEscolha);
        tr_tipoResposta.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, (float) 0.50));

        //incluindo as rows na tabela
        //tabelaLayout.addView(tr_cabecalho, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tabelaLayout.addView(tr_textoPergunta, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tabelaLayout.addView(tr_tipoResposta, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)); 
    }

    //Funções de callback dos botões

    public void btn_updatePergunta (View view) {
        Bundle extras = getIntent().getExtras();
        String pergunta_texto = extras.getString("pergunta_texto");
        Perguntas toEditPergunta;
        toEditPergunta = PerguntasORM.getPergunta(this, pergunta_texto);

        int estagioVerificacao = 0;
        String errosParse = "";

        //teste de erros: campos de pergunta vazios ou com espaço (tentativa de burlar)
        if (perguntaTexto.getText().toString().trim().equals("")) {
            errosParse += "Campo de pergunta vazio.\n";
            Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
            estagioVerificacao = 1;
        }

        //testa se a pergunta já existe no banco
        if (estagioVerificacao == 0) {

            if ( PerguntasORM.checkPerguntaExisteBanco(this, perguntaTexto.getText().toString().trim() )) {
                errosParse += "Pergunta já existe no banco, digite outra.\n";
                Toast.makeText(getApplicationContext(), errosParse, Toast.LENGTH_SHORT).show();
                estagioVerificacao = 1;
            }    
        }

        //se errosParse é vazio não teve nenhum erro
        if (errosParse.equals("")) {        
            PerguntasORM.updatePergunta (this, toEditPergunta.getId(), perguntaTexto.getText().toString().trim(), respostaEscolha.getSelectedItem().toString());
            Toast.makeText(getApplicationContext(), "Pergunta alterada com sucesso.", Toast.LENGTH_SHORT).show();

            //solução para o problema do usuário apertar o botão voltar depois de editar a pergunta e voltar para a tela de edição novamente
            Intent listaPerguntas = new Intent(this, ListaPerguntasBancoActivity.class); 
            AlteraPerguntasBancoActivity.this.finish();
            startActivity(listaPerguntas);
        }
    }


    public void btn_deletePergunta(View view) {
        Bundle extras = getIntent().getExtras();
        String pergunta_texto = extras.getString("pergunta_texto");
        if (PerguntasORM.deletePergunta(this, pergunta_texto)) 
            Toast.makeText(getApplicationContext(), "Pergunta : "+pergunta_texto+ " APAGADA.", Toast.LENGTH_SHORT).show();

        //solução para o problema do usuário apertar o botão voltar depois de apagar a pergunta e voltar para a tela de edição novamente
        Intent listaPerguntas = new Intent(this, ListaPerguntasBancoActivity.class);    
        AlteraPerguntasBancoActivity.this.finish();
        startActivity(listaPerguntas);
    }

    public void btn_voltar(View view) {
        Intent listaPerguntas = new Intent(this, ListaPerguntasBancoActivity.class);    
        startActivity(listaPerguntas);
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
        //moveTaskToBack(true); 
        AlteraPerguntasBancoActivity.this.finish();
        Intent goLogin = new Intent(this, LoginActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goLogin);
    }
    @Override
    public void onBackPressed() {
        Intent goListaPerguntas = new Intent(this, ListaPerguntasBancoActivity.class);
        //Limpa a stack de activities pra resolver o bug do usuário apertar o botão de voltar e ele voltar na sessão já logado pelo sharedPreferences
        goListaPerguntas.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goListaPerguntas);
        super.onBackPressed();
    }

}



