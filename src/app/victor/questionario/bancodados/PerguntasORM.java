/*
 * Autor: Victor Santos Aplicativo de questionário Android 31/01/2015
 * victor.ifsc@gmail.com ORM da classe Perguntas, para facilitar os acessos
 * relativos ao uso do banco de dados Sqlite
 */

package app.victor.questionario.bancodados;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PerguntasORM {

    private static final String TAG = "PerguntasORM";

    private static final String TABLE_NAME = "perguntas";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_PERGUNTA_ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String COLUMN_PERGUNTA_ID = "pergunta_id";

    private static final String COLUMN_TEXTOPERGUNTA_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_TEXTOPERGUNTA = "pergunta_texto";

    private static final String COLUMN_TIPORESPOSTA_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_TIPORESPOSTA = "pergunta_tiporesposta";

    private static final String CONSTRAINT_UNIQUE = "UNIQUE";
    private static final String UNIQUE_CONDITIONS = "ON CONFLICT ABORT";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_PERGUNTA_ID + " " + COLUMN_PERGUNTA_ID_TYPE + COMMA_SEP
            + COLUMN_TEXTOPERGUNTA + " " + COLUMN_TEXTOPERGUNTA_TYPE + COMMA_SEP + COLUMN_TIPORESPOSTA + " " + COLUMN_TIPORESPOSTA_TYPE + COMMA_SEP + CONSTRAINT_UNIQUE + "("
            + COLUMN_TEXTOPERGUNTA + ") " + UNIQUE_CONDITIONS + ")";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertPergunta(Context context, Perguntas pergunta) {

        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        ContentValues values = postToContentValues(pergunta);
        long perguntaID = database.insert(PerguntasORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Pergunta with ID: " + perguntaID);
        Log.v("Debug Inserção pergunta BD", "Pergunta inserida == : " + "pergunta_texto: " + pergunta.getPerguntaTexto() + "  tipo_resposta: " + pergunta.getTipoResposta());
        database.close(); // whenever you get a readable/writable db you should
                          // close it after using
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(Perguntas pergunta) {
        ContentValues values = new ContentValues();
        values.put(PerguntasORM.COLUMN_TEXTOPERGUNTA, pergunta.getPerguntaTexto());
        values.put(PerguntasORM.COLUMN_TIPORESPOSTA, pergunta.getTipoResposta());
        return values;
    }

    public static List<Perguntas> getPerguntas(Context context) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + PerguntasORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Perguntas...");
        List<Perguntas> perguntasList = new ArrayList<Perguntas>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Perguntas pergunta = cursorToPergunta(cursor);
                perguntasList.add(pergunta);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }

        cursor.close();
        database.close();

        return perguntasList;
    }

    /**
     * Populates a Post object with data from a Cursor
     * 
     * @param cursor
     * @return
     */
    private static Perguntas cursorToPergunta(Cursor cursor) {
        Perguntas pergunta = new Perguntas();
        pergunta.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_PERGUNTA_ID)));
        pergunta.setPerguntaTexto(cursor.getString(cursor.getColumnIndex(COLUMN_TEXTOPERGUNTA)));
        pergunta.setTipoResposta(cursor.getString(cursor.getColumnIndex(COLUMN_TIPORESPOSTA)));

        return pergunta;
    }

    //Testa se a pergunta existe no banco, pra não criar perguntas duplicadas
    public static boolean checkPerguntaExisteBanco(Context context, String pergunta_texto) throws SQLException {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + PerguntasORM.TABLE_NAME + " WHERE " + COLUMN_TEXTOPERGUNTA + "=?", new String[] { pergunta_texto });
        if (cursor != null && cursor.getCount() > 0) {
            // só tem uma pergunta porque o campo é UNIQUE no BD
            cursor.moveToFirst();
            Perguntas pergunta = cursorToPergunta(cursor);
            Log.v("Debug pergunta existente BD", "Pergunta encontrada == : " + "pergunta_texto: " + pergunta.getPerguntaTexto() + "  tipo_resposta: " + pergunta.getTipoResposta() );
            cursor.close();
            database.close(); // whenever you get a readable/writable db you
            // should close it after using
            return true;
        } else {
            cursor.close();
            database.close(); // whenever you get a readable/writable db you
            // should close it after using
            return false;
        }
    }
    
    
    public static Perguntas getPergunta(Context context,String pergunta_texto){
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Perguntas pergunta = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + PerguntasORM.TABLE_NAME + " WHERE " + COLUMN_TEXTOPERGUNTA + "=?", new String[] { pergunta_texto });
        if (cursor != null && cursor.getCount() > 0) {
            // só tem uma pergunta porque o campo é UNIQUE no BD
            cursor.moveToFirst();
            pergunta = cursorToPergunta(cursor);
            Log.v("Debug pergunta existente BD", "Pergunta encontrada == : " + "pergunta_texto: " + pergunta.getPerguntaTexto() + "  tipo_resposta: " + pergunta.getTipoResposta() );
            cursor.close();
            database.close();
        } 
        return pergunta;
    }
        
     public static int numberOfRows(Context context ){
         DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
         SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(database, PerguntasORM.TABLE_NAME);
        database.close();
        return numRows;
     }
     
     
     public static boolean updatePergunta (Context context, Integer pergunta_id, String pergunta_texto, String pergunta_tiporesposta)
     {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PerguntasORM.COLUMN_TEXTOPERGUNTA, pergunta_texto);
        contentValues.put(PerguntasORM.COLUMN_TIPORESPOSTA, pergunta_tiporesposta);
        database.update(PerguntasORM.TABLE_NAME, contentValues, PerguntasORM.COLUMN_PERGUNTA_ID+"=? ", new String[] { Integer.toString(pergunta_id) } );
        database.close();
        return true;
     }

     public static boolean deletePergunta (Context context, String pergunta_texto)
     {
         DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
         SQLiteDatabase database = databaseWrapper.getReadableDatabase();
         database.delete(PerguntasORM.TABLE_NAME, PerguntasORM.COLUMN_TEXTOPERGUNTA+"=? ", new String[] { pergunta_texto });
         database.close();
         return true;
     }

}
