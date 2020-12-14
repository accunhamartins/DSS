package business;

public class Palete {
    private QRCode code;
    private Localizacao localizacao;

    public Palete(QRCode code, Localizacao localizacao){
        this.code = code;
        this.localizacao = localizacao;
    }

    public Localizacao getLocalizacao() {
        return this.localizacao.clone();
    }

    public QRCode getCode() {
        return this.code.clone();
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public void setCode(QRCode code) {
        this.code = code;
    }
}
