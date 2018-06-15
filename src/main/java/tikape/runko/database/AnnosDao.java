package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Annos;
import tikape.runko.domain.RaakaAine;

public class AnnosDao implements Dao<Annos, Integer> {

    private Database database;

    public AnnosDao(Database database) {
        this.database = database;
    }

    @Override
    public Annos findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet resultSet = stmt.executeQuery();
        boolean hasOne = resultSet.next();
        if (!hasOne) {
            return null;
        }

        Integer id = resultSet.getInt("id");
        String nimi = resultSet.getString("nimi");

        Annos annos = new Annos(id, nimi);

        resultSet.close();
        stmt.close();
        connection.close();

        return annos;
    }

    @Override
    public List<Annos> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Annos");

        ResultSet resultSet = stmt.executeQuery();
        List<Annos> annokset = new ArrayList<>();
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String nimi = resultSet.getString("nimi");

            annokset.add(new Annos(id, nimi));
        }

        resultSet.close();
        stmt.close();
        connection.close();

        return annokset;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        
        PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM AnnosRaakaAine WHERE annos_id = ?");
        stmt2.setInt(1, key);

        PreparedStatement stmt = connection.prepareStatement("DELETE FROM Annos WHERE id = ?");
        stmt.setInt(1, key);

        stmt.executeUpdate();

        stmt.close();
        connection.close();

    }
    public void save(Annos annos) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Annos WHERE nimi = ?");
        statement.setString(1, annos.getNimi());
        
        ResultSet resultSet = statement.executeQuery();
        
        if (!resultSet.next() && !annos.getNimi().equals("")) {
            statement = connection.prepareStatement("INSERT INTO Annos (nimi) VALUES (?)");
            statement.setString(1, annos.getNimi());
            statement.executeUpdate();
        } 
        statement.close();        
        resultSet.close();
        connection.close();
    }
    

}
