package business;

import data.PaleteDAO;

import java.util.HashMap;
import java.util.Map;

public class ArmazemFacade implements IArmazemFacade{
    private Map<Integer, Palete> corredores;
    private Map<String, Gestor> gestores;

    public ArmazemFacade (){
        //this.corredores = PaleteDAO.getInstance();
        this.corredores = new HashMap<>();
        this.gestores = new HashMap<>();
    }

    public String listaPalete(){
        String ret = null;
        return ret;
    }

    public Map<String, Gestor> getGestores() {
        Map<String, Gestor> gestores = new HashMap<>();
        for(String s: gestores.keySet()){
            gestores.put(s, this.gestores.get(s).clone());
        }
        return gestores;
    }

    public Map<Integer, Palete> getPalete() {
        Map<Integer, Palete> paletes = new HashMap<>();
        for(int i: paletes.keySet()){
            corredores.put(i, this.corredores.get(i).clone());
        }
        return paletes;
    }
}
