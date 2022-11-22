package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionService {
    LogService logService;

    public ConnectionService(LogService logService) {
        this.logService = logService;
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/world?serverTimezone=Europe/Moscow&useSSL=false";
        String username = "root";
        String password = "root";
        this.logService.log("Connecting...");

        return DriverManager.getConnection(url, username, password);
    }
}
