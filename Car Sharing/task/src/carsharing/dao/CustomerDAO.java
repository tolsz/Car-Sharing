package carsharing.dao;

import carsharing.objects.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> selectAll();
    void insert(String name);
    Customer getById(int id);
    void returnCar(int id);
    void rentCar(int carId, int customerId);
}
