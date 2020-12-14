package business;

public class Palete {
    private QRCode code;
    private Localizacao localizacao;
    private Material material;
    private double peso;
    private double preco;

    public Palete(){
        this.code = null;
        this.localizacao = null;
        this.material = null;
        this.peso = 0;
        this.preco = 0;
    }

    public Palete(QRCode code, Localizacao localizacao, Material material, double peso){
        this.code = code;
        this.localizacao = localizacao;
        this.material = material;
        this.peso = peso;
        this.preco = material.getPrecoUnitario() * this.peso;
    }

    public Palete(Palete p){
        this.code = p.getCode().clone();
        this.localizacao = p.getLocalizacao().clone();
        this.material = p.getMaterial().clone();
        this.peso = p.getPeso();
        this.preco = p.getPreco();
    }

    public Localizacao getLocalizacao() {
        return this.localizacao.clone();
    }

    public QRCode getCode() {
        return this.code.clone();
    }

    public Material getMaterial() {
        return material.clone();
    }

    public double getPeso() {
        return peso;
    }

    public double getPreco() {
        return preco;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public void setCode(QRCode code) {
        this.code = code;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
