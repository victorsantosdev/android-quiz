package app.victor.questionario.bancodados;

public class Usuarios {
    private int usuario_id;
    private String usuario_nome;
    private String usuario_senha;
 
    public Usuarios(){}
 
    public Usuarios(String usuario_nome, String usuario_senha) {
        super();
        this.usuario_nome = usuario_nome;
        this.usuario_senha = usuario_senha;
    } 
     
    public int getId() {
        return usuario_id;
    }
    
    public void setId(int usuario_id) {
        this.usuario_id = usuario_id;
    }
    
    public String getNome() {
        return usuario_nome;
    }
    
    public void setNome(String usuario_nome) {
        this.usuario_nome = usuario_nome;
    }
    
    public void setSenha(String usuario_senha) {
        this.usuario_senha = usuario_senha;
    }
    
    public String getSenha() {
        return usuario_senha;
    }
    
    @Override
    public String toString() {
        return "Usuarios [usuario_id=" + usuario_id + " nome_usuario= " + usuario_nome + ", senha=" + usuario_senha + "]";
    }
}
