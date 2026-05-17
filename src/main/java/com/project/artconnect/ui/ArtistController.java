package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Objects;
import java.util.Optional;

public class ArtistController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Discipline> disciplineFilter;
    @FXML
    private TableView<Artist> artistTable;
    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;
    @FXML
    private TableColumn<Artist, Integer> bioColumn;
    @FXML
    private TableColumn<Artist, Integer> socialColumn;
    @FXML
    private TableColumn<Artist, Integer> webColumn;
    @FXML
    private HBox buttonField;


    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        bioColumn.setCellValueFactory(new PropertyValueFactory<>("bio"));
        socialColumn.setCellValueFactory(new PropertyValueFactory<>("socialMedia"));
        webColumn.setCellValueFactory(new PropertyValueFactory<>("website"));

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


//        Add a column phone if admin

        if (Objects.equals(DatabaseConfig.getUSER(), "admin")){
            TableColumn<Artist, String> phoneColumn = new TableColumn<>("Phone");
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

            artistTable.getColumns().add(phoneColumn);
        }


        disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline d = disciplineFilter.getValue();
        String dName = (d != null) ? d.getName() : null;
        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    private void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }

    @FXML
    private void handleDelete() {

        Artist selectedPerson = artistTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            artistService.deleteArtistById(selectedPerson);
        }
        refreshTable();
    }

    @FXML
    private void handleAdd(){
        Dialog<Artist> dialog = createArtistDialog();
        Optional<Artist> result = dialog.showAndWait();

        result.ifPresent(artist -> {artistService.addArtist(artist);});
        refreshTable();
    }

    @FXML
    private void handleUpdate() {
        Artist oldArtist = artistTable.getSelectionModel().getSelectedItem();
        if (oldArtist != null) {
            Dialog<Artist> dialog = updateArtistDialog(oldArtist);
            Optional<Artist> result = dialog.showAndWait();

            result.ifPresent(newArtist -> {
                oldArtist.setName(newArtist.getName());
                oldArtist.setCity(newArtist.getCity());
                oldArtist.setContactEmail(newArtist.getContactEmail());
                oldArtist.setBirthYear(newArtist.getBirthYear());
                oldArtist.setPhone(newArtist.getPhone());
                oldArtist.setWebsite(newArtist.getWebsite());
                oldArtist.setBio(newArtist.getBio());

                artistService.updateArtist(oldArtist);
            });
        }

        refreshTable();
    }

    private Dialog<Artist> createArtistDialog() {

        Dialog<Artist> dialog = new Dialog<>();

        String s = "Add New Artist";
        dialog.setTitle(s);

        dialog.setHeaderText(" Enter the information of Artist ");
        TextField nameField = new TextField();
        nameField.setPromptText("Eg : John Cena");
        Label name = new Label("Name: ");

        TextField emailField = new TextField();
        emailField.setPromptText("Email@gmail.com");
        Label email = new Label("Email: ");

        TextField cityField = new TextField();
        cityField.setPromptText("Eg : Paris");
        Label city = new Label("City: ");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Eg : 911");
        Label phone = new Label("Phone: ");

        TextField birthYearField = new TextField();
        birthYearField.setPromptText("Eg : 1999");
        Label birthYear = new Label("Birth Year: ");

        TextField websiteField = new TextField();
        websiteField.setPromptText("Eg : website.com");
        Label website = new Label("Website: ");

        TextField bioField = new TextField();
        bioField.setPromptText("Bio");
        Label bio = new Label("Bio: ");

        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeSave);

        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide

        grid.add(nameField, 1, 1);
        grid.add(name, 0, 1);
        grid.add(emailField, 1, 2);
        grid.add(email, 0, 2);
        grid.add(cityField, 1, 3);
        grid.add(city, 0, 3);
        grid.add(phoneField, 1, 4);
        grid.add(phone, 0, 4);
        grid.add(birthYearField, 1, 5);
        grid.add(birthYear, 0, 5);
        grid.add(websiteField, 1, 6);
        grid.add(website, 0, 6);
        grid.add(bioField, 1, 7);
        grid.add(bio, 0, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeSave) {
                return new Artist(nameField.getText(), bioField.getText(), emailField.getText(), cityField.getText(), phoneField.getText(), Integer.parseInt(birthYearField.getText()), websiteField.getText());
            }
            return null;
        });
        return  dialog;
    }

    private Dialog<Artist> updateArtistDialog(Artist artist) {
        Dialog<Artist> dialog = new Dialog<>();

        String s = "Update Artist";
        dialog.setTitle(s);

        dialog.setHeaderText("Enter the new information of Artist ");

        dialog.setHeaderText(" Enter the information of Artist ");
        TextField nameField = new TextField();
        nameField.setText(artist.getName());
        Label name = new Label("Name: ");

        TextField emailField = new TextField();
        emailField.setText(artist.getContactEmail());
        Label email = new Label("Email: ");

        TextField cityField = new TextField();
        cityField.setText(artist.getCity());
        Label city = new Label("City: ");

        TextField phoneField = new TextField();
        phoneField.setText(artist.getPhone());
        Label phone = new Label("Phone: ");

        TextField birthYearField = new TextField();
        birthYearField.setText(Integer.toString(artist.getBirthYear()));
        Label birthYear = new Label("Birth Year: ");

        TextField websiteField = new TextField();
        websiteField.setText(artist.getWebsite());
        Label website = new Label("Website: ");

        TextField bioField = new TextField();
        bioField.setText(artist.getBio());
        Label bio = new Label("Bio: ");

        ButtonType buttonTypeSave = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeSave);


        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100));

        grid.add(nameField, 1, 1);
        grid.add(name, 0, 1);
        grid.add(emailField, 1, 2);
        grid.add(email, 0, 2);
        grid.add(cityField, 1, 3);
        grid.add(city, 0, 3);
        grid.add(phoneField, 1, 4);
        grid.add(phone, 0, 4);
        grid.add(birthYearField, 1, 5);
        grid.add(birthYear, 0, 5);
        grid.add(websiteField, 1, 6);
        grid.add(website, 0, 6);
        grid.add(bioField, 1, 7);
        grid.add(bio, 0, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeSave) {
                return new Artist(nameField.getText(), bioField.getText(), emailField.getText(), cityField.getText(), phoneField.getText(), Integer.parseInt(birthYearField.getText()), websiteField.getText());
            }
            return null;
        });
        return  dialog;
    }

}
