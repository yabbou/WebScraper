package edu.mco364;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

class WebCrawler implements Runnable {

    WebCrawler(final int MAX_EMAIL, String ROOT_URL) {
        WebCrawler.MAX_EMAIL = MAX_EMAIL;
        totalLinks.add(ROOT_URL);
    }

    private static int MAX_EMAIL;
    private static Set<String> totalEmails = Collections.synchronizedSet(new HashSet<>(MAX_EMAIL));
    private static Queue<String> totalLinks = new ConcurrentLinkedQueue<>();

    static Set<String> getTotalEmails() {
        return totalEmails;
    }

    private void breadthFirstSearchForEmails(String URL) {
        final int INDEX_OF_EMAIL = 1;
        String currentLink;

        try {
            Document website = Jsoup.connect(URL)
                    .userAgent("Chrome")
                    .get();
            Elements currentLinks = website.select("a[href]");

            for (Element link : currentLinks) {
                currentLink = link.attr("abs:href");

                if (!totalLinks.contains(currentLink)) {
                    if (!currentLink.startsWith("mailto:"))
                        totalLinks.add(currentLink);
                    else if (totalEmails.size() < MAX_EMAIL) {
                        totalEmails.add(currentLink.split("mailto:")[INDEX_OF_EMAIL]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (totalEmails.size() < MAX_EMAIL) {
            breadthFirstSearchForEmails(totalLinks.remove());
        }
    }

    @Override
    public void run() {
        synchronized (totalLinks) {
            breadthFirstSearchForEmails(totalLinks.remove());
        }
    }
}
