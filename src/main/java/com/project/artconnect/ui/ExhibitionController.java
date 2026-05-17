package com.project.artconnect.ui;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.ExhibitionService;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExhibitionController {
    @FXML
    private TableView<Exhibition> exhibitionTable;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filter;
    @FXML
    private TableColumn<Exhibition, String> titleColumn;
    @FXML
    private TableColumn<Exhibition, LocalDate> dateColumn;
    @FXML
    private TableColumn<Exhibition, String> themeColumn;
    @FXML
    private TableColumn<Exhibition, String> galleryColumn;
    @FXML
    private TableColumn<Exhibition, String> artworksColumn;
    @FXML
    private TableColumn<Exhibition, String> endDateColumn;
    @FXML
    private TableColumn<Exhibition, String> CuratorColumn;
    @FXML
    private TableColumn<Exhibition, String> descriptionColumn;

    private final ExhibitionService exhibitionService = ServiceProvider.getExhibitionService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        CuratorColumn.setCellValueFactory(new PropertyValueFactory<>("curatorName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        galleryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getGallery() != null ? cellData.getValue().getGallery().getName() : "Unknown"));

        artworksColumn.setCellValueFactory(cellData -> {
            List<Artwork> artworks = cellData.getValue().getArtworks();
            if (artworks == null || artworks.isEmpty()) {
                return new SimpleStringProperty("No artworks");
            }
            // Join all artwork titles with a comma
            String titles = artworks.stream()
                    .map(Artwork::getTitle)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            return new SimpleStringProperty(titles);
        });
        filter.setItems(FXCollections.observableArrayList(null, "Title","Theme", "City"));
        refreshData();
    }
    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        String filterColumn = filter.getValue().toLowerCase();
        exhibitionTable.setItems(FXCollections.observableArrayList(exhibitionService.filterExhibition(query, filterColumn)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        filter.setValue(null);
        refreshTable();
    }

    private void refreshTable() {
        exhibitionTable.setItems(FXCollections.observableArrayList(exhibitionService.findAll()));
    }

    private void refreshData() {
        List<Exhibition> all = exhibitionService.findAll();
        exhibitionTable.setItems(FXCollections.observableArrayList(all));

    }
}
