package com.ramyakata.ui;

import com.ramyakata.ui.exception.CustomerFileException;

public class CustomerApp {

    public static void main(String[] args) {
        try {
            InteractiveCustomerFile customerService = new InteractiveCustomerFile();
            customerService.start();
        } catch (CustomerFileException e) {
            System.err.println("Cannot run UI: " + e.getMessage());
        }
    }
}
