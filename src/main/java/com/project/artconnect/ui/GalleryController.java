package com.project.artconnect.ui;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;

public class GalleryController {

    // gallery composants
    @FXML
    private TableView<Gallery> galleryTable;
    @FXML
    private TableColumn<Gallery, String> nameColumn;
    @FXML
    private TableColumn<Gallery, String> ownerColumn;
    @FXML
    private TableColumn<Gallery, String> addressColumn;
    @FXML
    private TableColumn<Gallery, Double> ratingColumn;
    @FXML
    private TableColumn<Gallery, String> openingHoursColumn;
    @FXML
    private TableColumn<Gallery, String> phoneColumn;
    @FXML
    private TableColumn<Gallery, String> websiteColumn;

    // exhibition composants
    @FXML
    private TableView<Exhibition> exhibitionTable;
    @FXML
    private TableColumn<Exhibition, String> exhibitionTitleColumn;
    @FXML
    private TableColumn<Exhibition, String> exhibitionThemeColumn;
    @FXML
    private TableColumn<Exhibition, LocalDate> exhibitionStartDateColumn;
    @FXML
    private TableColumn<Exhibition, LocalDate> exhibitionEndDateColumn;
    @FXML
    private TableColumn<Exhibition, String> exhibitionCuratorColumn;
    @FXML
    private TableColumn<Exhibition, String> exhibitionDescriptionColumn;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        // gallery table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        openingHoursColumn.setCellValueFactory(new PropertyValueFactory<>("openingHours"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactPhone"));
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));

        addressColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAddress() != null) {
                return new SimpleStringProperty(cellData.getValue().getAddress().getCity_name());
            } else {
                return new SimpleStringProperty("Unknown");
            }
        });

        // exhibition table columns
        exhibitionTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        exhibitionThemeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        exhibitionStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        exhibitionEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        exhibitionCuratorColumn.setCellValueFactory(new PropertyValueFactory<>("curatorName"));
        exhibitionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // listener of user click's
        galleryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // if selected gallery -> update
                updateExhibitionTable(newValue);
            } else {
                // if no selected gallery -> clear
                exhibitionTable.getItems().clear();
            }
        });

        refreshData();
    }

    /**
    * retrieve info from db and update table
     */
    private void updateExhibitionTable(Gallery gallery) {
        List<Exhibition> exhibitions = galleryService.getExhibitionsByGallery(gallery);
        exhibitionTable.setItems(FXCollections.observableArrayList(exhibitions));
    }

    private void refreshData() {
        galleryTable.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }
}