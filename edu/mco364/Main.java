package edu.mco364;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        final int MAX_THREADS = 50;
        final int MAX_EMAIL = 10_000;
        final String ROOT_URL = "https://www.touro.edu/";

        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        WebCrawler crawler = new WebCrawler(MAX_EMAIL, ROOT_URL);

        while (WebCrawler.getTotalEmails().size() < MAX_EMAIL) {
            threadPool.execute(crawler);
            Thread.sleep(1000);
        }
        threadPool.shutdown();

        String connectionUrl =
                "jdbc:sqlserver://;"
                        + "database=;"
                        + "user=;"
                        + "password=;"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        SQLMethods methods = new SQLMethods(connectionUrl, "EMAILS");
        methods.uploadEmailsToDatabase(WebCrawler.getTotalEmails());
    }
}
