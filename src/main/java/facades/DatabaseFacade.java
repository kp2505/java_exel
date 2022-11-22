package facades;

import data.Data;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DatabaseFacade {

    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/world?serverTimezone=Europe/Moscow&useSSL=false";
        String username = "root";
        String password = "root";
        System.out.println("Connecting...");

        return DriverManager.getConnection(url, username, password);
    }

    public void clearTable() {
        try (Connection connection = this.getConnection()) {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("delete from test;");
            System.out.println("deleting success!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }


    public void insert(String query) {
        try (Connection connection = this.getConnection()) {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("INSERT into test values "+ query);
            System.out.println("Connection successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public List select() {
        List ll = new LinkedList();
        try (Connection connection = this.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("select * from test;");
            while (rs.next()) {
                String val1 = rs.getString("column_name_1");
                String val2 = rs.getString("column_name_2");
                String val3 = rs.getString("column_name_3");
                String val4 = rs.getString("column_name_4");

                //Assuming you have a user object
                Data user = new Data(val1,val2,val3,val4);

                ll.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ll;
    }
}

