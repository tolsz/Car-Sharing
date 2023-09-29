package carsharing.dao;

import carsharing.objects.Car;

import java.util.List;

public interface CarDAO {
    List<Car> selectAll(int companyID);
    void insert(String name, int companyID);
    Car getById(int id);
    List<Car> selectNotRented(int companyId);
}
