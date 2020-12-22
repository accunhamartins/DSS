package business;

import Exceptions.*;

import java.util.Map;

public interface IArmazemFacade {

    public Map<String, Gestor> getGestores();
    public Map<Integer, Palete> getPalete();
    public Map<Integer, Robot> getRobots();
    public void setRobots(Map<Integer, Robot> robot);
    public void registaPalete(QRCode cod) throws RegistoInvalidoException;
    public String imprimeRececao() throws NoPaleteRececaoException;
    public String localizaPalete();
    public void ordenaTransporte(int ID) throws RobotIndisponivelException, RobotInvalidoException;
    public void ordenaEntrega(int ID) throws RobotIndisponivelException, RobotInvalidoException;
    public String imprimePrateleira();
    public void adicionaGestor(String password, String nome) throws GestorInvalidoException;
    public void exiteGestor(String password) throws LoginInvalidoException;
    public Gestor getGestor(String password);
    public void adicionaRobot(int ID) throws RobotInvalidoException;
    public int alteraDisponivel(int ID) throws RobotInvalidoException;
    public int contaPaletesArmazenadas();
    public String imprimeRobot();

}
