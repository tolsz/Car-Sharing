package carsharing.dao;

import carsharing.objects.Company;

import java.util.List;

public interface CompanyDAO {
    List<Company> selectAll();
    void insert(String name);
    Company getById(int id);
}
