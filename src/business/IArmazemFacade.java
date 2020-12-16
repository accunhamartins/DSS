package business;

import Exceptions.RegistoInvalidoException;

import java.util.Map;

public interface IArmazemFacade {


    public Map<String, Gestor> getGestores();
    public Map<Integer, Palete> getPalete();
    public Map<Integer, Robot> getRobots();
    public void setRobot(Map<Integer, Robot> robot);
    public void registaPalete(QRCode cod) throws RegistoInvalidoException;
    public String printPalete();
    public String localizaPalete();
    public void ordenaTransporte();

}
