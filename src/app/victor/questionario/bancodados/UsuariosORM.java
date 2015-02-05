package app.victor.questionario.bancodados;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UsuariosORM {

    private static final String TAG = "UsuariosORM";

    private static final String TABLE_NAME = "usuarios";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_USUARIO_ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String COLUMN_USUARIO_ID = "usuario_id";

    private static final String COLUMN_NOME_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_NOME = "usuario_nome";

    private static final String COLUMN_SENHA_TYPE = "TEXT NOT NULL";
    private static final String COLUMN_SENHA = "usuario_senha";

    private static final String CONSTRAINT_UNIQUE = "UNIQUE";
    private static final String UNIQUE_CONDITIONS = "ON CONFLICT ABORT";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_USUARIO_ID + " " + COLUMN_USUARIO_ID_TYPE + COMMA_SEP + COLUMN_NOME
            + " " + COLUMN_NOME_TYPE + COMMA_SEP + COLUMN_SENHA + " " + COLUMN_SENHA_TYPE + COMMA_SEP + CONSTRAINT_UNIQUE + "(" + COLUMN_NOME + ") " + UNIQUE_CONDITIONS + ")";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertUsuario(Context context, Usuarios usuario) {

        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        ContentValues values = postToContentValues(usuario);
        long usuarioID = database.insert(UsuariosORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new Usuario with ID: " + usuarioID);
        Log.v("Debug Inserção usuario BD", "Usuario inserido == : " + "usuario: " + usuario.getNome() + "  senha: " + usuario.getSenha());
        database.close(); // whenever you get a readable/writable db you should
        // close it after using
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues postToContentValues(Usuarios usuario) {
        ContentValues values = new ContentValues();
        values.put(UsuariosORM.COLUMN_NOME, usuario.getNome());
        values.put(UsuariosORM.COLUMN_SENHA, usuario.getSenha());
        return values;
    }

    public static List<Usuarios> getUsuarios(Context context) {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + UsuariosORM.TABLE_NAME, null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Usuarios...");
        List<Usuarios> usuariosList = new ArrayList<Usuarios>();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Usuarios usuario = cursorToUsuario(cursor);
                usuariosList.add(usuario);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }

        cursor.close();
        database.close(); // whenever you get a readable/writable db you should
        // close it after using

        return usuariosList;
    }

    /**
     * Populates a Post object with data from a Cursor
     * 
     * @param cursor
     * @return
     */
    private static Usuarios cursorToUsuario(Cursor cursor) {
        Usuarios usuario = new Usuarios();
        usuario.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USUARIO_ID)));
        usuario.setNome(cursor.getString(cursor.getColumnIndex(COLUMN_NOME)));
        usuario.setSenha(cursor.getString(cursor.getColumnIndex(COLUMN_SENHA)));

        return usuario;
    }

    public static boolean checkUsuarioExisteBanco(Context context, String nome_usuario) throws SQLException {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + UsuariosORM.TABLE_NAME + " WHERE " + COLUMN_NOME + "=?", new String[] { nome_usuario });
        if (cursor != null && cursor.getCount() > 0) {
            // só tem um usuário porque o campo é UNIQUE no BD
            cursor.moveToFirst();
            Usuarios usuario = cursorToUsuario(cursor);
            Log.v("Debug usuario existente BD", "Usuario encontrado == : " + "usuario: " + usuario.getNome() + "  senha: " + usuario.getSenha());
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

    /*
     * checa se os dados de login condizem com os dados inscritos no banco de
     * dados
     */
    public static String loginValido(Context context, String nome_usuario, String senha) throws SQLException {
        DatabaseWrapper databaseWrapper = DatabaseWrapper.getInstance(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();
        // Cursor cursor = database.rawQuery("SELECT * FROM " +
        // UsuariosORM.TABLE_NAME + " WHERE "+ COLUMN_NOME + "=? AND " +
        // COLUMN_SENHA + "=?" , new String[]{nome_usuario, senha});
        Cursor cursor = database.rawQuery("SELECT * FROM " + UsuariosORM.TABLE_NAME + " WHERE " + COLUMN_NOME + "=?", new String[] { nome_usuario });
        if (cursor != null && cursor.getCount() > 0) {
            // só tem um usuário porque o campo é UNIQUE no BD
            cursor.moveToFirst();
            Usuarios usuario = cursorToUsuario(cursor);
            Log.v("Debug loginValido BD", "Usuario encontrado == : " + "usuario: " + usuario.getNome() + "  senha: " + usuario.getSenha());

            if (!senha.trim().equals(usuario.getSenha())) {
                Log.v("Debug loginValido BD", "Senha invalida == : " + "senha digitada: " + senha.trim() + "  senha do banco: " + usuario.getSenha());
                cursor.close();
                database.close();
                String erro = "Senha incorreta.\n";
                return erro;
            } else {
                Log.v("Debug loginValido BD", "Login efetuado com sucesso");
                cursor.close();
                database.close();
                String erro = "";
                return erro;
            }
        } else {
            Log.v("Debug loginValido BD", "usuario invalido == : " + "usuario digitado: " + nome_usuario.trim() + " não existe no banco de dados.");
            cursor.close();
            database.close();
            String erro = "Usuário não existe no banco.\n";
            return erro;
        }
    }

}
