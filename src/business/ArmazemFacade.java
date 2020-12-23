package business;

import Exceptions.*;
import data.GestorDAO;
import data.PaleteDAO;
import data.RobotDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ArmazemFacade implements IArmazemFacade{
    private Map<Integer, Palete> paletes;
    private Map<String, Gestor> gestores;
    private Map<Integer, Robot> robots;
    private int[] corredor1 = new int[5];
    private int[] corredor2 = new int[5];

    public ArmazemFacade (){
        this.paletes = PaleteDAO.getInstance();
        this.gestores = GestorDAO.getInstance();
        this.robots = RobotDAO.getInstance();
        for(int i = 0; i < 4; i++){
            corredor1[i] = 0;
            corredor2[i] = 0;
            }
        preenchePrateleiras();
        }

    //Método que preenche os arrays relativos a cada corredor, consoante tenham, ou não, paletes nas respetivas posições
    public void preenchePrateleiras(){
        for(Palete p: paletes.values()){
            if(p.getLocalizacao().getZona() > 0 && p.getLocalizacao().getPrateleira() > 0){
                if(p.getLocalizacao().getZona() == 1) corredor1[p.getLocalizacao().getPrateleira() - 1] = 1;
                else corredor2[p.getLocalizacao().getPrateleira() - 1] = 1;
            }
        }
    }

    //Método que devolve o primeiro robot disponível
    private Robot escolheRobot() throws RobotIndisponivelException{
        for(Robot robot: this.robots.values()){
            if(robot.isDisponivel() == 1) return robot.clone();
        }
        throw new RobotIndisponivelException();
    }

    //Método que efetua o registo de um novo gestor na nossa Base de Dados
    public void adicionaGestor(String password, String nome) throws GestorInvalidoException {
        if(this.gestores.containsKey(password)) throw new GestorInvalidoException();
        this.gestores.put(password, new Gestor(password, nome));
    }

    //Método que adiciona um novo robot à base de dados
    public void adicionaRobot(int ID) throws RobotInvalidoException {
        if(this.robots.containsKey(ID)) throw new RobotInvalidoException();
        this.robots.put(ID, new Robot(ID, 1));
    }

    //Método que altera a disponibilidade de um robot. Se este está disponível (1) passa a indisponível (0) e o inverso.
    public int alteraDisponivel(int ID) throws RobotInvalidoException{
        if(!this.robots.containsKey(ID)) throw new RobotInvalidoException();
        Robot r = this.robots.get(ID);
        int i = r.isDisponivel();
        if(i == 0){
            i = 1;
            this.robots.put(ID, new Robot(ID, i));
        }
        else if(i == 1){
            i = 0;
            this.robots.put(ID, new Robot(ID, i));
        }
        return i;
    }

    // Método que indica se um gestor existe, dada uma password
    public void exiteGestor(String password) throws LoginInvalidoException{
       if(!this.gestores.containsKey(password)) throw new LoginInvalidoException();
    }

    // Método que devolve um gestor dada a sua password
    public Gestor getGestor(String password){
        return this.gestores.get(password).clone();
    }

    //Método de devolve todos os gestores registados
    public Map<String, Gestor> getGestores() {
        Map<String, Gestor> gestores = new HashMap<>();
        for(Gestor g: gestores.values()){
            gestores.put(g.getPassword(), g.clone());
        }
        return gestores;
    }

    //Método que devolve todos os robots registados
    public Map<Integer, Robot> getRobots() {
        Map<Integer, Robot> robots = new HashMap<>();
        for(Robot r: robots.values()){
            robots.put(r.getId(), r.clone());
        }
        return robots;
    }

    //Método que devolve todas as paletes registadas
    public Map<Integer, Palete> getPalete() {
        Map<Integer, Palete> paletes = new HashMap<>();
        for(Palete p: paletes.values()){
            paletes.put(p.getID(), p.clone());
        }
        return paletes;
    }

    //Método que altera as paletes registadas
    public void setCorredores(Map<Integer, Palete> paleteRegistada) {
        this.paletes=new HashMap<>();
        for(Palete p: paleteRegistada.values()){
            this.paletes.put(p.getID(),p.clone());
        }
    }

    //Método que altera os robots registados
    public void setRobots(Map<Integer, Robot> robot) {
        this.robots =new HashMap<>();
        for(Robot r: robot.values()){
            this.robots.put(r.getId(),r);
        }
    }

    //Método que efetua o registo de uma nova palete
    public void registaPalete(QRCode cod) throws RegistoInvalidoException {
        LeitorQR qr = new LeitorQR();
        qr.setPaleteRegistada(paletes);
        Palete nova = qr.registaPalete(cod);
        paletes.put(nova.getID(), nova);
    }

    //Método que devolve em formato de String as paletes na zona de carga
    public String imprimeRececao() throws NoPaleteRececaoException{
        int count = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("PALETES REGISTADAS");
        for(Palete p: paletes.values()){
           if(p.getLocalizacao().getPrateleira() == 0 && p.getLocalizacao().getZona() == 0){
               sb.append("\nID = " + p.getID());
               count++;
           }
        }
        if(count == 0) throw new NoPaleteRececaoException();
        return sb.toString();
    }

    //Método que devolve no formato de String a localização de todas as paletes, para serem apresentadas a um Gestor
    public String localizaPalete(){
        StringBuilder sb = new StringBuilder();
        if(paletes.size() == 0) return sb.append("NÃO HÁ PALETES!").toString();
        sb.append("LOCALIZAÇÃO DAS PALETES\n");
        for(Palete p: paletes.values()){
            sb.append("\nID " + p.getID());
            if(p.getLocalizacao().getPrateleira() == 0 && p.getLocalizacao().getZona() == 0){
                sb.append("\nZONA DE DESCARGA");
                sb.append("\n" + "MATERIAL -> " + p.getMaterial().getDesignacao());
            }
            else if(p.getLocalizacao().getPrateleira() == -1 && p.getLocalizacao().getZona() == -1){
                sb.append("\nZONA DE CARGA");
                sb.append("\n" + "MATERIAL-> " + p.getMaterial().getDesignacao());
            }
            else {
                sb.append("\n" + "CORREDOR -> " + p.getLocalizacao().getZona());
                sb.append("\n" + "PRATELEIRA -> " + p.getLocalizacao().getPrateleira());
                sb.append("\n" + "MATERIAL -> " + p.getMaterial().getDesignacao());
            }
            sb.append("\n" + "PESO -> " + p.getPeso());
            sb.append("\n" + "PREÇO -> " + (p.getMaterial().getPrecoUnitario() * p.getPeso()) + "\n\n");
        }
        return sb.toString();
    }

    //Método que devolve o número de paletes armazenadas nos corredores
    public int contaPaletesArmazenadas() {
        int count = 0;
        for (Palete p : paletes.values()) {
            if (p.getLocalizacao().getZona() > 0 && p.getLocalizacao().getPrateleira() > 0) {
                count++;
            }
        }
        return count;
    }

    //Método que devolve o número de paletes no corredor 1
    public int contaCorredor(){
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if(corredor1[i] == 1) count++;
        }
        return count;
    }

    //Método que efetua o transporte de uma palete da zona de descarga para um corredor e prateleira
    public void ordenaTransporte(int ID) throws RobotIndisponivelException, RobotInvalidoException{
        try {
            Robot robot = escolheRobot();
            for (Palete p : this.paletes.values()) {
                if (p.getID() == ID) {
                    alteraDisponivel(robot.getId());
                    if (contaPaletesArmazenadas() == 10) return;
                    else if (contaCorredor() < 5) {
                        System.out.println(contaCorredor());
                        Random rand = new Random();
                        int rand_int2 = rand.nextInt(5);
                        while (corredor1[rand_int2] == 1) {
                            rand_int2 = rand.nextInt(5);
                        }
                        p.setLocalizacao(new Localizacao(1, rand_int2 + 1));
                        corredor1[rand_int2] = 1;
                        this.paletes.put(p.getID(), p.clone());
                        System.out.println("PALETE TRANSPORTADA PARA O CORREDOR " + 1 + " PRATELEIRA " + (rand_int2 + 1) + " PELO ROBOT NR " + robot.getId());
                    } else {
                        Random rand = new Random();
                        int rand_int2 = rand.nextInt(5);
                        while (corredor2[rand_int2] == 1) {
                            rand_int2 = rand.nextInt(5);
                        }
                        p.setLocalizacao(new Localizacao(2, rand_int2 + 1));
                        corredor2[rand_int2] = 1;
                        this.paletes.put(p.getID(), p.clone());
                        System.out.println("PALETE TRANSPORTADA PARA O CORREDOR " + 2 + " PRATELEIRA " + (rand_int2 + 1) + " PELO ROBOT NR " + robot.getId());
                    }
                }
            }
        } catch(RobotIndisponivelException | RobotInvalidoException e){
            System.out.println("NÃO EXISTEM ROBOTS DISPONÍVEIS. POR FAVOR, AGUARDE");
        }
    }

    //Método que devolve no formato de String o ID das paletes armazenadas nas prateleiras
    public String imprimePrateleira(){
        StringBuilder sb = new StringBuilder();
        sb.append("PALETES ARMAZENADAS");
        for(Palete p: paletes.values()){
            if(p.getLocalizacao().getPrateleira() != 0 && p.getLocalizacao().getZona() != 0 && p.getLocalizacao().getPrateleira() != -1 && p.getLocalizacao().getZona() != -1){
                sb.append("\nID = " + p.getID());
            }
        }
        return sb.toString();
    }

    //Método que efetua o transporte de uma palete de uma prateleira para a zona de carga
    public void ordenaEntrega(int ID) throws RobotIndisponivelException, RobotInvalidoException {
        try{
        Robot robot = this.escolheRobot();
        for(Palete p: this.paletes.values()) {
            if (p.getID() == ID ) {
                alteraDisponivel(robot.getId());
                if (p.getLocalizacao().getZona() == 1) corredor1[p.getLocalizacao().getPrateleira() - 1] = 0;
                else corredor2[p.getLocalizacao().getPrateleira() - 1] = 0;
                p.setLocalizacao(new Localizacao(-1, -1));
                this.paletes.put(p.getID(), p.clone());
                System.out.println("PALETE TRANSPORTADA PARA ZONA DE CARGA PELO ROBOT NR " + robot.getId());
            }
        }
        } catch (RobotIndisponivelException | RobotInvalidoException e){
            System.out.println("NÃO EXISTEM ROBOTS DISPONÍVEIS. POR FAVOR, AGUARDE");
        }
    }

    //Método que devolve no formato de String o ID dos robots e a sua disponibilidade
    public String imprimeRobot(){
        StringBuilder sb = new StringBuilder();
        for(Robot r: this.robots.values()){
            sb.append("\nID -> " + r.getId());
            sb.append("\nDISPONÍVEL -> ");
            if(r.isDisponivel() == 1){
                sb.append("TRUE");
            }
            else sb.append("FALSE");
        }
        return sb.toString();
    }

}
