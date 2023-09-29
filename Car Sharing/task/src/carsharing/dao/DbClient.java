package carsharing.dao;

import carsharing.objects.Car;
import carsharing.objects.Company;
import carsharing.objects.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbClient {
    private static final String path = "jdbc:h2:./src/carsharing/db/";
    private final String name;

    public DbClient(String name) {
        this.name = name;
        dropAndCreateTables();
    }

    private boolean dropAndCreateTables() {
        try (Connection connection = DriverManager.getConnection(path + name);
             Statement statement = connection.createStatement()
        ) {
//            statement.executeUpdate("drop table if exists CUSTOMER");
//            statement.executeUpdate("drop table if exists CAR");
//            statement.executeUpdate("drop table if exists COMPANY");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS COMPANY(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(40) UNIQUE NOT NULL)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CAR(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(60) UNIQUE NOT NULL, " +
                    "COMPANY_ID INTEGER NOT NULL, " +
                    "CONSTRAINT fk_id FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID))");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CUSTOMER(" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR(60) UNIQUE NOT NULL, " +
                    "RENTED_CAR_ID INTEGER, " +
                    "CONSTRAINT fk_id_customer FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID))");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void run(String query) {
        try (Connection connection = DriverManager.getConnection(path + name);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Company> selectForListCompany(String query) {
        List<Company> companies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(path + name);
             Statement statement = connection.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                int id = resultSetItem.getInt("id");
                String name = resultSetItem.getString("name");
                Company company = new Company(name, id);
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    List<Car> selectForListCar(String query) {
        List<Car> cars = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(path + name);
             Statement statement = connection.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                int id = resultSetItem.getInt("id");
                String name = resultSetItem.getString("name");
                int companyId = resultSetItem.getInt("COMPANY_ID");
                Car car = new Car(id, name, companyId);
                cars.add(car);
            }
            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    //if rentedCarId == null, the query will return 0
    List<Customer> selectForListCustomer(String query) {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(path + name);
             Statement statement = connection.createStatement();
             ResultSet resultSetItem = statement.executeQuery(query)
        ) {
            while (resultSetItem.next()) {
                int id = resultSetItem.getInt("id");
                String name = resultSetItem.getString("name");
                int rentedCarId = resultSetItem.getInt("rented_car_id");
                Customer customer = new Customer(id, name, rentedCarId);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }
}
