package com.indus.training.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.indus.training.persist.entity.CustomerInput;
import com.indus.training.persist.entity.CustomerOutput;
import com.indus.training.persist.exception.CustomerException;
import com.indus.training.persist.impl.CustomerManager;
import com.indus.training.ui.exception.CustomerFileException;

public class InteractiveCustomerFile {

	private Scanner in;
	PrintWriter out;
	PrintWriter err;
	CustomerManager customerObj = new CustomerManager();

	public InteractiveCustomerFile() {
		this.in = new Scanner(System.in);
		this.out = new PrintWriter(System.out, true);
		this.err = new PrintWriter(System.err, true);
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

		out.println(
				"1.Fetch all the Existing users\n2.Fetch a customer by name\n3.Sort Customers on daily basis\n4.Remove Customer\n");
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
		// StaffMethods(StaffChoice)
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

		String firstName = null;
		String lastName = null;
		String gmail = null;
		LocalDate today = null;
		int rate = 0;
		out.println("<--------Welcome!!!------------->");
		out.println("Please enter your First Name: ");
		firstName = in.nextLine();
		CustomerOutput existingCustomer;
		try {
			existingCustomer = customerObj.getCustomer(firstName);
			if (existingCustomer != null) {
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
			if (gmail.contains("@gmail.com")) {
				break;
			} else {
				err.println("Incorrect Gmail address. Please enter a valid Gmail address.");
			}
		}
		out.println("Please enter Date: (yyyy-MM-dd)");
		String date = in.nextLine();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			today = LocalDate.parse(date, formatter);
		} catch (DateTimeParseException e) {
			err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
			return; // Exits the program or you can loop to ask again
		}
		out.println("Please enter your rating: ");
		rate = in.nextInt();

		// Exit the method in case of an error
		CustomerInput inObj = new CustomerInput();
		inObj.setFirstName(firstName);
		inObj.setLastName(lastName);
		try {
			inObj.setEmailAddress(gmail);
		} catch (CustomerException e) {
			err.println("Entered email is not Supported. Please enter it again");
		}
		inObj.setDate(today);
		try {
			inObj.setRate(rate);
		} catch (CustomerException e) {
			err.println("Rating should be from 1 to 5");
		}
		try {
			customerObj.addCustomer(inObj);
			CustomerOutput outObj = customerObj.getCustomer(firstName);
			// Creating a .txt file
			createCustomerFile(outObj);
		} catch (CustomerException e) {
			err.println("Problem While adding");
		}
		out.println("*****User Registered Successfully!!*****");
	}

