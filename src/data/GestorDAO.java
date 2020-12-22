package data;

import business.Gestor;
import business.Robot;

import java.sql.*;
import java.util.*;

public class GestorDAO implements Map<String, Gestor> {
    private static GestorDAO singleton = null;

    private GestorDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Gestores (" +
                    "Password varchar(30) NOT NULL PRIMARY KEY," +
                    "Nome varchar(45) DEFAULT NULL)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public static GestorDAO getInstance() {
        if (GestorDAO.singleton == null) {
            GestorDAO.singleton = new GestorDAO();
        }
        return GestorDAO.singleton;
    }

    //Método que devolve a quantidade de Gestores registados na base de dados
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Gestores")) {
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

    //Método que indica se um dada chave existe no map
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT Password FROM Gestores WHERE Password='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    //Método que indica se um dado Gestor existe na base de dados
    @Override
    public boolean containsValue(Object value) {
        Gestor a = (Gestor) value;
        return this.containsKey(a.getPassword());
    }

    //Método que devolve um dado Gestor, dada a sua password
    @Override
    public Gestor get(Object key) {
        Gestor a = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Gestores WHERE Password ='"+key+"'")) {
            if (rs.next()) {  // A chave existe na tabela
                // Reconstruir o aluno com os dados obtidos da BD - a chave estranjeira da turma, não é utilizada aqui.
                a = new Gestor(rs.getString("Password"), rs.getString("Nome"));
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    //Método que adiciona um novo Gestor à base de dados
    @Override
    public Gestor put(String key, Gestor a) {
        Gestor res = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            stm.executeUpdate(
                    "INSERT INTO Gestores VALUES ('"+a.getPassword()+"', '"+a.getNome()+"') " +
                            "ON DUPLICATE KEY UPDATE Nome=VALUES(Nome)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    //Método que remove um Gestor da base de dados, dada a sua password
    @Override
    public Gestor remove(Object key) {
        Gestor t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Gestores WHERE Password='"+key+"'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }


    @Override
    public void putAll(Map<? extends String, ? extends Gestor> gestores) {
        for(Gestor a : gestores.values()) {
            this.put(a.getPassword(), a);
        }
    }


    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE Gestores");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    //Método que devolve um Set com todas as key registadas
    @Override
    public Set<String> keySet() {
        Set<String> set = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Gestores WHERE Password = ?")){
            set = new HashSet<>();
            ps.setString(1, "Password");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                set.add(rs.getString("Password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return set;
    }


    //Método que devolve uma Collection com todos os Gestores registados
    @Override
    public Collection<Gestor> values() {
        Collection<Gestor> col = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT Password FROM Gestores")) {
            while (rs.next()) {
                col.add(this.get(rs.getString("Password")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }



    @Override
    public Set<Entry<String, Gestor>> entrySet() {
        Set<String> keys = new HashSet<>(this.keySet());
        HashMap<String, Gestor> map = new HashMap<>();
        for(String key: keys){
            map.put(key, this.get(key));
        }
        return map.entrySet();
    }
}

