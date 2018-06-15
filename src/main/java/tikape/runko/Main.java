package tikape.runko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AnnosDao;
import tikape.runko.database.AnnosRaakaAineDao;
import tikape.runko.database.Database;
import tikape.runko.database.RaakaAineDao;
import tikape.runko.domain.Annos;
import tikape.runko.domain.AnnosRaakaAine;
import tikape.runko.domain.RaakaAine;

public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
//        Database database = new Database("jdbc:sqlite:raakaaineet.db");
//        database.init();


        Database database = new Database();

        RaakaAineDao raakaAineDao = new RaakaAineDao(database);
        AnnosDao annosDao = new AnnosDao(database);
        AnnosRaakaAineDao annosRaakaAineDao = new AnnosRaakaAineDao(database, annosDao, raakaAineDao);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/reseptit/raakaaineet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("raakaaineet", raakaAineDao.findAll());

            return new ModelAndView(map, "raakaaineet");
        }, new ThymeleafTemplateEngine());

        Spark.get("/reseptit/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", annosDao.findAll());
            map.put("raakaaineet", raakaAineDao.findAll());

            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());

        Spark.get("/reseptit/smoothiet", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", annosDao.findAll());
            map.put("raakaaineet", raakaAineDao.findAll());

            return new ModelAndView(map, "smoothiet");
        }, new ThymeleafTemplateEngine());
        
        Spark.get("/reseptit", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("smoothiet", annosDao.findAll());
            
            return new ModelAndView(map, "reseptit");
        }, new ThymeleafTemplateEngine());

        Spark.get("/reseptit/smoothiet/yksittainen/*", (req, res) -> {
            Integer annosId = Integer.parseInt(req.splat()[0]);
            Annos annos = annosDao.findOne(annosId);                   
            
            HashMap map = new HashMap<>();           
            map.put("smoothienimi", annos.getNimi());
            map.put("annoksenRaakaAineet", annosRaakaAineDao.findAnnoksenRaakaAineet(annos));

            return new ModelAndView(map, "smoothie");
        }, new ThymeleafTemplateEngine());

        Spark.post("/reseptit/smoothieainelisays", (req, res) -> {

            Integer raakaaineId = Integer.parseInt(req.queryParams("raakaaineId"));
            RaakaAine raakaAine = raakaAineDao.findOne(raakaaineId);
            Integer annosId = Integer.parseInt(req.queryParams("smoothieId"));
            Annos annos = annosDao.findOne(annosId);
            Integer jarjestys = Integer.parseInt(req.queryParams("jarjestys"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");

            AnnosRaakaAine annosRaakaAine = new AnnosRaakaAine(raakaAine, annos, jarjestys, maara, ohje);
            annosRaakaAineDao.save(annosRaakaAine);

            res.redirect("/reseptit/smoothiet");
            return "";
        });

        Spark.get("/reseptit/smoothiet/delete/*", (req, res) -> {
            Integer annosId = Integer.parseInt(req.splat()[0]);
            annosDao.delete(annosId);

            res.redirect("/reseptit/smoothiet");
            return "";
        });

        Spark.get("/reseptit/raakaaineet/delete/*", (req, res) -> {
            Integer raakaaineId = Integer.parseInt(req.splat()[0]);
            raakaAineDao.delete(raakaaineId);

            res.redirect("/reseptit/raakaaineet");
            return "";
        });
        
        
        Spark.post("/reseptit/raakaaine", (req, res) -> {
            RaakaAine raakaAine = new RaakaAine(-1, req.queryParams("nimi"));
            raakaAineDao.save(raakaAine);

            res.redirect("/reseptit/raakaaineet");
            return "";
        });

//        get("/opiskelijat/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "opiskelija");
//        }, new ThymeleafTemplateEngine());
    }
    
    
    public static Connection getConnection() throws Exception {
    String dbUrl = System.getenv("JDBC_DATABASE_URL");
    if (dbUrl != null && dbUrl.length() > 0) {
        return DriverManager.getConnection(dbUrl);
    }

    return DriverManager.getConnection("jdbc:sqlite:raakaaineet.db");

    }
}
