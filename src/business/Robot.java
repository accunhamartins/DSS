package business;

public class Robot {
    private int id;
    private int disponivel;


    public Robot(){
        this.id = 0;
        this.disponivel = 1;
    }

    public Robot(int id, int disponivel){
        this.id = id;
        this.disponivel = disponivel;
    }

    public int getId() {
        return id;
    }

    public int isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(int disponivel) {
        this.disponivel = disponivel;
    }

    public void setId(int id) {
        this.id = id;
    }
}
