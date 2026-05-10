package com.project.artconnect;

import com.project.artconnect.dao.DisciplineDAO;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.persistence.JdbcArtistDao;
import com.project.artconnect.persistence.JdbcDisciplineDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/MainView.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        stage.setTitle("ArtConnect Pro - Local Art Community Platform");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


//public class MainApp{
//    public static void main(String[] args) {
//        JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();
////
////        Artist artist = new Artist(11, "aaaa", "aaa", 18, "@email.com", "010101010", "cit", "websit", "social medi", false);
////        jdbcArtistDao.update(artist);
////        Artist banksi = jdbcArtistDao.findByName("Banksy");
//        Discipline discipline = new Discipline("beautiful art");
////        banksi.addDiscipline(discipline);
////        jdbcArtistDao.update(banksi);
////
//        DisciplineDAO disciplineDAO = new JdbcDisciplineDAO();
//        disciplineDAO.save(discipline);
//
//        List<Artist> artists = jdbcArtistDao.findAll();
//
//        artists.forEach(prelo -> System.out.println(prelo));
//
//    }
//}
