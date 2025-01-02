package GraphQL;

import Domain.Service.MuskelTrainingServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MuskelTrainingResolver {
    private final MuskelTrainingServiceImpl muskelTrainingService = new MuskelTrainingServiceImpl();

    public List<Map<String, Object>> getAllUebungen() {
        String json = muskelTrainingService.getAllUebungen();
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean createTrainingMitMuskeln(String trainingName, String description, List<String> muskelNamen) {
        try {
            muskelTrainingService.createTrainingMitMuskeln(trainingName, description, muskelNamen);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addMuskelToTraining(String trainingName, String muskelName) {
        try {
            muskelTrainingService.addMuskelToTraining(trainingName, muskelName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTraining(String trainingName) {
        try {
            muskelTrainingService.deleteTraining(trainingName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
