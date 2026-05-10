package com.project.artconnect.ui;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GalleryController {

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

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        // Basic properties
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Complex property: Extracting city/street from the Address object
        addressColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAddress() != null) {
                return new SimpleStringProperty(cellData.getValue().getAddress().getCity_name());
            } else {
                return new SimpleStringProperty("Unknown");
            }
        });

        refreshData();
    }

    private void refreshData() {
        galleryTable.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }
}