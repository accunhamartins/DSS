package business;

public class Material {
    private String designacao;
    private double precoUnitario;


    public Material(){
        this.designacao = null;
        this.precoUnitario= 0;
    }

    public Material(String designacao, double precoUnitario){
        this.designacao = designacao;
        this.precoUnitario = precoUnitario;
    }

    public Material(Material material){
        this.designacao = material.getDesignacao();
        this.precoUnitario = material.getPrecoUnitario();
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public String getDesignacao() {
        return designacao;
    }

    public void setDesignacao(String designacao) {
        this.designacao = designacao;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Material clone(){
        return  new Material(this);
    }

}
