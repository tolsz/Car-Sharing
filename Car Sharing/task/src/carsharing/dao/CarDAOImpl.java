package carsharing.dao;

import carsharing.objects.Car;

import java.util.List;

public class CarDAOImpl implements CarDAO {
    private static final String SELECT_ALL = "SELECT * FROM CAR WHERE COMPANY_ID = %d";
    private static final String INSERT = "INSERT INTO CAR (name, company_id) VALUES ('%s', %d)";
    private static final String GET_BY_ID = "SELECT * FROM CAR WHERE ID = %d";
    private static final String GET_NOT_RENTED = "SELECT * " +
            "FROM CAR " +
            "WHERE NOT EXISTS (SELECT 1 FROM CUSTOMER WHERE CUSTOMER.RENTED_CAR_ID = CAR.ID) " +
            "AND COMPANY_ID = 1";

    DbClient client;

    public CarDAOImpl(DbClient client) {
        this.client = client;
    }

    @Override
    public List<Car> selectAll(int companyID) {
        return client.selectForListCar(String.format(SELECT_ALL, companyID));
    }

    @Override
    public void insert(String name, int companyID) {
        client.run(String.format(INSERT, name, companyID));
    }

    @Override
    public Car getById(int id) {
        List<Car> car = client.selectForListCar(String.format(GET_BY_ID, id));
        return car.get(0);
    }

    @Override
    public List<Car> selectNotRented(int companyId) {
        return client.selectForListCar(String.format(GET_NOT_RENTED, companyId));
    }
}
