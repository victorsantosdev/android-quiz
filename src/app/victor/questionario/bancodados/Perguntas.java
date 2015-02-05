package app.victor.questionario.bancodados;

public class Perguntas {
    private int pergunta_id;
    private String pergunta_texto;
    private String pergunta_tiporesposta;
 
    public Perguntas(){}
 
    public Perguntas(String pergunta_texto, String pergunta_tiporesposta) {
        super();
        this.pergunta_texto = pergunta_texto;
        this.pergunta_tiporesposta = pergunta_tiporesposta;
    } 
     
    public int getId() {
        return pergunta_id;
    }
    
    public void setId(int pergunta_id) {
        this.pergunta_id = pergunta_id;
    }
    
    public String getPerguntaTexto() {
        return pergunta_texto;
    }
    public void setPerguntaTexto(String pergunta_texto) {
        this.pergunta_texto = pergunta_texto;
    }
    
    public String getTipoResposta() {
        return pergunta_tiporesposta;
    }
    public void setTipoResposta(String pergunta_tiporesposta) {
        this.pergunta_tiporesposta = pergunta_tiporesposta;
    }
 
    @Override
    public String toString() {
        return "EstruturaPerguntas [pergunta_id=" + pergunta_id + ", pergunta_texto=" + pergunta_texto + ", pergunta_tiporesposta=" + pergunta_tiporesposta + "]";
    }
}
