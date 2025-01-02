package JDBC;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JDBCMusklen {


        /**
         * Erstellt ein neues Training oder holt es, falls es bereits existiert,
         * und verknüpft es mit mindestens einem Muskel.
         */
        public void createTrainingMitMuskeln(String trainingName, String beschreibung, List<String> muskelNamen) throws SQLException {
            if (muskelNamen == null || muskelNamen.isEmpty()) {
                throw new IllegalArgumentException("Es muss mindestens ein Muskel angegeben werden!");
            }

            // 1) Prüfen, ob Training schon existiert
            Integer trainingId = findTrainingIdByName(trainingName);

            // 2) Wenn es nicht existiert -> neu anlegen
            if (trainingId == null) {
                trainingId = insertNewTraining(trainingName, beschreibung);
            }

            // 3) Für jeden Muskel:
            for (String muskelName : muskelNamen) {
                // a) Prüfen, ob Muskel existiert
                Integer muskelId = findMuskelIdByName(muskelName);
                // b) Wenn nicht existiert -> neu anlegen
                if (muskelId == null) {
                    muskelId = insertNewMuskel(muskelName);
                }

                // c) Verknüpfung in trainings_muskeln erstellen (falls nicht schon vorhanden)
                insertTrainingMuskel(trainingId, muskelId);
            }
        }

        /**
         * Trainings löschen. Da wir ON DELETE CASCADE in trainings_muskeln haben,
         * werden die Verknüpfungen automatisch gelöscht.
         * Optional: Prüfen, ob dadurch Muskeln "verwaisen" und sie ggf. auch löschen.
         */
        public void deleteTraining(String trainingName) throws SQLException {
            Integer trainingId = findTrainingIdByName(trainingName);
            if (trainingId == null) {
                System.out.println("Training '" + trainingName + "' existiert nicht.");
                return;
            }

            // Falls wir Muskeln löschen möchten, die jetzt kein anderes Training haben,
            // müssten wir erst die trainings_muskeln-Einträge entfernen und für jeden betroffenen Muskel checken.
            // Ansonsten reicht:
            try (Connection conn = JDBCHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM trainings WHERE id = ?")) {
                pstmt.setInt(1, trainingId);
                int rows = pstmt.executeUpdate();
                System.out.println("Training gelöscht, rows affected: " + rows);
            }
        }

        /**
         * Fügt ein existierendes Training (trainingName) mit einem neuen oder existierenden Muskel (muskelName) zusammen.
         */
        public void addMuskelToTraining(String trainingName, String muskelName) throws SQLException {
            Integer trainingId = findTrainingIdByName(trainingName);
            if (trainingId == null) {
                throw new IllegalArgumentException("Training '" + trainingName + "' existiert nicht. Bitte zuerst anlegen.");
            }

            Integer muskelId = findMuskelIdByName(muskelName);
            if (muskelId == null) {
                // neuen Muskel anlegen
                muskelId =insertNewMuskel(muskelName);
            }

            insertTrainingMuskel(trainingId, muskelId);
        }

        /**
         * Listet alle Trainings auf, inkl. ihrer Muskeln (für Read-Operation).
         */
        public String getAllUebungenMitMuskeln() {
            // SQL-Query für die Join-Abfrage
            String sql = "SELECT u.id AS uebung_id, u.name AS uebung_name, u.description AS uebung_beschreibung, " +
                    "m.id AS muskel_id, m.name AS muskel_name " +
                    "FROM uebungen u " +
                    "JOIN muskeln_uebungen mu ON u.id = mu.uebung_id " +
                    "JOIN muskeln m ON mu.muskel_id = m.id " +
                    "ORDER BY u.id;";

            // Liste, um alle Übungen mit ihren Muskeln zu speichern
            List<Map<String, Object>> uebungenList = new ArrayList<>();

            try (Connection conn = JDBCHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                // Temporäre Map für Übungen, um Duplikate zu vermeiden
                Map<Integer, Map<String, Object>> uebungenMap = new LinkedHashMap<>();

                while (rs.next()) {
                    int uebungId = rs.getInt("uebung_id");
                    String uebungName = rs.getString("uebung_name");
                    String uebungBeschreibung = rs.getString("uebung_beschreibung");
                    int muskelId = rs.getInt("muskel_id");
                    String muskelName = rs.getString("muskel_name");

                    // Prüfen, ob die Übung bereits existiert
                    Map<String, Object> uebungData = uebungenMap.get(uebungId);
                    if (uebungData == null) {
                        // Neue Übung erstellen
                        uebungData = new LinkedHashMap<>();
                        uebungData.put("id", uebungId);
                        uebungData.put("name", uebungName);
                        uebungData.put("beschreibung", uebungBeschreibung);
                        uebungData.put("muskeln", new ArrayList<Map<String, Object>>());
                        uebungenMap.put(uebungId, uebungData);
                    }

                    // Muskel zur Übung hinzufügen
                    List<Map<String, Object>> muskelList = (List<Map<String, Object>>) uebungData.get("muskeln");
                    Map<String, Object> muskelData = new LinkedHashMap<>();
                    muskelData.put("id", muskelId);
                    muskelData.put("name", muskelName);
                    muskelList.add(muskelData);
                }

                // Alle Übungen aus der Map in eine Liste umwandeln
                uebungenList.addAll(uebungenMap.values());

            } catch (SQLException e) {
                e.printStackTrace();
                // Falls ein Fehler auftritt, eine leere JSON-Liste zurückgeben
                return "[]";
            }

            // JSON mit Jackson erzeugen
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(uebungenList);
            } catch (Exception e) {
                e.printStackTrace();
                return "[]";
            }
        }

        /* ---------------------------------------------------------------- */
        /*   Private Hilfsmethoden                                          */
        /* ---------------------------------------------------------------- */

        /**
         * Prüft, ob es ein Training mit diesem Namen gibt, und gibt ggf. die ID zurück.
         */
        private Integer findTrainingIdByName(String trainingName) throws SQLException {
            String sql = "SELECT id FROM trainings WHERE name = ?";
            try (Connection conn = JDBCHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, trainingName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    } else {
                        return null;
                    }
                }
            }
        }


        private Integer findMuskelIdByName(String muskelName) throws SQLException {
            String sql = "SELECT id FROM muskeln WHERE name = ? LIMIT 1";
            try (Connection conn = JDBCHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, muskelName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");

                    }
                    else {
                        return null;
                    }
                }
            }
        }

        private Integer insertNewMuskel(String muskelName ) throws SQLException {
            String sql = "INSERT INTO muskeln (name) VALUES (?) RETURNING id";
            try (Connection conn = JDBCHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, muskelName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                       return rs.getInt("id");
                    }
                }
            }
            throw  new SQLException("Fehler beim Einfügen eines neuen Muskels mit Name='" + muskelName + "'");

        }

        /**
         * Legt ein neues Training an, gibt die generierte ID zurück.
         */
        private Integer insertNewTraining(String trainingName, String beschreibung) throws SQLException {
            String sql = "INSERT INTO trainings (name, beschreibung) VALUES (?, ?) RETURNING id";
            try (Connection conn = JDBCHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, trainingName);
                pstmt.setString(2, beschreibung);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            throw new SQLException("Fehler beim Anlegen eines neuen Trainings!");
        }

        /**
         * Verknüpft Training und Muskel in der Tabelle trainings_muskeln, wenn noch nicht vorhanden.
         */
        private void insertTrainingMuskel(int trainingId, int muskelId) throws SQLException {
            String sql = "INSERT INTO trainings_muskeln (training_id, muskel_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
            // "ON CONFLICT DO NOTHING" funktioniert in PostgreSQL ab Version 9.5 und verhindert Duplikatfehler.
            // Alternativ: man kann vorher prüfen, ob das Paar schon existiert.

            try (Connection conn = JDBCHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, trainingId);
                pstmt.setInt(2, muskelId);
                pstmt.executeUpdate();
            }
        }
    }




