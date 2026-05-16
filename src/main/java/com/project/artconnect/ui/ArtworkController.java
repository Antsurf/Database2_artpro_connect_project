package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.ArtworkTag;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtworkController {
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, String> statusColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;
    @FXML
    private TableColumn<Artwork, String> avgRatingColumn;


    private final ArtworkService artworkService = ServiceProvider.getArtworkService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        avgRatingColumn.setCellValueFactory(new PropertyValueFactory<>("avgRatingString"));

        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getArtist() != null ? cellData.getValue().getArtist().getName() : "Unknown"));

        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }

    private void refreshTable(){
        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }

    @FXML
    private void handleDelete() {

        Artwork selectedArtwork = artworkTable.getSelectionModel().getSelectedItem();
        if (selectedArtwork != null) {
            artworkService.deleteArtwork(selectedArtwork.getTitle());
        }
        refreshTable();
    }

    @FXML
    private void handleAdd(){
        Dialog<Artwork> dialog = createArtworkDialog();
        Optional<Artwork> result = dialog.showAndWait();

        result.ifPresent(artist -> {
            System.out.println("out");
            artworkService.createArtwork(artist);
        });
        refreshTable();
    }

    private Dialog<Artwork> createArtworkDialog() {

        Dialog<Artwork> dialog = new Dialog<>();

        String s = "Add New Artwork";
        dialog.setTitle(s);
        dialog.setHeaderText("Enter the information of the Artwork");

        // Initialize all input fields
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField creationYearField = new TextField();
        creationYearField.setPromptText("Creation Year");

        TextField typeField = new TextField();
        typeField.setPromptText("Type (e.g., painting, sculpture)");

        TextField mediumField = new TextField();
        mediumField.setPromptText("Medium (e.g., oil, watercolor)");

        TextField dimensionsField = new TextField();
        dimensionsField.setPromptText("Dimensions");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField tagField = new TextField();
        tagField.setPromptText("Artwork Tag");

        // Setup Artist ComboBox
        ArtistService artistService = ServiceProvider.getArtistService();
        List<Artist> artistList = artistService.getAllArtists();
        ComboBox<String> artistComboBox = new ComboBox<>();
        for (Artist artist : artistList) {
            artistComboBox.getItems().add(artist.getName());
        }

        // Setup Status ComboBox
        ComboBox<Artwork.Status> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Artwork.Status.values());
        statusComboBox.getSelectionModel().select(Artwork.Status.FOR_SALE);

        // Setup Dialog Buttons
        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeSave, ButtonType.CANCEL);

        // Setup Layout Grid
        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide
        grid.setHgap(10);
        grid.setVgap(10); // Adds a little spacing between rows

        // Add labels to column 0, and fields to column 1
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);

        grid.add(new Label("Year:"), 0, 2);
        grid.add(creationYearField, 1, 2);

        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeField, 1, 3);

        grid.add(new Label("Medium:"), 0, 4);
        grid.add(mediumField, 1, 4);

        grid.add(new Label("Dimensions:"), 0, 5);
        grid.add(dimensionsField, 1, 5);

        grid.add(new Label("Description:"), 0, 6);
        grid.add(descriptionField, 1, 6);

        grid.add(new Label("Price:"), 0, 7);
        grid.add(priceField, 1, 7);

        grid.add(new Label("Tag:"), 0, 8);
        grid.add(tagField, 1, 8);

        grid.add(new Label("Artist:"), 0, 9);
        grid.add(artistComboBox, 1, 9);

        grid.add(new Label("Status:"), 0, 10);
        grid.add(statusComboBox, 1, 10);

        dialog.getDialogPane().setContent(grid);

        // Process the result ONLY when a button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeSave) {

                // 1. Read values from the ComboBoxes
                Artwork.Status selectedStatus = statusComboBox.getValue();
                String artistName = artistComboBox.getValue();

                // 2. Fetch the artist from the database using the name
                Artist finalArtist = null;
                if (artistName != null) {
                    Optional<Artist> selectedArtist = artistService.getArtistByName(artistName);
                    finalArtist = selectedArtist.orElse(null);
                }

                // 3. Create the tag list from the TextField
                ArtworkTag artag = new ArtworkTag(tagField.getText());
                List<ArtworkTag> tags = new ArrayList<>();
                tags.add(artag);

                int year = Integer.parseInt(creationYearField.getText().trim());
                int price = Integer.parseInt(priceField.getText().trim());


                // 5. Construct and return the Artwork
                return new Artwork(
                        selectedStatus,
                        finalArtist,
                        titleField.getText(),
                        year,
                        mediumField.getText(),
                        typeField.getText(),
                        dimensionsField.getText(),
                        descriptionField.getText(),
                        price,
                        tags
                );
            }

            // Return null if they clicked Cancel or closed the window
            return null;
        });

        return dialog;
    }

    @FXML
    private void handleUpdate(){
        Artwork oldArtwork = artworkTable.getSelectionModel().getSelectedItem();
        if (oldArtwork != null) {
            Dialog<Artwork> dialog = updateArtwork(oldArtwork);
            Optional<Artwork> result = dialog.showAndWait();

            result.ifPresent(artwork -> {
                oldArtwork.setArtist(artwork.getArtist());
                oldArtwork.setTitle(artwork.getTitle());
                oldArtwork.setType(artwork.getType());
                oldArtwork.setMedium(artwork.getMedium());
                oldArtwork.setDimensions(artwork.getDimensions());
                oldArtwork.setDescription(artwork.getDescription());
                oldArtwork.setPrice(artwork.getPrice());
                oldArtwork.setTags(artwork.getTags());
                oldArtwork.setCreationYear(artwork.getCreationYear());
                oldArtwork.setStatus(artwork.getStatus());

                artworkService.updateArtwork(oldArtwork);
            });
        }
        refreshTable();
    }

    private Dialog<Artwork> updateArtwork(Artwork oldArtwork){
        Dialog<Artwork> dialog = new Dialog<>();

        String s = "Update Artwork";
        dialog.setTitle(s);
        dialog.setHeaderText("Enter the new information of the Artwork");

        // Initialize all input fields with existing data
        TextField titleField = new TextField();
        titleField.setText(oldArtwork.getTitle());

        TextField creationYearField = new TextField();
        creationYearField.setText(oldArtwork.getCreationYear() != null ? oldArtwork.getCreationYear().toString() : "");

        TextField typeField = new TextField();
        typeField.setText(oldArtwork.getType() != null ? oldArtwork.getType().toString() : "");

        TextField mediumField = new TextField();
        mediumField.setText(oldArtwork.getMedium() != null ? oldArtwork.getMedium().toString() : "");

        TextField dimensionsField = new TextField();
        dimensionsField.setText(oldArtwork.getDimensions() != null ? oldArtwork.getDimensions().toString() : "");

        TextField descriptionField = new TextField();
        descriptionField.setText(oldArtwork.getDescription());

        TextField priceField = new TextField();
        priceField.setText(oldArtwork.getPrice()+ "");

        // Pre-fill the tag field if the artwork already has tags
        TextField tagField = new TextField();
        if (oldArtwork.getTags() != null && !oldArtwork.getTags().isEmpty()) {
            // Assuming your ArtworkTag class has a method to get the string, like getName() or toString()
            // Change `.toString()` to `.getName()` or whatever your getter is called if needed!
            tagField.setText(oldArtwork.getTags().get(0).toString());
        } else {
            tagField.setPromptText("Artwork Tag");
        }

        // Setup Artist ComboBox
        ArtistService artistService = ServiceProvider.getArtistService();
        List<Artist> artistList = artistService.getAllArtists();
        ComboBox<String> artistComboBox = new ComboBox<>();
        for (Artist artist : artistList) {
            artistComboBox.getItems().add(artist.getName());
        }
        // IMPORTANT: Pre-select the artist that is already attached to this artwork
        if (oldArtwork.getArtist() != null) {
            artistComboBox.getSelectionModel().select(oldArtwork.getArtist().getName());
        }

        // Setup Status ComboBox
        ComboBox<Artwork.Status> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Artwork.Status.values());
        // IMPORTANT: Pre-select the current status instead of defaulting to FOR_SALE
        if (oldArtwork.getStatus() != null) {
            statusComboBox.getSelectionModel().select(oldArtwork.getStatus());
        } else {
            statusComboBox.getSelectionModel().select(Artwork.Status.FOR_SALE);
        }

        // Setup Dialog Buttons
        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeSave, ButtonType.CANCEL);

        // Setup Layout Grid
        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide
        grid.setHgap(10);
        grid.setVgap(10);

        // Add labels to column 0, and fields to column 1
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);

        grid.add(new Label("Year:"), 0, 2);
        grid.add(creationYearField, 1, 2);

        grid.add(new Label("Type:"), 0, 3);
        grid.add(typeField, 1, 3);

        grid.add(new Label("Medium:"), 0, 4);
        grid.add(mediumField, 1, 4);

        grid.add(new Label("Dimensions:"), 0, 5);
        grid.add(dimensionsField, 1, 5);

        grid.add(new Label("Description:"), 0, 6);
        grid.add(descriptionField, 1, 6);

        grid.add(new Label("Price:"), 0, 7);
        grid.add(priceField, 1, 7);

        grid.add(new Label("Tag:"), 0, 8);
        grid.add(tagField, 1, 8);

        grid.add(new Label("Artist:"), 0, 9);
        grid.add(artistComboBox, 1, 9);

        grid.add(new Label("Status:"), 0, 10);
        grid.add(statusComboBox, 1, 10);

        dialog.getDialogPane().setContent(grid);

        // Process the result ONLY when a button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeSave) {

                // 1. Read values from the ComboBoxes
                Artwork.Status selectedStatus = statusComboBox.getValue();
                String artistName = artistComboBox.getValue();

                // 2. Fetch the artist from the database using the name
                Artist finalArtist = null;
                if (artistName != null) {
                    Optional<Artist> selectedArtist = artistService.getArtistByName(artistName);
                    finalArtist = selectedArtist.orElse(null);
                }

                // 3. Create the tag list from the TextField
                ArtworkTag artag = new ArtworkTag(tagField.getText());
                List<ArtworkTag> tags = new ArrayList<>();
                tags.add(artag);

                // 5. Construct and return the UPDATED Artwork
                // Note: If your Artwork class has a method to retain the ID, you might need to set it here!
                return new Artwork(
                        selectedStatus,
                        finalArtist,
                        titleField.getText(),
                        Integer.parseInt(creationYearField.getText().trim()),
                        mediumField.getText(),
                        typeField.getText(),
                        dimensionsField.getText(),
                        descriptionField.getText(),
                        Integer.parseInt(priceField.getText().trim()),
                        tags
                );
            }

            return null;
        });

        return dialog;
    }
}