package business;

public class Gestor {
    private String password;
    private String nome;

    public Gestor(){
        this.nome = "";
        this.password= "";
    }

    public Gestor(String password, String nome){
        this.password = password;
        this.nome = nome;
    }

    public Gestor(Gestor g){
        this.password = g.getPassword();
        this.nome = g.getNome();
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gestor clone(){
        return new Gestor(this);
    }

}
