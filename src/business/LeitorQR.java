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
            this.paleteRegistada.put(p.getID(),p.clone());
        }
    }

    public void valida(int id, int prateleira, int corredor) throws RegistoInvalidoException{
        if (paleteRegistada.containsKey(id) || prateleira != 0 || corredor != 0) throw new RegistoInvalidoException();
    }


    private Palete validaPalete(QRCode cod) throws RegistoInvalidoException{
        try {
            String[] campos = cod.getCodigo().split("&&", 6);
            int id = parseInt(campos[0]);
            int corredor = parseInt(campos[1]);
            int prateleira = parseInt(campos[2]);
            String designacao = campos[3];
            double precoUni = parseDouble(campos[4]);
            double peso = parseDouble(campos[5]);
            try {
                valida(id, corredor, prateleira);
            } catch (RegistoInvalidoException e) {
                System.out.println("Registo Inválido");
                return null;
            }
            return new Palete(id, cod, new Localizacao(corredor, prateleira), new Material(designacao, precoUni), peso);
        } catch (Exception e) {
            return null;
        }
    }

    public void registaPalete(QRCode cod) throws RegistoInvalidoException{
        Palete p = validaPalete(cod);
        if(p == null) throw new RegistoInvalidoException();
        else
            {
            paleteRegistada.put(p.getID(), p.clone());
        }

    }

}
