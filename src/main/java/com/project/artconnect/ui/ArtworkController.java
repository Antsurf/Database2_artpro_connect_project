package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.ArtworkTag;
import com.project.artconnect.model.Review;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.service.ReviewService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.time.LocalDate;

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
    @FXML
    private TableColumn<Artwork, String> yearColumn;
    @FXML
    private TableColumn<Artwork, String> mediumComlumn;
    @FXML
    private TableColumn<Artwork, String> dimensionColumn;
    @FXML
    private TableColumn<Artwork, String> descriptionColumn;

    // Review Field
    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private TableColumn<Review, Double> ReviewRatingColumn;
    @FXML
    private TableColumn<Review, String> ReviewCommentColumn;
    @FXML
    private TableColumn<Review, LocalDate> ReviewDateColumn;
    @FXML
    private TableColumn<Review, String> ReviewTypeColumn;

    @FXML
    private HBox buttonField;


    private final ArtworkService artworkService = ServiceProvider.getArtworkService();
    private final ReviewService reviewService= ServiceProvider.getReviewService();
    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        avgRatingColumn.setCellValueFactory(new PropertyValueFactory<>("avgRatingString"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("creationYear"));
        mediumComlumn.setCellValueFactory(new PropertyValueFactory<>("medium"));
        dimensionColumn.setCellValueFactory(new PropertyValueFactory<>("dimensions"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getArtist() != null ? cellData.getValue().getArtist().getName() : "Unknown"));

        ReviewRatingColumn.setCellValueFactory(new PropertyValueFactory<>("reviewRating"));
        ReviewCommentColumn.setCellValueFactory(new PropertyValueFactory<>("reviewComment"));
        ReviewDateColumn.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));
        ReviewTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reviewType"));

        // listen for the user click's
        artworkTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                // if selected artwork -> update
                updateReviewTable(newValue);
            } else {
                // if no selected gallery -> clear
                reviewTable.getItems().clear();
            }
        });


        if(Objects.equals(DatabaseConfig.getUSER(), "admin")){
            Button addButton = new Button("Add");
            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleAdd();
                }
            });

            Button updateButton = new Button("Update");
            updateButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleUpdate();
                }
            });

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    handleDelete();
                }
            });

            buttonField.getChildren().add(addButton);
            buttonField.getChildren().add(updateButton);
            buttonField.getChildren().add(deleteButton);
        }

        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
        refreshTable();
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

        ArtistService artistService = ServiceProvider.getArtistService();
        List<Artist> artistList = artistService.getAllArtists();
        ComboBox<String> artistComboBox = new ComboBox<>();
        for (Artist artist : artistList) {
            artistComboBox.getItems().add(artist.getName());
        }

        ComboBox<Artwork.Status> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Artwork.Status.values());
        statusComboBox.getSelectionModel().select(Artwork.Status.FOR_SALE);

        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeSave, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100));
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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeSave) {

                Artwork.Status selectedStatus = statusComboBox.getValue();
                String artistName = artistComboBox.getValue();

                Artist finalArtist = null;
                if (artistName != null) {
                    Optional<Artist> selectedArtist = artistService.getArtistByName(artistName);
                    finalArtist = selectedArtist.orElse(null);
                }
                ArtworkTag artag = new ArtworkTag(tagField.getText());
                List<ArtworkTag> tags = new ArrayList<>();
                tags.add(artag);

                return new Artwork(
                        selectedStatus,
                        finalArtist,
                        titleField.getText(),
                        Integer.parseInt(creationYearField.getText().trim()),
                        mediumField.getText(),
                        typeField.getText(),
                        dimensionsField.getText(),
                        descriptionField.getText(),
                        Double.parseDouble(priceField.getText().trim()),
                        tags
                );
            }
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

        TextField tagField = new TextField();
        if (oldArtwork.getTags() != null && !oldArtwork.getTags().isEmpty()) {
            tagField.setText(oldArtwork.getTags().get(0).toString());
        } else {
            tagField.setPromptText("Artwork Tag");
        }

        ArtistService artistService = ServiceProvider.getArtistService();
        List<Artist> artistList = artistService.getAllArtists();
        ComboBox<String> artistComboBox = new ComboBox<>();
        for (Artist artist : artistList) {
            artistComboBox.getItems().add(artist.getName());
        }

        if (oldArtwork.getArtist() != null) {
            artistComboBox.getSelectionModel().select(oldArtwork.getArtist().getName());
        }

        ComboBox<Artwork.Status> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll(Artwork.Status.values());
        if (oldArtwork.getStatus() != null) {
            statusComboBox.getSelectionModel().select(oldArtwork.getStatus());
        } else {
            statusComboBox.getSelectionModel().select(Artwork.Status.FOR_SALE);
        }

        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeSave, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100));

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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeSave) {

                Artwork.Status selectedStatus = statusComboBox.getValue();
                String artistName = artistComboBox.getValue();

                Artist finalArtist = null;
                if (artistName != null) {
                    Optional<Artist> selectedArtist = artistService.getArtistByName(artistName);
                    finalArtist = selectedArtist.orElse(null);
                }

                ArtworkTag artag = new ArtworkTag(tagField.getText());
                List<ArtworkTag> tags = new ArrayList<>();
                tags.add(artag);

                return new Artwork(
                        selectedStatus,
                        finalArtist,
                        titleField.getText(),
                        Integer.parseInt(creationYearField.getText().trim()),
                        typeField.getText(),
                        mediumField.getText(),
                        dimensionsField.getText(),
                        descriptionField.getText(),
                        Double.parseDouble(priceField.getText().trim()),
                        tags
                );
            }
            return null;
        });
        return dialog;
    }
    @FXML
    private void handleRating() {
        Artwork artwork = artworkTable.getSelectionModel().getSelectedItem();
        if (artwork != null) {
            Dialog<Review> dialog =  rateArtwork();
            Optional<Review> result = dialog.showAndWait();

            result.ifPresent( review -> {
                reviewService.createReview(review,artwork.getId(),communityService.getByEmail(DatabaseConfig.getUSER()).getId());
            });
        }
    }

    private Dialog<Review> rateArtwork() {

        Dialog<Review> dialog = new Dialog<>();

        String s = "Add a rating to the Artwork";
        dialog.setTitle(s);
        dialog.setHeaderText("Enter the information of the rating");

        TextField ratingField = new TextField();
        ratingField.setPromptText("5");

        TextField commentField = new TextField();
        commentField.setPromptText("Beautiful");

        TextField typeField = new TextField();
        typeField.setPromptText("Professional or Visitor");

        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeSave, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100));

        grid.add(new Label("Rating :"), 0, 0);
        grid.add(ratingField, 1, 0);
        grid.add(new Label("Comment :"), 0, 1);
        grid.add(commentField, 1, 1);
        grid.add(new Label("Tag :"), 0, 2);
        grid.add(typeField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeSave) {
                return new Review(Integer.parseInt(ratingField.getText().trim()), commentField.getText(), LocalDate.now(), typeField.getText());
            }
            return null;
        });
        return dialog;
    }

    private void updateReviewTable(Artwork artwork) {
        List<Review> reviews = communityService.getReviewsByMember(communityService.getByEmail(DatabaseConfig.getUSER()));

        System.out.println("Just before");
        // find the review associated to the artwork
        for (Review review : reviews) {
            if ( review.getArtwork().equals(artwork) ) {
                System.out.println(artwork);
                reviewTable.setItems(FXCollections.observableArrayList(review));
            }
        }
    }


}