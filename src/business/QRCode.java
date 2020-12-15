package business;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class QRCode {
    private String codigo;

    public QRCode(){
        this.codigo = null;
    }

    public QRCode(String code){
        this.codigo = code;
    }

    public QRCode(QRCode code){
        this.codigo = code.getCodigo();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public QRCode clone() {
        return new QRCode(this);
    }

}
