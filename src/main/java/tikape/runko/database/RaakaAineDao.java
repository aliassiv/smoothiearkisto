package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.RaakaAine;

public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet resultSet = stmt.executeQuery();
        boolean hasOne = resultSet.next();
        if (!hasOne) {
            return null;
        }

        Integer id = resultSet.getInt("id");
        String nimi = resultSet.getString("nimi");

        RaakaAine raakaAine = new RaakaAine(id, nimi);

        resultSet.close();
        stmt.close();
        connection.close();

        return raakaAine;
    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM RaakaAine");

        ResultSet resultSet = stmt.executeQuery();
        List<RaakaAine> raakaAineet = new ArrayList<>();
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String nimi = resultSet.getString("nimi");

            raakaAineet.add(new RaakaAine(id, nimi));
        }

        resultSet.close();
        stmt.close();
        connection.close();

        return raakaAineet;
    }

    
    public boolean deleteRaakaAine(Integer key) throws SQLException {
        
        Connection connection = database.getConnection();
        
        PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM AnnosRaakaAine WHERE raaka_aine_id = ?");
        stmt1.setInt(1, key);
        
        ResultSet resultSet = stmt1.executeQuery();
    
        if (!resultSet.next()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM RaakaAine WHERE id = ?");
            stmt.setInt(1, key);

            stmt.executeUpdate();
            stmt.close();
            stmt1.close();
            connection.close();
            return true;
        }
        resultSet.close();
        stmt1.close();
        connection.close();
        return false;
    }

    public void save(RaakaAine raakaAine) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM RaakaAine WHERE nimi = ?");
        statement.setString(1, raakaAine.getNimi());
        
        ResultSet resultSet = statement.executeQuery();
        
        if (!resultSet.next() && !raakaAine.getNimi().equals("")) {
            statement = connection.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
            statement.setString(1, raakaAine.getNimi());
            statement.executeUpdate();
        } 
        statement.close();        
        resultSet.close();
        connection.close();
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
