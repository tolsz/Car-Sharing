package carsharing;

import carsharing.dao.*;
import carsharing.objects.Car;
import carsharing.objects.Company;
import carsharing.objects.Customer;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private static  final Scanner scanner = new Scanner(System.in);
    private final CarDAO carDAO;
    private final CompanyDAO companyDAO;
    private final CustomerDAO customerDAO;

    public Menu(String[] args) {
        String name;
        try {
            name = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            name = "database";
        }
        DbClient client = new DbClient(name);
        carDAO = new CarDAOImpl(client);
        companyDAO = new CompanyDAOImpl(client);
        customerDAO = new CustomerDAOImpl(client);
    }

    public void run() {
        while (true) {
            System.out.println("""
                    1. Log in as a manager
                    2. Log in as a customer
                    3. Create a customer
                    0. Exit""");
            String option = scanner.nextLine();
            if ("1".equals(option)) {
                logAsManager();
            } else if ("2".equals(option)) {
                logAsCustomer();
            } else if ("3".equals(option)) {
                createCustomer();
            } else if ("0".equals(option)) {
                break;
            } else {
                System.out.println("Wrong option");
            }
        }
    }

    private void logAsManager() {
        boolean loop = true;
        while (loop) {
            System.out.println("""
                    \n1. Company list
                    2. Create a company
                    0. Back""");
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> {
                    int companyId = chooseCompany();
                    if (companyId != 0) carMenu(companyId);
                }
                case "2" -> {
                    System.out.println("\nEnter the company name: ");
                    String name = scanner.nextLine();
                    companyDAO.insert(name);
                    System.out.println("The company was created!");
                }
                case "0" -> {
                    loop = false;
                    System.out.println();
                }
                default -> System.out.println("Wrong option");
            }
        }
    }

    //0 - nothing
    private int chooseCompany() {
        boolean loop = true;
        while (loop) {
            List<Company> companies = companyDAO.selectAll();
            if (companies.isEmpty()) {
                System.out.println("\nThe company list is empty!");
                return 0;
            }
            companies.forEach(n -> System.out.println(n.getId() + ". " + n.getName()));
            System.out.println("0. Back");
            String option = scanner.nextLine();
            try {
                int opt = Integer.parseInt(option);
                if (opt < 0 || opt > companies.size()) System.out.println("Wrong option");
                else if (opt == 0) loop = false;
                else {
                    Company company = companies.get(opt - 1);
                    System.out.println("\n'" + company.getName() + "' company");
                    return company.getId();
                }
            } catch (NumberFormatException e) {
                System.out.println("Wrong option");
            }
        }
        return 0;
    }

    private void carMenu(int companyId) {
        boolean loop = true;
        while (loop) {
            System.out.println("""
                    1. Car list
                    2. Create a car
                    0. Back""");
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> {
                    List<Car> cars = carDAO.selectAll(companyId);
                    if (cars.isEmpty()) {
                        System.out.println("\nThe car list is empty!\n");
                        continue;
                    }
                    int number = 1;
                    System.out.println("\nCar list:");
                    for (Car car : cars) {
                        System.out.println(number + ". " + car.getName());
                        number++;
                    }
                    System.out.println();
                }
                case "2" -> {
                    System.out.println("\nEnter the car name:");
                    String name = scanner.nextLine();
                    carDAO.insert(name, companyId);
                    System.out.println("The car was added!\n");
                }
                case "0" -> loop = false;
                default -> System.out.println("Wrong option");
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = scanner.nextLine();
        customerDAO.insert(name);
        System.out.println("The customer was added!\n");
    }

    private void logAsCustomer() {
        List<Customer> customers = customerDAO.selectAll();
        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty!\n");
            return;
        }

        System.out.println("\nCustomer list:");
        customers.forEach(n -> System.out.println(n.getId() + ". " + n.getName()));
        System.out.println("0. Back");
        String option = scanner.nextLine();
        Customer choice = null;
        try {
            int opt = Integer.parseInt(option);
            if (opt == 0) return;
            else choice = customers.get(opt - 1);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Wrong option");
        }
        if (choice != null) {
            customerOptions(choice);
        }
    }

    private void customerOptions(Customer customer){
        boolean loop = true;
        while (loop) {
            System.out.println("""
                \n1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back""");
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> rentCar(customer);
                case "2" -> {
                    if (customer.getRentedCarId() == 0) {
                        System.out.println("\nYou didn't rent a car!");
                    } else {
                        customerDAO.returnCar(customer.getId());
                        customer.setRentedCarId(0);
                        System.out.println("\nYou've returned a rented car!");
                    }
                }
                case "3" -> {
                    int rentedCarId = customer.getRentedCarId();
                    if (rentedCarId == 0) {
                        System.out.println("\nYou didn't rent a car!");
                    } else {
                        Car car = carDAO.getById(rentedCarId);
                        Company company = companyDAO.getById(car.getCompanyId());
                        System.out.println("\nYour rented car:\n" + car.getName());
                        System.out.println("Company:\n" + company.getName());
                    }
                }
                case "0" -> {
                    loop = false;
                    System.out.println();
                }
                default -> System.out.println("Wrong option");
            }
        }
    }

    private void rentCar(Customer customer) {
        if (customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
            return;
        }
        System.out.println("\nChoose a company:");
        int companyId = chooseCompany();
        if (companyId == 0) return;
        List<Car> cars = carDAO.selectNotRented(companyId);
        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!");
            return;
        }
        int number = 1;
        System.out.println("\nChoose a car:");
        for (Car car : cars) {
            System.out.println(number + ". " + car.getName());
            number++;
        }
        String option = scanner.nextLine();
        try {
            int opt = Integer.parseInt(option);
            if (opt == 0) return;
            Car car = cars.get(opt - 1);
            customerDAO.rentCar(car.getId(), customer.getId());
            System.out.println("\nYou rented '" + car.getName() + "'");
            customer.setRentedCarId(car.getId());
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong option");
        }
    }
}
