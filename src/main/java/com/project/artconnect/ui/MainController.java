package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.application.Platform;

import javax.xml.transform.Source;
import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    public void initialize() {
        if(Objects.equals(DatabaseConfig.getUSER(), "admin")){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CommunityTab.fxml"));
                Tab Community = new Tab("Community");
                Community.setContent(loader.load());

                mainTabPane.getTabs().add(Community);
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }


        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
