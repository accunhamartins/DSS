import UI.TextUI;
import data.PaleteDAO;

public class Main {

    /**
     * O método main cria a aplicação e invoca o método run()
     */
    public static void main(String[] args) {
        try {
            new TextUI().run();
        }
        catch (Exception e) {
            System.out.println("NÃO FOI POSSÍVEL ARRANCAR:  "+e.getMessage());
        }
    }

}
