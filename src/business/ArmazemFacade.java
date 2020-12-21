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

    public void preenchePrateleiras(){
        for(Palete p: paletes.values()){
            if(p.getLocalizacao().getZona() > 0 && p.getLocalizacao().getPrateleira() > 0){
                if(p.getLocalizacao().getZona() == 1) corredor1[p.getLocalizacao().getPrateleira() - 1] = 1;
                else corredor2[p.getLocalizacao().getPrateleira() - 1] = 1;
            }
        }
    }

    public void adicionaGestor(String password, String nome) throws GestorInvalidoException {
        if(this.gestores.containsKey(password)) throw new GestorInvalidoException();
        this.gestores.put(password, new Gestor(password, nome));
    }

    public void adicionaRobot(int ID) throws RobotInvalidoException {
        if(this.robots.containsKey(ID)) throw new RobotInvalidoException();
        this.robots.put(ID, new Robot(ID, 1));
    }

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

    public void exiteGestor(String password) throws LoginInvalidoException{
       if(!this.gestores.containsKey(password)) throw new LoginInvalidoException();
    }

    public Gestor getGestor(String password){
        return this.gestores.get(password).clone();
    }

    public Map<String, Gestor> getGestores() {
        Map<String, Gestor> gestores = new HashMap<>();
        for(Gestor g: gestores.values()){
            gestores.put(g.getPassword(), g.clone());
        }
        return gestores;
    }

    public Map<Integer, Robot> getRobots() {
        Map<Integer, Robot> robots = new HashMap<>();
        for(int i: robots.keySet()){
            robots.put(i, this.robots.get(i));
        }
        return robots;
    }

    public Map<Integer, Palete> getPalete() {
        Map<Integer, Palete> paletes = new HashMap<>();
        for(int i: paletes.keySet()){
            paletes.put(i, this.paletes.get(i).clone());
        }
        return paletes;
    }

    public void setCorredores(Map<Integer, Palete> paleteRegistada) {
        this.paletes=new HashMap<>();
        for(Palete p: paleteRegistada.values()){
            this.paletes.put(p.getID(),p.clone());
        }
    }

    public void setRobots(Map<Integer, Robot> robot) {
        this.robots =new HashMap<>();
        for(Robot r: robot.values()){
            this.robots.put(r.getId(),r);
        }
    }


    public void registaPalete(QRCode cod) throws RegistoInvalidoException {
        LeitorQR qr = new LeitorQR();
        qr.setPaleteRegistada(paletes);
        Palete nova = qr.registaPalete(cod);
        paletes.put(nova.getID(), nova);
    }

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

    public String localizaPalete(){
        StringBuilder sb = new StringBuilder();
        if(paletes.size() == 0) return sb.append("Não há paletes!").toString();
        sb.append("LOCALIZAÇÃO DAS PALETES\n");
        for(Palete p: paletes.values()){
            sb.append("\nID " + p.getID());
            if(p.getLocalizacao().getPrateleira() == 0 && p.getLocalizacao().getZona() == 0){
                sb.append("\nZona de Descarga");
                sb.append("\n" + "Material -> " + p.getMaterial().getDesignacao());
            }
            else if(p.getLocalizacao().getPrateleira() == -1 && p.getLocalizacao().getZona() == -1){
                sb.append("\nZona de Carga");
                sb.append("\n" + "Material -> " + p.getMaterial().getDesignacao());
            }
            else {
                sb.append("\n" + "Corredor -> " + p.getLocalizacao().getZona());
                sb.append("\n" + "Prateleira -> " + p.getLocalizacao().getPrateleira());
                sb.append("\n" + "Material -> " + p.getMaterial().getDesignacao());
            }
            sb.append("\n" + "Peso -> " + p.getPeso());
            sb.append("\n" + "Preço -> " + (p.getMaterial().getPrecoUnitario() * p.getPeso()) + "\n\n");
        }
        return sb.toString();
    }

    public int contaPaletesArmazenadas() {
        int count = 0;
        for (Palete p : paletes.values()) {
            if (p.getLocalizacao().getZona() > 0 && p.getLocalizacao().getPrateleira() > 0) {
                count++;
            }
        }
        return count;
    }

    public int contaCorredor(){
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if(corredor1[i] == 1) count++;
        }
        return count;
    }

    public void ordenaTransporte(int ID){
        Robot robot = robots.get(0);
        for(Palete p: this.paletes.values()){
            if(p.getID() == ID && robot.isDisponivel() == 1) {
                if (contaPaletesArmazenadas() == 10) return;
                else if(contaCorredor() < 5){
                    Random rand = new Random();
                    int rand_int2 = rand.nextInt(5);
                    while(corredor1[rand_int2] == 1){
                        rand_int2 = rand.nextInt(5);
                    }
                        p.setLocalizacao(new Localizacao(1, rand_int2 + 1));
                    corredor1[rand_int2] = 1;
                    this.paletes.put(p.getID(), p.clone());
                    System.out.println("Palete transportada para corredor " + 1 + " prateleira " + (rand_int2 + 1));
                }
                else {
                    Random rand = new Random();
                    int rand_int2 = rand.nextInt(5);
                    while(corredor2[rand_int2] == 1){
                        rand_int2 = rand.nextInt(5);
                    }
                    p.setLocalizacao(new Localizacao(2, rand_int2 + 1));
                    corredor2[rand_int2] = 1;
                    this.paletes.put(p.getID(), p.clone());
                    System.out.println("Palete transportada para corredor " + 2 + " prateleira " + (rand_int2 + 1));
                }
            }
            else if(robot.isDisponivel() == 0){
                System.out.println("Não existem robots disponíveis. Por favor, aguarde");
            }

        }
    }

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

    public void ordenaEntrega(int ID){
        Robot robot = robots.get(0);
        for(Palete p: this.paletes.values()){
            if(p.getID() == ID && robot.isDisponivel() == 1){
                if(p.getLocalizacao().getZona() == 1) corredor1[p.getLocalizacao().getPrateleira() - 1] = 0;
                else corredor2[p.getLocalizacao().getPrateleira() - 1] = 0;
                p.setLocalizacao(new Localizacao(-1, -1));
                this.paletes.put(p.getID(), p.clone());
                System.out.println("Palete transportada para zona de carga!");
            }
            else if(robot.isDisponivel() == 0){
                System.out.println("Não existem robots disponíveis. Por favor, aguarde");
            }
        }
    }

}
