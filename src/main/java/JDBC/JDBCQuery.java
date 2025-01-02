package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JDBCQuery extends JDBCMusklen {

    public String getMuskelnForUebung(String uebungName) {
        String sql = "SELECT m.name AS muskel "
                + "FROM muskeln m "
                + "JOIN muskeln_uebungen mu ON m.id = mu.muskel_id "
                + "JOIN uebungen u ON mu.uebung_id = u.id "
                + "WHERE u.name = ?";

        List<String> muskeln = new ArrayList<>();

        try (Connection conn = JDBCHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, uebungName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String muskel = rs.getString("muskel");
                    muskeln.add(muskel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Jetzt haben wir eine Liste mit Muskeln. Diese verpacken wir in ein Objekt.
        Map<String, Object> result = new HashMap<>();
        result.put("muskeln", muskeln);
        result.put("uebung", uebungName);


        // In JSON umwandeln
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public String getUebungenForMuskelWithAllMuskeln(String muskelName) {
        String sql = "SELECT u.id, u.name AS uebung, u.description, ARRAY_AGG(DISTINCT m2.name) AS alle_muskeln "
                + "FROM uebungen u "
                + "JOIN muskeln_uebungen mu ON u.id = mu.uebung_id "
                + "JOIN muskeln m ON mu.muskel_id = m.id "
                + "JOIN muskeln_uebungen mu2 ON mu2.uebung_id = u.id "
                + "JOIN muskeln m2 ON m2.id = mu2.muskel_id "
                + "WHERE m.name = ? "
                + "GROUP BY u.id, u.name, u.description";

        List<Map<String, Object>> uebungsListe = new ArrayList<>();

        try (Connection conn = JDBCHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, muskelName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String uebung = rs.getString("uebung");
                    String description = rs.getString("description");
                    java.sql.Array alleMuskelnArray = rs.getArray("alle_muskeln");
                    String[] alleMuskeln = (String[]) alleMuskelnArray.getArray();

                    Map<String, Object> uebungData = new HashMap<>();
                    uebungData.put("uebung", uebung);
                    uebungData.put("description", description);
                    uebungData.put("alle_muskeln", alleMuskeln);
                    uebungsListe.add(uebungData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Jetzt haben wir eine Liste von Übungen mit ihren Muskeln.
        // Wir packen das zusammen mit dem eingegebenen Muskel in ein JSON-Objekt.
        Map<String, Object> result = new HashMap<>();
        result.put("angefragter_muskel", muskelName);
        result.put("uebungen", uebungsListe);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }



}
