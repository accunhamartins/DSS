package business;

import Exceptions.RegistoInvalidoException;
import data.PaleteDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ArmazemFacade implements IArmazemFacade{
    private Map<Integer, Palete> corredores;
    private Map<String, Gestor> gestores;
    private Map<Integer, Robot> robots;
    private int[] corredor1 = new int[5];
    private int[] corredor2 = new int[5];

    public ArmazemFacade (){
        //this.corredores = PaleteDAO.getInstance();
        this.corredores = new HashMap<>();
        this.gestores = new HashMap<>();
        this.robots = new HashMap<>();
        for(int i = 0; i < 5; i++){
            corredor1[i] = 0;
            corredor2[i] = 0;
        }
    }

    public Map<String, Gestor> getGestores() {
        Map<String, Gestor> gestores = new HashMap<>();
        for(String s: gestores.keySet()){
            gestores.put(s, this.gestores.get(s).clone());
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
            corredores.put(i, this.corredores.get(i).clone());
        }
        return paletes;
    }

    public void setCorredores(Map<Integer, Palete> paleteRegistada) {
        this.corredores=new HashMap<>();
        for(Palete p: paleteRegistada.values()){
            this.corredores.put(p.getID(),p.clone());
        }
    }

    public void setRobot(Map<Integer, Robot> robot) {
        this.robots =new HashMap<>();
        for(Robot r: robots.values()){
            this.robots.put(r.getId(),r);
        }
    }

    public void registaPalete(QRCode cod) throws RegistoInvalidoException {
        LeitorQR qr = new LeitorQR();
        qr.setPaleteRegistada(corredores);
        qr.registaPalete(cod);
        setCorredores(qr.getPaleteRegistada());
    }

    public String printPalete(){
        StringBuilder sb = new StringBuilder();
        sb.append("PALETES REGISTADAS");
        for(Palete p: corredores.values()){
            sb.append("\nID " + p.getID()).append("\n" + "Corredor " + p.getLocalizacao().getZona());
            sb.append("\n" + "Prateleira " + p.getLocalizacao().getPrateleira());
            sb.append("\n" + "Material " + p.getMaterial().getDesignacao());
            sb.append("\n" + "Peso " + p.getPeso());
            sb.append("\n" + "Preço " + (p.getMaterial().getPrecoUnitario() * p.getPeso()));
        }
        return sb.toString();
    }

    public String localizaPalete(){
        StringBuilder sb = new StringBuilder();
        if(corredores.size() == 0) return sb.append("Não há paletes!").toString();
        sb.append("LOCALIZAÇÃO DAS PALETES\n");
        for(Palete p: corredores.values()){
            sb.append("\nID " + p.getID()).append("\n" + "Corredor " + p.getLocalizacao().getZona());
            sb.append("\n" + "Prateleira " + p.getLocalizacao().getPrateleira());
            sb.append("\n" + "Material " + p.getMaterial().getDesignacao());
            sb.append("\n" + "Peso " + p.getPeso());
            sb.append("\n" + "Preço " + (p.getMaterial().getPrecoUnitario() * p.getPeso()));
        }
        return sb.toString();
    }

    public void ordenaTransporte(){
        Robot robot = robots.get(0);
        for(Palete p: this.corredores.values()){
            if(p.getLocalizacao().getZona() == 0 && p.getLocalizacao().getPrateleira() == 0){
                Random rand = new Random();
                int rand_int1 = rand.nextInt(1);
                int rand_int2 = rand.nextInt(4);
                while(corredor1[rand_int1] == 1 && corredor1[rand_int2] == 1){
                     rand_int1 = rand.nextInt(4);
                    rand_int2 = rand.nextInt(4);
                }
                p.setLocalizacao(new Localizacao(rand_int1 + 1, rand_int2 + 1));
                this.corredores.put(p.getID(), p.clone());
                System.out.println("Palete transportada para corredor " + (rand_int1 + 1) + " prateleira " + (rand_int2 + 1));
            }
        }
    }

}
