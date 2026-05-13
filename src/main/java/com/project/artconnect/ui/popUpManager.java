package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.util.ConnectionManager;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class popUpManager {

    public static Dialog<String> getConnectionPopUp(){
        Dialog<String> dialog = new Dialog<>();


        dialog.setTitle("Connection");

        dialog.setHeaderText(" Enter your credentials ");
        TextField username = new TextField();
        username.setPromptText("Username");
        TextField password = new TextField();
        password.setPromptText("Password");

        ButtonType buttonTypeLogIn = new ButtonType("Log in", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeLogIn);
        ButtonType buttonTypeNewAccount = new ButtonType("I don't have an account", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeNewAccount);
        ButtonType buttonTypeClose = new ButtonType("close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeClose);

        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide

        grid.add(username, 1, 1);
        grid.add(password, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeLogIn) {
                DatabaseConfig.setUSER(username.getText());
                DatabaseConfig.setPASSWORD(password.getText());
                return "connected";
            }
            if (dialogButton == buttonTypeNewAccount){
                showCreateAccountPopUp();
                DatabaseConfig.setUSER("");
                DatabaseConfig.setPASSWORD("");
                return "user created";
            }
            if(dialogButton == buttonTypeClose){
                return "stop";
            }
            return "ERROR";
        });
        return dialog;
    }


    public static void showCreateAccountPopUp(){
        Dialog<String> dialog = new Dialog<>();


        dialog.setTitle("Create account");

        dialog.setHeaderText(" Enter your credentials ");
        TextField username = new TextField();
        username.setPromptText("Username");
        TextField password = new TextField();
        password.setPromptText("Password");
        TextField confirmPassword = new TextField();
        confirmPassword.setPromptText("confirm password");

        ButtonType buttonTypeCreateAccount = new ButtonType("Create account", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCreateAccount);
        ButtonType buttonTypeHaveAccount = new ButtonType("I already have an account", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeHaveAccount);

        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide

        grid.add(username, 1, 1);
        grid.add(password, 1, 2);
        grid.add(confirmPassword, 1, 3);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeCreateAccount & Objects.equals(password.getText(), confirmPassword.getText())){
                try(Connection connection = ConnectionManager.getConnection()){
                    String sql = "call create_user_communitymember(?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, username.getText());
                    preparedStatement.setString(2, password.getText());
                    preparedStatement.execute();
                    System.out.println("account created");
                }
                catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                return "done";
            }
            else if(dialogButton == buttonTypeCreateAccount & !Objects.equals(password.getText(), confirmPassword.getText())){
                System.out.println("The 2 passwords are not the same");
            }
            return "not done";
        });
        dialog.showAndWait();
    }
}
