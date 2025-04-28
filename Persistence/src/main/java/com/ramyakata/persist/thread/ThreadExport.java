package com.ramyakata.persist.thread;

import com.ramyakata.persist.dao.IExportThread;
import com.ramyakata.persist.dao.IExportManager;
import com.ramyakata.persist.exception.CustomerException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadExport implements IExportThread {

    private IExportManager exportManager;
    private static final ExecutorService executor = Executors.newFixedThreadPool(3);

    public ThreadExport(IExportManager exportManager) {
        this.exportManager = exportManager;
    }

    @Override
    public void startExport(int customerId) {
        executor.submit(() -> {
            try {
                exportManager.exportToTxt(customerId);
            } catch (CustomerException e) {
                System.err.println("Error exporting TXT: " + e.getMessage());
            }
        });

        executor.submit(() -> {
            try {
                exportManager.exportToCsv(customerId);
            } catch (CustomerException e) {
                System.err.println("Error exporting CSV: " + e.getMessage());
            }
        });

        executor.submit(() -> {
            try {
                exportManager.exportToJson(customerId);
            } catch (CustomerException e) {
                System.err.println("Error exporting JSON: " + e.getMessage());
            }
        });
    }

    public static void shutdownExecutor() {
        executor.shutdown();
        System.out.println("Export thread pool shutdown complete.");
    }
}
