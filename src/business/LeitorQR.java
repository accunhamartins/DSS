package business;

import Exceptions.RegistoInvalidoException;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class LeitorQR {

    private Map<Integer, Palete> paleteRegistada;

    public LeitorQR(){
        this.paleteRegistada = new HashMap<Integer,Palete>();
    }

    public LeitorQR(Map<Integer,Palete> paletes){
        this.paleteRegistada = new HashMap<Integer,Palete>();

        for(Palete p : paletes.values()){
            this.paleteRegistada.put(p.getID(), p.clone());
        }
    }

    public LeitorQR(LeitorQR qr){
        setPaleteRegistada(qr.getPaleteRegistada());
    }

    public Map<Integer, Palete> getPaleteRegistada() {
        Map<Integer, Palete> ret = new HashMap<>();
        for(Palete p: this.paleteRegistada.values())
        ret.put(p.getID(),p.clone());
        return ret;
    }

    public void setPaleteRegistada(Map<Integer, Palete> paleteRegistada) {
        this.paleteRegistada =new HashMap<>();
        for(Palete p: paleteRegistada.values()){
            this.paleteRegistada.put(p.getID(), p.clone());
        }
    }

    //Método que valida de um dado ID de palete existe
    public void valida(int id) throws RegistoInvalidoException{
        if (paleteRegistada.containsKey(id)) throw new RegistoInvalidoException();
    }

    //Método que valida um QRCode
    private Palete validaPalete(QRCode cod) throws RegistoInvalidoException{
        try {
            String[] campos = cod.getCodigo().split("&&", 4);
            int id = parseInt(campos[0]);
            String designacao = campos[1];
            double precoUni = parseDouble(campos[2]);
            double peso = parseDouble(campos[3]);
            try {
                valida(id);
            } catch (RegistoInvalidoException e) {
                System.out.println("Registo Inválido");
                return null;
            }
            return new Palete(id, cod, new Localizacao(0, 0), new Material(designacao, precoUni), peso);
        } catch (Exception e) {
            return null;
        }
    }

    //Método que efetua o registo de uma nova palete
    public Palete registaPalete(QRCode cod) throws RegistoInvalidoException{
        Palete p = validaPalete(cod);
        if(p == null) throw new RegistoInvalidoException();
        else
            {
            paleteRegistada.put(p.getID(), p.clone());
            return p.clone();
        }

    }

}
