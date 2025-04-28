package com.ramyakata.ui;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.ramyakata.persist.entity.CustomerInput;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.CustomerException;
import com.ramyakata.persist.service.CustomerPoolManager;
import com.ramyakata.persist.export.FeedBackExportManager;
import com.ramyakata.persist.thread.ThreadExport;
import com.ramyakata.ui.exception.CustomerFileException;

public class InteractiveCustomerFile {

    private Scanner in;
    private PrintWriter out;
    private PrintWriter err;
    private CustomerPoolManager customerObj;
    private ThreadExport threadExport; 

    public InteractiveCustomerFile() throws CustomerFileException {
        this.in = new Scanner(System.in);
        this.out = new PrintWriter(System.out, true);
        this.err = new PrintWriter(System.err, true);
        try {
            customerObj = new CustomerPoolManager();
            threadExport = new ThreadExport(new FeedBackExportManager(customerObj)); // 🔥 Attach thread export
        } catch (CustomerException e) {
            throw new CustomerFileException("Error initializing customer manager: " + e.getMessage());
        }
    }

    public void start() throws CustomerFileException {
        try {
            int choice = 0;
            do {
                out.println("<----------------Home--------------------->");
                out.println("1.Customer\n2.Staff\n3.Exit\n");
                try {
                    choice = in.nextInt();
                    if (choice < 1 || choice > 3) {
                        err.println("Entered choice is not one of the Option");
                    } else if (choice == 3) {
                        out.println("Exiting....");
                        break;
                    }
                } catch (InputMismatchException e) {
                    err.println("Enter valid input");
                    in.nextLine();
                    continue;
                }
                switch (choice) {
                    case 1:
                        customerOperations();
                        break;
                    case 2:
                        staffOperations();
                        break;
                    default:
                        out.println("Invalid choice. Please enter a number between 1 and 3.");
                        break;
                }
            } while (choice != 3);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    private void customerOperations() throws CustomerFileException {
        out.println("First Time Entering >> Enter Option 1 to Register");
        out.println("Already an existing Customer >> Enter Option 2 to update the rate of our service\n");
        out.println("1.New User\n2.Existing User\n");
        int userChoice = 0;
        try {
            userChoice = in.nextInt();
            if (userChoice < 1 || userChoice > 2) {
                err.println("Entered choice is not one of the Option");
                return;
            }
            in.nextLine();
        } catch (InputMismatchException e) {
            err.println("Enter valid input");
            in.nextLine();
            return;
        }
        switch (userChoice) {
            case 1:
                registerUser();
                break;
            case 2:
                updateUser();
                break;
            default:
                out.println("Invalid choice. Please enter a number between 1 and 2.");
                break;
        }
    }

    private void staffOperations() throws CustomerFileException {
        out.println("1.Fetch all the Existing users\n2.Fetch a customer by name\n3.Sort Customers\n4.Remove Customer\n");
        int staffChoice = 0;
        try {
            staffChoice = in.nextInt();
            if (staffChoice < 1 || staffChoice > 4) {
                err.println("Entered choice is not one of the Option");
                return;
            }
            in.nextLine();
        } catch (InputMismatchException e) {
            err.println("Enter valid input");
            in.nextLine();
            return;
        }
        switch (staffChoice) {
            case 1:
                fetchCustomers();
                break;
            case 2:
                fetchCustomerByName();
                break;
            case 3:
                sortCustomers();
                break;
            case 4:
                removeCustomers();
                break;
            default:
                out.println("Invalid choice. Please enter a number between 1 and 4.");
                break;
        }
    }

    private void registerUser() throws CustomerFileException {
        int id = 0;
        String firstName, lastName, gmail;
        LocalDate today;
        int rate = 0;

        out.println("<--------Welcome!!!------------->");
        out.println("Please enter ID to register: ");
        id = in.nextInt();
        in.nextLine();
        out.println("Please enter your First Name: ");
        firstName = in.nextLine();

        try {
            if (customerObj.getCustomer(id) != null) {
                out.println("User already exists!!! Please select Existing Users");
                return;
            }
        } catch (CustomerException e) {
            out.println("Hello " + firstName);
        }

        out.println("Please enter your Last Name: ");
        lastName = in.nextLine();

        while (true) {
            out.println("Please enter your Gmail Address: ");
            gmail = in.nextLine();
            if (gmail.contains("@gmail.com")) break;
            else err.println("Incorrect Gmail address. Please enter a valid Gmail address.");
        }

        out.println("Please enter Date: (yyyy-MM-dd)");
        String date = in.nextLine();
        try {
            today = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        out.println("Please enter your rating: ");
        rate = in.nextInt();

        CustomerInput inObj = new CustomerInput();
        inObj.setCustomerID(id);
        inObj.setFirstName(firstName);
        inObj.setLastName(lastName);
        try {
            inObj.setEmailAddress(gmail);
        } catch (CustomerException e) {
            err.println("Entered email is not Supported.");
        }
        inObj.setDate(today);
        try {
            inObj.setRate(rate);
        } catch (CustomerException e) {
            err.println("Rating should be from 1 to 5.");
        }

        try {
            customerObj.addCustomer(inObj);
            threadExport.startExport(id);
        } catch (CustomerException e) {
            err.println("Problem While adding: " + e.getMessage());
        }

        out.println("*****User Registered Successfully!!*****");
    }

    private void updateUser() throws CustomerFileException {
        int id = 0;
        String firstName, lastName, gmail;
        LocalDate today;
        int rate = 0;

        out.println("<------------Welcome Back!!!----------------->");
        out.println("Please enter your ID: ");
        id = in.nextInt();
        in.nextLine();
        out.println("Please enter your First Name: ");
        firstName = in.nextLine();

        try {
            if (customerObj.getCustomer(id) == null) {
                err.println("User doesn't exist!!! Please select New Users");
                return;
            }
        } catch (CustomerException e) {
            err.println("User lookup error.");
            return;
        }

        out.println("Please enter your Last Name: ");
        lastName = in.nextLine();

        while (true) {
            out.println("Please enter your Gmail Address: ");
            gmail = in.nextLine();
            if (gmail.contains("@gmail.com")) break;
            else err.println("Incorrect Gmail address. Please enter a valid Gmail address.");
        }

        out.println("Please enter Date: (yyyy-MM-dd)");
        String updateDate = in.nextLine();
        try {
            today = LocalDate.parse(updateDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
            return;
        }

        out.println("Please enter your rating: ");
        rate = in.nextInt();

        CustomerInput inObj1 = new CustomerInput();
        inObj1.setCustomerID(id);
        inObj1.setFirstName(firstName);
        inObj1.setLastName(lastName);
        try {
            inObj1.setEmailAddress(gmail);
        } catch (CustomerException e) {
            err.println("Entered email is not Supported.");
        }
        inObj1.setDate(today);
        try {
            inObj1.setRate(rate);
        } catch (CustomerException e) {
            err.println("Rating should be from 1 to 5.");
        }

        try {
            customerObj.updateCustomer(inObj1);
            threadExport.startExport(id); 
        } catch (CustomerException e) {
            err.println("Problem While Updating: " + e.getMessage());
        }

        out.println("****User Update Successfull!!****");
    }

    private void fetchCustomers() throws CustomerFileException {
        out.println("Fetching all Registered Customers:\n");
        try {
            CustomerOutput[] customers = customerObj.getallCustomers();
            for (CustomerOutput customer : customers) {
                out.printf("%d - %s %s\n", customer.getCustomerID(), customer.getFirstName(), customer.getLatName());
            }
        } catch (CustomerException e) {
            throw new CustomerFileException("Error fetching customers: " + e.getMessage());
        }
    }

    private void fetchCustomerByName() throws CustomerFileException {
        out.println("Please enter Customer ID:\n");
        int id = in.nextInt();
        try {
            CustomerOutput customer = customerObj.getCustomer(id);
            out.printf("%d - %s %s\n", customer.getCustomerID(), customer.getFirstName(), customer.getLatName());
        } catch (CustomerException e) {
            throw new CustomerFileException("Error fetching customer: " + e.getMessage());
        }
    }

    private void sortCustomers() throws CustomerFileException {
        out.println("Sorting Customers by Entry Date:\n");
        try {
            CustomerOutput[] customers = customerObj.sortCustomers();
            for (CustomerOutput customer : customers) {
                out.printf("%d - %s %s\n", customer.getCustomerID(), customer.getFirstName(), customer.getLatName());
            }
        } catch (CustomerException e) {
            throw new CustomerFileException("Error sorting customers: " + e.getMessage());
        }
    }

    private void removeCustomers() throws CustomerFileException {
        out.println("Please enter Customer ID to delete: ");
        int id = in.nextInt();
        try {
            customerObj.removeCustomer(id);
            out.println("Customer " + id + " removed successfully!");
        } catch (CustomerException e) {
            throw new CustomerFileException("Error removing customer: " + e.getMessage());
        }
    }
}
