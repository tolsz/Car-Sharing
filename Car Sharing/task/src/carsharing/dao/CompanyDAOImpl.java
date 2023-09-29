package carsharing.dao;

import carsharing.objects.Company;

import java.util.List;

public class CompanyDAOImpl implements CompanyDAO {
    private static final String SELECT_ALL = "SELECT * FROM COMPANY";
    private static final String INSERT = "INSERT INTO COMPANY (name) VALUES ('%s')";
    private static final String GET_BY_ID = "SELECT * FROM COMPANY WHERE ID = %d";
    DbClient client;

    public CompanyDAOImpl(DbClient client) {
        this.client = client;
    }

    @Override
    public List<Company> selectAll() {
        return client.selectForListCompany(SELECT_ALL);
    }

    @Override
    public void insert(String name) {
        client.run(String.format(INSERT, name));
    }

    @Override
    public Company getById(int id) {
        return client.selectForListCompany(String.format(GET_BY_ID, id)).get(0);
    }
}
