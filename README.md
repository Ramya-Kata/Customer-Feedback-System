# Customer Feedback Management System

This is a simple Java-based Customer Feedback Management System built with a **2-tier architecture**:

- **Persistence module**: Handles database operations and feedback export (TXT, CSV, JSON).
- **UI module**: Provides a console-based user interface for customer and staff interactions.

---

## Technologies Used

- Java 8+
- Maven
- MySQL (JDBC)
- Multithreading (ExecutorService)
- JUnit Testing

---

## Features

- Add, update, fetch, sort, and remove customer feedback.
- Automatically export customer data to **TXT**, **CSV**, and **JSON** files.
- Configurable database and export settings through properties files.

---

## How to Run

1. Build the `Persistence/` and `UI/` modules separately using Maven.
2. Run the `CustomerApp` class from the UI module.
3. Follow the console prompts to register, update, view, and manage feedback.

---

## Important

- Ensure database connection details are correctly configured inside `appconfig.properties`.
- Exported feedback files are saved automatically after customer operations.

---

# Customer-Feedback-System
