package business;

public class Robot {
    private int id;
    private boolean disponivel;
    private String codTransportar;


    public Robot(){
        this.id = 0;
        this.disponivel = true;
        this.codTransportar = null;
    }

    public Robot(int id, boolean disponivel, String codTransportar){
        this.id = id;
        this.disponivel = disponivel;
        this.codTransportar = codTransportar;
    }

    public String getCodTransportar() {
        return codTransportar;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }
}
