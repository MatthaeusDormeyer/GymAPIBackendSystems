package Domain.Service;

import JDBC.JDBCHelper;

public class UserServiceImpl {

    JDBCHelper jdbc = new JDBCHelper();


    public String getAllUsers() {
        return jdbc.getUsers(); // JDBC-Methode
    }


    public String getUserById(int id) {
        return jdbc.getUserById(id); // JDBC-Methode
    }

    public String getUserByName(String username) {
        return jdbc.getUserByName(username);
    }


    public void createUser(String username, String password) {
        jdbc.createUser(username, password);
    }


    public void updateUser(int id, String username, String password) {
        jdbc.updateUser(id, username, password);
    }


    public void deleteUser(int id) {
        jdbc.deleteUser(id);
    }
}