	private void updateUser() throws CustomerFileException {

		String firstName = null;
		String lastName = null;
		String gmail = null;
		LocalDate today = null;
		int rate = 0;
		out.println("<------------Welcome Back!!!----------------->");
		out.println("Please enter your First Name: ");
		firstName = in.nextLine();
		CustomerOutput existingCustomer;
		try {
			existingCustomer = customerObj.getCustomer(firstName);
			if (existingCustomer == null) {
				return;
			}
		} catch (CustomerException e) {
			err.println("User doesn't exist!!! Please select New Users");
			return;
		}
		out.println("Please enter your Last Name: ");
		lastName = in.nextLine();
		while (true) {
			out.println("Please enter your Gmail Address: ");
			gmail = in.nextLine();
			if (gmail.contains("@gmail.com")) {
				break;
			} else {
				err.println("Incorrect Gmail address. Please enter a valid Gmail address.");
			}
		}
		out.println("Please enter Date: (yyyy-MM-dd)");
		String updateDate = in.nextLine();
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			today = LocalDate.parse(updateDate, formatter);
		} catch (DateTimeParseException e) {
			err.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
			return; // Exits the program or you can loop to ask again
		}
		out.println("Please enter your rating: ");
		rate = in.nextInt();
		CustomerInput inObj1 = new CustomerInput();
		inObj1.setFirstName(firstName);
		inObj1.setLastName(lastName);
		try {
			inObj1.setEmailAddress(gmail);
		} catch (CustomerException e) {
			err.println("Entered email is not Supported. Please enter it again");
		}
		inObj1.setDate(today);
		try {
			inObj1.setRate(rate);
		} catch (CustomerException e) {
			err.println("Rating should be from 1 to 5");
		}
		try {
			customerObj.updateCustomer(inObj1);
			CustomerOutput outObj = customerObj.getCustomer(firstName);
			// update .txt file
			createCustomerFile(outObj);
		} catch (CustomerException e) {
			err.println("Problem While Updating");
		}
		out.println("****User Update Successfull!!****");
	}

	private void fetchCustomers() throws CustomerFileException {

		out.println("Fetching all the Registered Customers:\n");
		try {
			CustomerOutput[] outfetchObj = customerObj.getallCustomers();
			out.println(
					"+---------------+---------------+---------------+-------------------------+---------------+----------+");
			out.printf("| %-13s | %-13s | %-13s | %-23s | %-13s | %-8s |%n", "CustomerID", "First Name", "LastName",
					"EMail Address", "Entry Date", "Rating");
			out.println(
					"+---------------+---------------+---------------+-------------------------+---------------+----------+");
			for (int i = 0; i < outfetchObj.length; i++) {
				out.printf("| %-13s | %-13s | %-13s | %-23s | %-13s | %-8s |%n", outfetchObj[i].getCustomerID(),
						outfetchObj[i].getFirstName(), outfetchObj[i].getLatName(), outfetchObj[i].getGmail(),
						outfetchObj[i].getDate(), outfetchObj[i].getRate());
			}
			out.println(
					"+---------------+---------------+---------------+-------------------------+---------------+----------+");
		} catch (CustomerException e) {
			throw new CustomerFileException("Error While fetching all the customers\n" + e.getMessage());
		}
	}

	private void fetchCustomerByName() throws CustomerFileException {

		String name = null;
		out.println("Please enter your First Name Or Last Name:\n");
		name = in.nextLine();
		try {
			CustomerOutput outcustObj = customerObj.getCustomer(name);
			out.println(
					"+---------------+---------------+---------------+-------------------------+---------------+----------+");
			out.printf("| %-13s | %-13s | %-13s | %-23s | %-13s | %-8s |%n", "CustomerID", "First Name", "LastName",
					"EMail Address", "Entry Date", "Rating");
			out.println(
					"+---------------+---------------+---------------+-------------------------+---------------+----------+");
			out.printf("| %-13s | %-13s | %-13s | %-23s | %-13s | %-8s |%n", outcustObj.getCustomerID(),
					outcustObj.getFirstName(), outcustObj.getLatName(), outcustObj.getGmail(), outcustObj.getDate(),
					outcustObj.getRate());
			out.println(
					"+---------------+---------------+---------------+-------------------------+---------------+----------+");

		} catch (CustomerException e) {
			throw new CustomerFileException("Error While fetching the entered customer\n" + e.getMessage());
		}
	}

	private void sortCustomers() throws CustomerFileException {

		out.println("Sorting all the Registered Customers based on the dates they registered:\n ");
		try {
			CustomerOutput[] outfetchObj = customerObj.sortCustomers();
			for (int i = 0; i < outfetchObj.length; i++) {
				out.println(outfetchObj[i].getCustomerID());
				out.println(outfetchObj[i].getFirstName());
				out.println(outfetchObj[i].getLatName());
				out.println(outfetchObj[i].getGmail());
				out.println(outfetchObj[i].getDate());
				out.println(outfetchObj[i].getRate());
			}
		} catch (CustomerException e) {
			throw new CustomerFileException("Error While fetching the entered customer\n" + e.getMessage());
		}
	}

	private void removeCustomers() throws CustomerFileException {

		String name = null;
		out.println("To delete an existing User: ");
		out.println("Please enter your First Name Or Last Name:");
		name = in.nextLine();
		try {
			CustomerOutput outObj = customerObj.getCustomer(name);
			customerObj.removeCustomer(name);
			out.println("User " + name + " is successfully deleted!!!");
			// delete .txt file
			deleteCustomerFile(outObj);
		} catch (CustomerException e) {
			throw new CustomerFileException("Error While removing the customer\n" + e.getMessage());
		}

	}

	private void createCustomerFile(CustomerOutput obj) throws CustomerFileException {
		String filePath = "src/main/resources/" + obj.getCustomerID() + ".txt";
		try (FileWriter file = new FileWriter(filePath)) {
			file.write("CustomerID: " + obj.getCustomerID() + "\n");
			file.write("FirstName: " + obj.getFirstName() + "\n");
			file.write("LastName: " + obj.getLatName() + "\n");
			file.write("E-Mail Address: " + obj.getGmail() + "\n");
			file.write("Date: " + obj.getDate() + "\n");
			file.write("Rating: " + obj.getRate() + "\n");
		} catch (Exception e) {
			throw new CustomerFileException("Error writing to file: " + filePath);
		}
	}

	private void deleteCustomerFile(CustomerOutput obj) {
		String filePath = "src/main/resources/" + obj.getCustomerID() + ".txt";
		File file = new File(filePath);
		if (file.exists()) {
			if (file.delete()) {
				out.println("*****" + obj.getCustomerID() + " File deleted Successfully*****");
			} else {
				err.println("Failed to delete the file " + obj.getCustomerID());
			}
		} else {
			out.println(obj.getCustomerID() + " File not Found");
		}
	}
}
