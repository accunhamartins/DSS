package business;

public class Robot {

    private boolean disponivel;
    private String codTransportar;


    public Robot(){
        this.disponivel = true;
        this.codTransportar = null;
    }

    public String getCodTransportar() {
        return codTransportar;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setCodTransportar(String codTransportar) {
        this.codTransportar = codTransportar;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
