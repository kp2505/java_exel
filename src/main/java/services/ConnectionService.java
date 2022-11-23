package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionService {
    LogService logService;

    public ConnectionService(LogService logService) {
        this.logService = logService;
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/world";
        String username = "root";
        String password = "root";
        this.logService.log("Connecting...");

        Properties properties=new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","root");
        /*
          настройки указывающие о необходимости конвертировать данные из Unicode
	  в UTF-8, который используется в нашей таблице для хранения данных
        */
        properties.setProperty("useSSL","false");
        properties.setProperty("characterEncoding","Cp1251");
        properties.setProperty("serverTimezone","Europe/Moscow");

        return DriverManager.getConnection(url, properties);
    }
}
