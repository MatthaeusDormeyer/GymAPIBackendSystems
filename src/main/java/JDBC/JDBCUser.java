package JDBC;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCUser extends JDBCQuery{

    public boolean createUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = JDBCHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);  // noch verschlüsseln
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUsers() {
        // Liste für Benutzer speichern
        List<Map<String, Object>> usersList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = JDBCHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Jeden Benutzer als Map speichern
                Map<String, Object> user = new LinkedHashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "[]"; // Falls ein Fehler auftritt, gibt eine leere JSON-Liste zurück
        }

        // JSON mit Jackson erzeugen
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(usersList);
        } catch (Exception e) {
            e.printStackTrace();
            return "[]"; // Falls ein Fehler bei der JSON-Konvertierung auftritt
        }
    }

    public boolean updateUser(int id, String newUsername, String newPassword) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";

        try (Connection conn = JDBCHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Map<String, Object> user = new LinkedHashMap<>();

        try (Connection conn = JDBCHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user.put("id", rs.getInt("id"));
                    user.put("username", rs.getString("username"));
                } else {
                    return "{}"; // Benutzer nicht gefunden
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{}"; // Fehlerfall
        }

        // JSON mit Jackson erzeugen
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Fehler bei der JSON-Konvertierung
        }
    }



    public String getUserByName(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        Map<String, Object> user = new LinkedHashMap<>();

        try (Connection conn = JDBCHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user.put("id", rs.getInt("id"));
                    user.put("username", rs.getString("username"));
                } else {
                    return "{}"; // Benutzer nicht gefunden
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "{}"; // Fehlerfall
        }

        // JSON mit Jackson erzeugen
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Fehler bei der JSON-Konvertierung
        }
    }




    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = JDBCHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
