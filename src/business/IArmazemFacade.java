package business;

import java.util.Map;

public interface IArmazemFacade {

    public String listaPalete();
    public Map<String, Gestor> getGestores();
    public Map<Integer, Palete> getPalete();


}
