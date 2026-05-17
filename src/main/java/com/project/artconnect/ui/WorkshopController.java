package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDateTime;

public class WorkshopController {
    @FXML
    private ComboBox<String> filterLevel;
    @FXML
    private TableView<Workshop> workshopTable;
    @FXML
    private TableColumn<Workshop, String> titleColumn;
    @FXML
    private TableColumn<Workshop, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Workshop, String> instructorColumn;
    @FXML
    private TableColumn<Workshop, Double> priceColumn;
    @FXML
    private TableColumn<Workshop, String> levelColumn;
    @FXML
    private TableColumn<Workshop, String> nbUserInWorkshopColumn;
    @FXML
    private TableColumn<Workshop, String> timeColumn;
    @FXML
    private TableColumn<Workshop, String> descriptionColumn;

    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));


        nbUserInWorkshopColumn.setCellValueFactory(cellData ->new SimpleStringProperty(
                workshopService.getNumberOfBooking(cellData.getValue())
        ));

        instructorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getInstructor() != null ? cellData.getValue().getInstructor().getName()
                        : "Unknown"));

        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
        filterLevel.setItems(FXCollections.observableArrayList(null, "Advanced", "Intermediate", "Beginner"));
    }

    @FXML
    private void handleRegister(){
        Workshop selectedWorkshop = workshopTable.getSelectionModel().getSelectedItem();
        CommunityMemberDao communityMemberDao = new JdbcCommunityMemberDao();
        CommunityMember communityMember = communityMemberDao.findByEmail(DatabaseConfig.getUSER());

        WorkshopDao workshopDao = new JdbcWorkshopDao();
        workshopDao.registerToWorkshop(selectedWorkshop, communityMember);
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String filter = filterLevel.getValue();
        if (filter != null) {
            workshopTable.setItems(FXCollections.observableArrayList(workshopService.filterByLevel(filter)));
        }
    }

    @FXML
    private void handleReset() {
        filterLevel.setValue(null);
        refreshTable();
    }

    private void refreshTable() {
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }

    @FXML
    private void handleSeeBookings(){
        CommunityMemberDao communityMemberDao = new JdbcCommunityMemberDao();
        CommunityMember communityMember = communityMemberDao.findByEmail(DatabaseConfig.getUSER());
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getWorkshopsByMember(communityMember)));
    }
}
