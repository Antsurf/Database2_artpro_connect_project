package com.project.artconnect;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.model.Artist;
import com.project.artconnect.ui.popUpManager;
import com.project.artconnect.util.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javafx.scene.control.*;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        boolean connected = false;
        do {
            Dialog<String> dialog  = popUpManager.getConnectionPopUp();
            Optional<String> result = dialog.showAndWait();
            if(result.get().equals("stop")){
                return;
            }

            if(result.get().equals("connected")){
                try(Connection connection = ConnectionManager.getConnection()){
                    connected = true;
                    System.out.println("\nYou are connected, the page will be displayed shortly");
                }
                catch(SQLException e){
                    System.out.println("User or password wrong");
                }
            }

        }while(!connected);


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
