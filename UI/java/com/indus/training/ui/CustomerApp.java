package com.indus.training.ui;

import com.indus.training.ui.exception.CustomerFileException;

public class CustomerApp {

	public static void main(String[] args) {
		InteractiveCustomerFile customerService = new InteractiveCustomerFile();
		try {
			customerService.start();
		} catch (CustomerFileException e) {
			System.err.println("Cannot open\n"+e.getMessage());
		}
	}
}
