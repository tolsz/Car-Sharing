package carsharing.dao;

import carsharing.objects.Customer;

import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private static final String SELECT_ALL = "SELECT * FROM CUSTOMER";
    private static final String INSERT = "INSERT INTO CUSTOMER (name) VALUES ('%s')";
    private static final String GET_BY_ID = "SELECT * FROM CUSTOMER WHERE ID = %d";
    private static final String SET_CAR_ID_NULL = "UPDATE CUSTOMER SET RENTED_CAR_ID = null WHERE ID = %d";
    private static final String SET_CAR_ID = "UPDATE CUSTOMER SET RENTED_CAR_ID = %d WHERE ID = %d";
    DbClient client;

    public CustomerDAOImpl(DbClient client) {
        this.client = client;
    }

    @Override
    public List<Customer> selectAll() {
        return client.selectForListCustomer(SELECT_ALL);
    }

    @Override
    public void insert(String name) {
        client.run(String.format(INSERT, name));
    }

    @Override
    public Customer getById(int id) {
        List<Customer> customers = client.selectForListCustomer(String.format(GET_BY_ID, id));
        return customers.get(0);
    }

    @Override
    public void returnCar(int id) {
        client.run(String.format(SET_CAR_ID_NULL, id));
    }

    @Override
    public void rentCar(int carId, int customerId) {
        client.run(String.format(SET_CAR_ID, carId, customerId));
    }
}
