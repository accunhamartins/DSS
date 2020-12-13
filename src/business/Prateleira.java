package business;

public class Prateleira {
    private Localizacao localizacao;
    private boolean livre;

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
