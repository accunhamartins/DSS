package data;

import business.*;

import java.sql.*;
import java.util.*;

import static java.lang.Double.parseDouble;

public class PaleteDAO implements Map<Integer, Palete> {
    private static PaleteDAO singleton = null;

    private PaleteDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
           String sql = "CREATE TABLE IF NOT EXISTS QRCode (" +
                    "Codigo varchar(100) NOT NULL PRIMARY KEY )";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Armazem (" +
                    "ID int NOT NULL PRIMARY KEY )";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Material (" +
                    "Designacao varchar(100) NOT NULL PRIMARY KEY ," +
                    "Preco DECIMAL(5,2) NOT NULL )";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Palete (" +
                    "ID int NOT NULL PRIMARY KEY," +
                    "Armazem int NOT NULL," +
                    "QRCode varchar(100) NOT NULL," +
                    "Peso DECIMAL(5,2)," +
                    "Corredor int NOT NULL, " +
                    "Prateleira int NOT NULL, " +
                    "Material varchar(100) NOT NULL," +
                    "foreign key(QRCode) references QRCode(Codigo), " +
                    "foreign key(Armazem) references Armazem(ID), " +
                    "foreign key(Material) references Material(Designacao))" ;
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public static PaleteDAO getInstance() {
        if (PaleteDAO.singleton == null) {
            PaleteDAO.singleton = new PaleteDAO();
        }
        return PaleteDAO.singleton;
    }

    //Método que devolve o número de paletes registadas
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Palete")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    //Método que indica se o map está vazio
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    //Método que indica se uma dada palete existe, dado o seu ID
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT ID FROM Palete WHERE ID='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    //Método que indica se uma dada palete existe
    @Override
    public boolean containsValue(Object value) {
        Palete p = (Palete) value;
        return this.containsKey(p.getID());
    }

    //Método que devolve uma palete, dado o seu ID
    public Palete get(Object key) {
        Palete p = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Palete WHERE ID='"+key+"'")) {
            if (rs.next()) {
                QRCode code = null;
                Localizacao local = null;
                Material material = null;
                double peso = 0;
                code = new QRCode(rs.getString("QRCode"));
                String[] campos = code.getCodigo().split("&&", 4);
                double precoUni = parseDouble(campos[2]);
                local = new Localizacao(rs.getInt("Corredor"), rs.getInt("Prateleira"));
                material = new Material(rs.getString("Material"), precoUni);
                peso = rs.getDouble("Peso");
                  p = new Palete(rs.getInt("ID"),code, local, material, peso);
                }
            }
         catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return p;
    }

    //Método que adiciona uma palete à base de dados
    public Palete put(Integer key, Palete t) {
        Palete res = null;
        Localizacao l = t.getLocalizacao();
        Material m = t.getMaterial();
        QRCode qr = t.getCode();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate(
                    "INSERT INTO QRCode VALUES ('"+qr.getCodigo() + "'" +") " +
                            "ON DUPLICATE KEY UPDATE Codigo=VALUES(Codigo)");

            stm.executeUpdate(
                    "INSERT INTO Material VALUES ('"+m.getDesignacao() +"', '"+m.getPrecoUnitario()+ "'" +") " +
                            "ON DUPLICATE KEY UPDATE Designacao=VALUES(Designacao)");

            stm.executeUpdate(
                    "INSERT INTO Palete VALUES ('"+t.getID()+ "', '"+ 1 +"', '"+qr.getCodigo()+"', '"+t.getPeso()+"', '"
                            +l.getZona()+ "', '" + l.getPrateleira() + "', '"+ m.getDesignacao()
                            + "'" +") " +
                            "ON DUPLICATE KEY UPDATE Corredor =VALUES(Corredor), Prateleira = VALUES(Prateleira)");

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    //Método que remove uma palete da base de dados
    @Override
    public Palete remove(Object key) {
        Palete p = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement())
             {
            stm.executeUpdate("DELETE FROM Palete WHERE Id='"+key+"'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return p;
    }


    @Override
    public void putAll(Map<? extends Integer, ? extends Palete> paletes) {
        for(Palete p: paletes.values()) {
            this.put(p.getID(), p);
        }
    }

    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE Palete");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    //Método que devolve um Set com todos os ID's de paletes registadas
    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Palete WHERE ID = ?")){
            set = new HashSet<>();
            ps.setString(1, "ID");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                set.add(rs.getInt("ID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return set;
    }

    //Método que devolve uma Collection com todas as paletes na base de dados
    @Override
    public Collection<Palete> values() {
        Collection<Palete> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT ID FROM Palete")) {
            while (rs.next()) {
                String idt = rs.getString("ID");
                Palete t = this.get(idt);
                res.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }


    @Override
    public Set<Entry<Integer, Palete>> entrySet(){
        Set<Integer> keys = new HashSet<>(this.keySet());
        HashMap<Integer, Palete> map = new HashMap<>();
        for(Integer key: keys){
            map.put(key, this.get(key));
        }
        return map.entrySet();
    }
}
