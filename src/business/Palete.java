package business;

public class Palete {
    private String code;
    private Localizacao localizacao;

    public Palete(String code, Localizacao localizacao){
        this.code = code;
        this.localizacao = localizacao;
    }

    public Localizacao getLocalizacao() {
        return this.localizacao.clone();
    }

    public String getCode() {
        return code;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
