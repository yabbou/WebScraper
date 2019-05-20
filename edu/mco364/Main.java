package edu.mco364;

public class Main {

    public static void main(String[] args) {
        String rootURL = "https://www.touro.edu/";

        WebCrawler crawler = new WebCrawler();
        crawler.beginBreadthFirstSearch(rootURL);

        String connectionUrl =
                "jdbc:sqlserver://;"
                        + "database=;"
                        + "user=;"
                        + "password=;"
                        + "encrypt=false;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";

        SQLMethods methods = new SQLMethods(connectionUrl, "Abbou364", "EMAILS");
        methods.uploadEmailsToDatabase(crawler.getTotalEmails());
    }
}
