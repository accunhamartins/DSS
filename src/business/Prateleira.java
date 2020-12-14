package business;

public class Prateleira {
    private Localizacao localizacao;
    private boolean livre;

    public Prateleira(){
        this.localizacao = null;
        this.livre = true;
    }

    public Prateleira(Localizacao localizacao){
        this.localizacao = localizacao;
        this.livre = true;
    }

    public boolean isLivre() {
        return this.livre;
    }

    public void setLivre(boolean livre) {
        this.livre = livre;
    }
}
