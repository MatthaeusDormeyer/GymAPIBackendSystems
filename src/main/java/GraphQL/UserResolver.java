package GraphQL;

import java.util.*;
import Domain.Service.UserServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserResolver {

    private final UserServiceImpl userService = new UserServiceImpl();

    public List<Map<String, Object>> getAllUsers() {
        String json = userService.getAllUsers();
        // JSON in Java-Objekte umwandeln
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Map<String, Object> getUserById(int id) {
        String json = userService.getUserById(id);
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public Map<String, Object> getUserByName(String username) {
        String json = userService.getUserByName(username);
        try {
            return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public boolean createUser(String username, String password) {
        userService.createUser(username, password);
        return true;
    }

    public boolean updateUser(int id, String username, String password) {
        userService.updateUser(id, username, password);
        return true;
    }

    public boolean deleteUser(int id) {
        userService.deleteUser(id);
        return true;
    }
}
