package facades;

import constants.AppConstants;
import data.Provider;
import data.ProviderConfig;
import data.Sku;
import services.ConnectionService;
import services.LogService;

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DatabaseFacade {
    LogService logService;
    ConnectionService connectionService;

    public DatabaseFacade(LogService logService) {
        this.logService = logService;
        this.connectionService = new ConnectionService(logService);
    }

    public void clearTable() {
        try (Connection connection = this.connectionService.getConnection()) {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("delete from providers;");
            this.logService.log("deleting success!");
        } catch (SQLException e) {
            this.logService.log("Connection failed!");
            e.printStackTrace();
        }
    }


    public void insert(List<Provider> dataList) {
        try (Connection connection = this.connectionService.getConnection()) {
            Statement stmt = connection.createStatement();
            String query = "";

            int i = 0;
            Iterator<Provider> it  = dataList.iterator();

            while (it.hasNext()) {
                Provider item = it.next();
                i++;
                query += this.getQueryPart(item);
                Boolean isUpdateStep = i % 1000 == 0 || !it.hasNext();

                if (isUpdateStep) {
                    this.logService.log(i);
                    stmt.executeUpdate("INSERT into providers values "+ query +';');
                    query = "";
                } else {
                    query += ",";
                }
            }

            this.logService.log("Insert successful!");
        } catch (SQLException e) {
            this.logService.log("Connection failed!");
            e.printStackTrace();
        }
    }

    public void insertSqu(ProviderConfig providerConfig) {
        List<Sku> dataList = providerConfig.existedSkuList;

        try (Connection connection = this.connectionService.getConnection()) {
            Statement stmt = connection.createStatement();
            String query = "";

            int i = 0;
            Iterator<Sku> it  = dataList.iterator();

            this.logService.log("Insert sku started!");
            while (it.hasNext()) {
                Sku item = it.next();
                i++;
                String id = item.id.replaceAll("\'","");
                String value = item.value.replaceAll("\'","");;
                query += "("+
                        "'"+id+"',"+
                        "'"+value+"'"+
                        ")";
                Boolean isUpdateStep = i % 1000 == 0 || !it.hasNext();

                if (isUpdateStep) {
                    if(i> 1) {
                        stmt.executeUpdate("INSERT into sku values " + query + ';');
                    }
                    query = "";
                } else {
                    query += ",";
                }
            }

            this.logService.log("Insert sku successful!");
        } catch (SQLException e) {
            this.logService.log("Connection failed!");
            e.printStackTrace();
        }
    }

    public String getQueryPart(Provider provider) {
        return "(" +
                this.getQueryElement(provider.provider, false) +
                this.getQueryElement(provider.vendorCode, false) +
                this.getQueryElement(provider.brand, false) +
                this.getQueryElement(provider.sku, false) +
                this.getQueryElement(provider.count, false) +
                this.getQueryElement(provider.minCount, false) +
                this.getQueryElement(provider.price, false) +
                this.getQueryElement(provider.maxPrice, false) +
                this.getQueryElement(provider.minPrice, false) +
                this.getQueryElement(provider.rating, true) +
                ")";
    }

    public String getQueryElement(String element,Boolean isLast) {
        String separator = isLast ? "" : ",";
        String formattedString = element.replaceAll("\"","'");

        return '"' + formattedString+ '"' + separator;
    }

    public String getQueryElement(int element,Boolean isLast) {
        String separator = isLast ? "" : ",";

        return element + separator;
    }

    public String getQueryElement(double element,Boolean isLast) {
        String separator = isLast ? "" : ",";

        return element + separator;
    }

    public List select() {
        List ll = new LinkedList();
        try (Connection connection = this.connectionService.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery(AppConstants.SELECT_PROVIDERS);
            while (rs.next()) {
                String provider = rs.getString("provider");
                String vendorCode = rs.getString("vendor_code");
                String brand = rs.getString("brand");
                String sku = rs.getString("sku");
                double count = rs.getInt("count");
                double price = rs.getDouble("price");
                double minPrice = rs.getDouble("min_price");
                double maxPrice = rs.getDouble("max_price");
                double minCount = rs.getInt("min_count");
                double rating = rs.getInt("rating");
                //this.logService.log(brand);

                //Assuming you have a user object
                Provider user = new Provider(
                        provider,
                        vendorCode,
                        brand,
                        sku,
                        count,
                        minCount,
                        price,
                        maxPrice,
                        minPrice,
                        rating
                );

                ll.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ll;
    }
}

