package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

public class AnnosRaakaAineDao implements Dao<AnnosRaakaAine, Integer> {

    private Database database;
    private AnnosDao annosDao;
    private RaakaAineDao raakaAineDao;

    public AnnosRaakaAineDao(Database database, AnnosDao annosDao, RaakaAineDao raakaAineDao) {
        this.database = database;
        this.annosDao = annosDao;
        this.raakaAineDao = raakaAineDao;
    }

    @Override
    public AnnosRaakaAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<AnnosRaakaAine> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM AnnosRaakaAine");

        ResultSet resultSet = stmt.executeQuery();
        List<AnnosRaakaAine> annosRaakaAineet = new ArrayList<>();
        
        while (resultSet.next()) {            
            Integer raakAineId = resultSet.getInt("raakaaineId");
            RaakaAine raakaAine = raakaAineDao.findOne(raakAineId);
            Integer annosId = resultSet.getInt("smoothieId");
            Annos annos = annosDao.findOne(annosId);
            Integer jarjestys = resultSet.getInt("jarjestys");
            String maara = resultSet.getString("maara");
            String ohje = resultSet.getString("ohje");

            annosRaakaAineet.add(new AnnosRaakaAine(raakaAine, annos, jarjestys, maara, ohje));
        }

        resultSet.close();
        stmt.close();
        connection.close();

        return annosRaakaAineet;
    }

    public void save(AnnosRaakaAine annosRaakaAine) throws SQLException {

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO AnnosRaakaAine (raaka_aine_id, annos_id, jarjestys, maara, ohje) VALUES (?,?,?,?,?)");
            stmt.setInt(1, annosRaakaAine.getRaakaAine().getId());
            stmt.setInt(2, annosRaakaAine.getAnnos().getId());
            stmt.setInt(3, annosRaakaAine.getJarjestys());
            stmt.setString(4, annosRaakaAine.getMaara());
            stmt.setString(5, annosRaakaAine.getOhje());

            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
    }

    public List<String> findAnnoksenRaakaAineet(Annos annos) throws SQLException {
        
        List<String> raakaAineet = new ArrayList<>();
        Integer annoksenId = annos.getId();
        
        Connection connection = database.getConnection();
        PreparedStatement statement1 = connection.prepareStatement("SELECT Annos.nimi AS annos, RaakaAine.nimi AS raakaaine, AnnosRaakaAine.maara AS maara, AnnosRaakaAine.jarjestys AS jarjestys \n" +
"	FROM Annos\n" +
"	LEFT JOIN AnnosRaakaAine ON Annos.id = AnnosRaakaAine.annos_id\n" +
"	LEFT JOIN RaakaAine ON AnnosRaakaAine.raaka_aine_id = RaakaAine.id\n" +
"	WHERE Annos.id = ?\n" +
"	ORDER BY jarjestys;");
        statement1.setInt(1, annoksenId);

        ResultSet resultSet1 = statement1.executeQuery();
        
        while(resultSet1.next()){
            String raakaAine = resultSet1.getString("raakaaine");
            String maara = resultSet1.getString("maara");
            
            String kuvaus = raakaAine + ", " + maara;            
            raakaAineet.add(kuvaus);            
        }
        statement1.close();
        resultSet1.close();
        connection.close();
        
        return raakaAineet;
    }

//    public AnnosRaakaAine saveOrUpdate(AnnosRaakaAine annosRaakaAine) throws SQLException {
//
//        
//        AnnosRaakaAine byName = findByName(annosRaakaAine.getAnnos().getNimi());
//
//        if (byName != null) {
//            return byName;
//        }
//
//        try (Connection conn = database.getConnection()) {
//            PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + tableName + " (name) VALUES (?)");
//            stmt.setString(1, annosRaakaAine.getName());
//            stmt.executeUpdate();
//        }
//
//        return findByName(annosRaakaAine.getAnnos());
//    }
//    
//    private AnnosRaakaAine findByName(String name) throws SQLException {
//        try (Connection conn = database.getConnection()) {
//            PreparedStatement stmt = conn.prepareStatement("SELECT id, name FROM " + tableName + " WHERE name = ?");
//            stmt.setString(1, name);
//
//            try (ResultSet result = stmt.executeQuery()) {
//                if (!result.next()) {
//                    return null;
//                }
//
//                return createFromRow(result);
//            }
//        }
//    }
    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
