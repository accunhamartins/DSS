package UI;

import Exceptions.*;
import business.*;


import java.util.Scanner;

public class TextUI {

    private Menu menu;
    private IArmazemFacade model;
    private Scanner scanner;

    public TextUI() {
        String[] opcoes = {
                "COMUNICAR QRCODE",
                "NOTIFICAR RECOLHA DE PALETE",
                "NOTIFICAR ENTREGA DE PALETE",
                "CONSULTAR LISTAGEM DE PALETES",
                "REGISTAR UM NOVO GESTOR",
                "ADICIONAR UM ROBOT",
                "ALTERAR DISPONIBILIDADE DE UM ROBOT"};
        this.menu = new Menu(opcoes);
        this.model = new ArmazemFacade();
        this.scanner = new Scanner(System.in);
    }

    public void run() throws RegistoInvalidoException, GestorInvalidoException, LoginInvalidoException, RobotInvalidoException, RobotIndisponivelException {
        do {
            menu.executa();
            switch (menu.getOpcao()) {
                case 1:
                    registaPalete();
                    break;
                case 2:
                    recolhePalete();
                    break;
                case 3:
                    entregaPalete();
                    break;
                case 4:
                    listaPalete();
                    break;
                case 5:
                    adicionaGestor();
                    break;
                case 6:
                    adicionaRobot();
                    break;
                case 7:
                    alteraDisponivel();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção Incorreta");
                    break;
            }
        } while (menu.getOpcao()!=0); // A opção 0 é usada para sair do menu.
        System.out.println("ATÉ BREVE!!!");
    }

    //Método que efetua o registo de um novo Gestor
    private void adicionaGestor() {
        System.out.println("\nREGISTO DE UM NOVO GESTOR\n");
        System.out.println("INSIRA O SEU CÓDIGO DE ACESSO");
        String password = null;
        password = scanner.nextLine();
        System.out.println("INSIRA O SEU NOME");
        String nome= null;
        nome = scanner.nextLine();
        try {
            this.model.adicionaGestor(password, nome);
            System.out.println("NOVO GESTOR REGISTADO COM SUCESSO!");
        } catch (GestorInvalidoException e){
            System.out.println("O CÓDIGO DE ACESSO NÃO É VÁLIDO");
        }
    }

    //Método que regista uma nova palete
    private void registaPalete(){
        QRCode cod = new QRCode();
        System.out.println("INSIRA QRCODE: ");
        String codigo = null;
        codigo = scanner.nextLine();
        cod.setCodigo(codigo);
        try {
            this.model.registaPalete(cod);
            System.out.println("REGISTADO COM SUCESSO");
        } catch (RegistoInvalidoException e){
            System.out.println("O CÓDIGO INSERIDO É INVÁLIDO");
        }
    }

    //Método que efetua a listagem de todas as paletes
    private void listaPalete(){
        System.out.println("INSIRA CÓDIGO DE ACESSO ");
        String password = "";
        password = scanner.nextLine();
       try {
            this.model.exiteGestor(password);
            Gestor g = this.model.getGestor(password);
            System.out.println("SEJA BEM VINDO SR. " + g.getNome());
            System.out.println(this.model.localizaPalete());
        }
        catch(LoginInvalidoException e){
            System.out.println("CÓDIGO DE ACESSO INCORRETO!");
        }
    }

    //Método que efetua o transporte de uma palete da zona de descarga para uma prateleira
    private void recolhePalete(){
        try {
            if (this.model.contaPaletesArmazenadas() == 10) {
                System.out.println("\nO ARMAZÉM ESTÁ CHEIO\n");
                return;
            } else {
                System.out.println(this.model.imprimeRececao());
                System.out.println("INSIRA ID DA PALETE A TRANSPORTAR: ");
                int ID = -2;
                ID = scanner.nextInt();
                try {
                    this.model.ordenaTransporte(ID);
                } catch(RobotInvalidoException | RobotIndisponivelException e){

                }
            }
        }
        catch (NoPaleteRececaoException e) {
                System.out.println("NÃO HÁ PALETES NA ZONA DE RECEÇÃO\n");
                return;
            }
    }

    //Método que efetua o transporte de uma palete de uma prateleira para a zona de carga
    public void entregaPalete(){
        if(this.model.contaPaletesArmazenadas() == 0){
            System.out.println("NÃO EXISTEM PALETES ARMAZENADAS\n");
            return;
        }
        else {
            System.out.println(this.model.imprimePrateleira());
            System.out.println("INSIRA ID DE PALETE A TRANSPORTAR: ");
            int ID = -2;
            ID = scanner.nextInt();
            try {
            this.model.ordenaEntrega(ID);
            } catch(RobotInvalidoException | RobotIndisponivelException e){

            }
        }
    }

    //Método que adiciona um novo Robot
    private void adicionaRobot(){
        System.out.println("\nINSERIR NOVO ROBOT\n");
        System.out.println("INSIRA O ID DO NOVO ROBOT\n");
        int ID = 0;
        ID = scanner.nextInt();
        try{
            this.model.adicionaRobot(ID);
            System.out.println("ROBOT ADICIONADO COM SUCESSO");
        } catch (RobotInvalidoException e){
            System.out.println("O ID DO ROBOT É INVÁLIDO");
        }
    }

    //Método que altera a disponibilidade de um Robot
    private void alteraDisponivel(){
        System.out.println("\nALTERAR DISPONIBILIDADE DE ROBOT\n");
        System.out.println(this.model.imprimeRobot());
        System.out.println("INSIRA O ID DO ROBOT\n");
        int ID = 0;
        ID = scanner.nextInt();
        try{
            int i = this.model.alteraDisponivel(ID);
            if(i == 0) {
                System.out.println("ROBOT MARCADO COMO INDISPONÍVEL");
            }
            else {
                System.out.println("ROBOT MARCADO COMO DISPONÍVEL");
            }
        } catch (RobotInvalidoException e){
            System.out.println("O ID DO ROBOT É INVÁLIDO");
        }
    }
}

