package com.project.artconnect.ui;

import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;


public class TestConnection {

    public static void main(String[] args) {
        System.out.println("Attempting to connect to the School database...");
        try (Connection conn = ConnectionManager.getConnection()) {
            System.out.println("[OK] Connection to the ArtProject database successful!");
            System.out.println("Catalog: " + conn.getCatalog());
            System.out.println("Current Auto-commit: " + conn.getAutoCommit());
        } catch (UnsupportedOperationException e) {
            System.out.println("[TODO] ConnectionManager not yet implemented: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[ERROR] Connection failed: " + e.getMessage());
            System.err.println("Verify the URL, username, and password in ConnectionManager.");
        }
    }
}
