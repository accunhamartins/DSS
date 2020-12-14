package UI;

import business.*;

import java.util.Scanner;

public class TextUI {

    private Menu menu;

    private Scanner scanner;


    public TextUI() {
        String[] opcoes = {
                "Comunicar Código QR",
                "Notificar recolha de palete",
                "Notificar entrega de palete",
                "Consultar listagem de localização"};
        this.menu = new Menu(opcoes);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        new Localizacao();
        new Material();
        new Palete();
        new Prateleira();
        new QRCode();
        new Robot();
        System.out.println("Teste");
}
}

