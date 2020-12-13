package business;

public class Localizacao {
    private int zona;
    private int prateleira;

    public Localizacao(){
        this.zona= 0;
        this.prateleira = 0;
    }

    public Localizacao(int zona, int prateleira){
        this.zona = zona;
        this.prateleira = prateleira;
    }

    public Localizacao(Localizacao localizacao){
        this.zona = localizacao.getZona();
        this.prateleira = localizacao.getPrateleira();
    }

    public int getZona() {
        return this.zona;
    }

    public int getPrateleira() {
        return this.prateleira;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public void setPrateleira(int prateleira) {
        this.prateleira = this.prateleira;
    }

    public Localizacao clone(){
        return new Localizacao(this);
    }
}
