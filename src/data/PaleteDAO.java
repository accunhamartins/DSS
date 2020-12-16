package data;

import business.Localizacao;
import business.Material;
import business.Palete;
import business.QRCode;

import java.sql.*;
import java.util.*;

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
            sql = "CREATE TABLE IF NOT EXISTS palete (" +
                    "ID int NOT NULL PRIMARY KEY," +
                    "Armazem int NOT NULL," +
                    "QRCode varchar(100) NOT NULL," +
                    "Peso DECIMAL(5,2)," +
                    "Corredor int NOT NULL, " +
                    "Prateleira int NOT NULL, " +
                    "Material varchar(100) NOT NULL," +
                    "Armazenada int default null, " +
                    "foreign key(QRCode) references QRCode(Codigo), " +
                    "foreign key(Armazem) references Armazem(ID), " +
                    "foreign key(Material) references Material(Designacao))" ;
            stm.executeUpdate(sql);

        } catch (SQLException e) {
            // Erro a criar tabela...
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


    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM palete")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }


    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }


    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT ID FROM palete WHERE ID='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Palete p = (Palete) value;
        return this.containsKey(p.getID());
    }



    public Palete get(Object key) {
        Palete p = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM palete WHERE Id='"+key+"'")) {
            if (rs.next()) {
                // Reconstruir a Sala
                QRCode code = null;
                Localizacao local = null;
                Material material = null;
                double peso = 0;
                int armazenada = 0;
                String sql = "SELECT * FROM palete WHERE id='"+rs.getString("ID")+"'";
                try (ResultSet rsa = stm.executeQuery(sql)) {
                    if (rsa.next()) {
                        code = new QRCode(rs.getString("QRCode"));
                        local = new Localizacao(rs.getInt("Corredor"), rs.getInt("Prateleira"));
                        material = new Material(rs.getString("Material"), rs.getDouble("Preco"));
                        peso = rs.getDouble("Peso");
                        armazenada = rs.getInt("Armazenada");
                        p = new Palete(rs.getInt("ID"),code, local, material, peso, armazenada);
                    }
                }
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return p;
    }

    public Palete put(Integer key, Palete t) {
        Palete res = null;
        Localizacao l = t.getLocalizacao();
        Material m = t.getMaterial();
        QRCode qr = t.getCode();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            stm.executeUpdate(
                    "INSERT INTO QRCode" +
                            "VALUES ('"+ qr.getCodigo()+ "'" +") " +
                            "ON DUPLICATE KEY UPDATE Codigo =Values(Codigo)");


            stm.executeUpdate(
                    "INSERT INTO Material VALUES ('"+m.getDesignacao()+"', '"+m.getPrecoUnitario()+"') " +
                            "ON DUPLICATE KEY UPDATE Designacao=VALUES(Designacao)");

            stm.executeUpdate(
                    "INSERT INTO Palete VALUES ('"+t.getID()+"', '"+qr.getCodigo()+"', '"+l.getZona()+"', '"
                            +l.getPrateleira()+ "', '" + m.getDesignacao() + "', '"+ t.getPeso()+
                    "', '" + t.getArmazenada()+ "'" +") " +
                            "ON DUPLICATE KEY UPDATE ID=VALUES(ID)");

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Palete remove(Object key) {
        Palete p = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement())
             {
            stm.executeUpdate("DELETE FROM palete WHERE Id='"+key+"'");
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
            stm.executeUpdate("TRUNCATE palete");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    @Override
    public Set<Integer> keySet() {
        throw new NullPointerException("Not implemented!");
    }

    /**
     * @return Todos as turmas da base de dados
     */
    @Override
    public Collection<Palete> values() {
        Collection<Palete> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Id FROM paletes")) {
            while (rs.next()) {
                String idt = rs.getString("Id");
                Palete t = this.get(idt);
                res.add(t);
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    /**
     * NÃO IMPLEMENTADO!
     * @return ainda nada!
     */
    @Override
    public Set<Entry<Integer, Palete>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Aluno>> entrySet() not implemented!");
    }
}
