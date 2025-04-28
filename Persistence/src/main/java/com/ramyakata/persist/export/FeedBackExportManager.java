package com.ramyakata.persist.export;

import com.ramyakata.persist.dao.IExportManager;
import com.ramyakata.persist.dao.ICustomerManager;
import com.ramyakata.persist.entity.CustomerOutput;
import com.ramyakata.persist.exception.CustomerException;
import com.ramyakata.persist.util.AppConfigReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FeedBackExportManager implements IExportManager {

    private ICustomerManager customerManager;
    private static final String EXPORT_FOLDER;


    static {
        String folder = AppConfigReader.getProperty("export.folder.path");
        if (folder != null && !folder.isEmpty()) {
            EXPORT_FOLDER = folder;
        } else {
            EXPORT_FOLDER = "src/main/resources/exports/";
        }
    }

    public FeedBackExportManager(ICustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    private void ensureExportFolderExists() throws CustomerException {
        File folder = new File(EXPORT_FOLDER);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                throw new CustomerException("Failed to create export folder.");
            }
        }
    }

    @Override
    public void exportToTxt(int customerId) throws CustomerException {
        ensureExportFolderExists();
        try (FileWriter writer = new FileWriter(EXPORT_FOLDER + customerId + ".txt")) {
            CustomerOutput customer = customerManager.getCustomer(customerId);
            writer.write("ID: " + customer.getCustomerID() + "\n");
            writer.write("First Name: " + customer.getFirstName() + "\n");
            writer.write("Last Name: " + customer.getLatName() + "\n");
            writer.write("Email: " + customer.getGmail() + "\n");
            writer.write("Date: " + customer.getDate() + "\n");
            writer.write("Rating: " + customer.getRate() + "\n");
            System.out.println("TXT Exported for customer: " + customerId);
        } catch (IOException e) {
            throw new CustomerException("Error writing TXT file: " + e.getMessage());
        }
    }

    @Override
    public void exportToCsv(int customerId) throws CustomerException {
        ensureExportFolderExists();
        try (FileWriter writer = new FileWriter(EXPORT_FOLDER + customerId + ".csv")) {
            CustomerOutput customer = customerManager.getCustomer(customerId);
            writer.write("ID,FirstName,LastName,Email,Date,Rating\n");
            writer.write(customer.getCustomerID() + "," +
                         customer.getFirstName() + "," +
                         customer.getLatName() + "," +
                         customer.getGmail() + "," +
                         customer.getDate() + "," +
                         customer.getRate() + "\n");
            System.out.println("CSV Exported for customer: " + customerId);
        } catch (IOException e) {
            throw new CustomerException("Error writing CSV file: " + e.getMessage());
        }
    }

    @Override
    public void exportToJson(int customerId) throws CustomerException {
        ensureExportFolderExists();
        try (FileWriter writer = new FileWriter(EXPORT_FOLDER + customerId + ".json")) {
            CustomerOutput customer = customerManager.getCustomer(customerId);
            writer.write("{\n");
            writer.write("\"id\": " + customer.getCustomerID() + ",\n");
            writer.write("\"firstName\": \"" + customer.getFirstName() + "\",\n");
            writer.write("\"lastName\": \"" + customer.getLatName() + "\",\n");
            writer.write("\"email\": \"" + customer.getGmail() + "\",\n");
            writer.write("\"date\": \"" + customer.getDate() + "\",\n");
            writer.write("\"rating\": " + customer.getRate() + "\n");
            writer.write("}\n");
            System.out.println("JSON Exported for customer: " + customerId);
        } catch (IOException e) {
            throw new CustomerException("Error writing JSON file: " + e.getMessage());
        }
    }
}
