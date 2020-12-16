package UI;

import Exceptions.RegistoInvalidoException;
import business.*;

import java.util.Map;
import java.util.Scanner;

public class TextUI {

    private Menu menu;
    private IArmazemFacade model;
    private Scanner scanner;


    public TextUI() {
        String[] opcoes = {
                "Comunicar Código QR",
                "Notificar recolha de palete",
                "Notificar entrega de palete",
                "Consultar listagem de localização"};
        this.menu = new Menu(opcoes);
        this.model = new ArmazemFacade();
        this.scanner = new Scanner(System.in);
    }

    public void run() throws RegistoInvalidoException{
        do {
            menu.executa();
            switch (menu.getOpcao()) {
                case 1:
                    registaPalete();
                    break;
                case 4:
                    listaPalete();
                    break;
            }
        } while (menu.getOpcao()!=0); // A opção 0 é usada para sair do menu.
        System.out.println("Até breve!...");
    }

    private void registaPalete() throws RegistoInvalidoException {
        LeitorQR qr = new LeitorQR();
        QRCode cod = new QRCode();
        System.out.println("Insira QRCode: ");
        String codigo = scanner.nextLine();
        cod.setCodigo(codigo);
        try {
            qr.registaPalete(cod);
        } catch (RegistoInvalidoException e){
            System.out.println("O código inserido é inválido");
        }
    }

    private void listaPalete(){
        System.out.println("Insira password: ");
        String password = scanner.nextLine();
        Map<String, Gestor> gestores = this.model.getGestores();

        if(gestores.containsKey(password)) {
            Gestor g = gestores.get(password);
            System.out.println("Seja Bem-Vindo Sr. " + g.getNome());
            Map<Integer, Palete> paletes = this.model.getPalete();
            System.out.println(paletes);
        }
        else{
            System.out.println("Password inválida!");
        }
    }

}

