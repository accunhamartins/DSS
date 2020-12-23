package data;

import business.Robot;

import java.sql.*;
import java.util.*;

public class RobotDAO implements Map<Integer, Robot> {
    private static RobotDAO singleton = null;

    private RobotDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Robots (" +
                    "ID int NOT NULL PRIMARY KEY," +
                    "Armazem int NOT NULL," +
                    "Disponivel int DEFAULT 1, "+
                    "foreign key(Armazem) references Armazem(ID))";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }


    public static RobotDAO getInstance() {
        if (RobotDAO.singleton == null) {
            RobotDAO.singleton = new RobotDAO();
        }
        return RobotDAO.singleton;
    }

    //Método que devolve o número de robots registados na base de dados
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Robots")) {
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

    //Método que indica se o map está vazio
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    //Método que indica se um dado robot está registado na base de dados, dado o seu ID
    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT ID FROM Robots WHERE ID='"+key.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    //Método que indica se um dado robot está registado na base de dados
    @Override
    public boolean containsValue(Object value) {
        Robot a = (Robot) value;
        return this.containsKey(a.getId());
    }

    //Método que devolve um Robot, dado o seu ID
    @Override
    public Robot get(Object key) {
        Robot a = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Robots WHERE Id ='"+key+"'")) {
            if (rs.next()) {
                a = new Robot(rs.getInt("ID"), rs.getInt("Disponivel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
    }

    //Método que adiciona um novo robot à base de dados
    @Override
    public Robot put(Integer key, Robot a) {
        Robot res = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate(
                    "INSERT INTO Robots VALUES ('"+a.getId()+ "', '"+ 1 +"', '"+a.isDisponivel()+"') " +
                            "ON DUPLICATE KEY UPDATE Disponivel=VALUES(Disponivel)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    //Método que remove um robot da base de dados
    @Override
    public Robot remove(Object key) {
        Robot t = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Robots WHERE Password='"+key+"'");
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }


    @Override
    public void putAll(Map<? extends Integer, ? extends Robot> robots) {
        for(Robot a : robots.values()) {
            this.put(a.getId(), a);
        }
    }


    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE Robots");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    //Método que devolve um Set com todos os ID's dos robots registados
    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Robots WHERE ID = ?")){
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

    //Método que devolve uma Collection com todos os Robots registados
    @Override
    public Collection<Robot> values() {
        Collection<Robot> col = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT ID FROM Robots")) {
            while (rs.next()) {
                col.add(this.get(rs.getInt("ID")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }


    @Override
    public Set<Entry<Integer, Robot>> entrySet() {
        Set<Integer> keys = new HashSet<>(this.keySet());
        HashMap<Integer, Robot> map = new HashMap<>();
        for(Integer key: keys){
            map.put(key, this.get(key));
        }
        return map.entrySet();
    }
}
