package Domain.Service;

import JDBC.JDBCHelper;

import java.sql.SQLException;
import java.util.List;

public class MuskelTrainingServiceImpl {
    JDBCHelper jdbc = new JDBCHelper();

    public String getAllUebungen(){
        return jdbc.getAllUebungenMitMuskeln();
    }
    public void addMuskelToTraining(String trainingName, String muskelName) throws SQLException {
        jdbc.addMuskelToTraining(trainingName, muskelName);
    }
    public void createTrainingMitMuskeln(String trainingName, String beschreibung, List<String> muskelNamen) throws SQLException {
        jdbc.createTrainingMitMuskeln(trainingName, beschreibung, muskelNamen);
    }
    public void deleteTraining(String trainingName) throws SQLException {
        jdbc.deleteTraining(trainingName);
    }





}
