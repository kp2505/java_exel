package facades;

import data.Data;

import java.sql.*;
import java.util.Iterator;
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


    public void insert(List<Data> dataList) {
        try (Connection connection = this.getConnection()) {
            Statement stmt = connection.createStatement();
            String query = "";

            int i = 0;
            Iterator<Data> it  = dataList.iterator();

            while (it.hasNext()) {
                Data item = it.next();
                i++;
                query += this.getQueryPart(item);
                Boolean isUpdateStep = i % 10000 == 0 || !it.hasNext();

                if (isUpdateStep) {
                    System.out.println(i);
                    stmt.executeUpdate("INSERT into test values "+ query +';');
                    query = "";
                } else {
                    query += ",";
                }
            }

            System.out.println("Insert successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public String getQueryPart(Data data) {
        return "(" +
                this.getQueryElement(data.val1, false) +
                this.getQueryElement(data.val2, false) +
                this.getQueryElement(data.val3, false) +
                this.getQueryElement(data.val4, true) +
                ")";
    }

    public String getQueryElement(String element,Boolean isLast) {
        String separator = isLast ? "" : ",";

        return "'"+element+"'" + separator;
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

