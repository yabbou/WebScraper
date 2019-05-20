package edu.mco364;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class WebCrawler{
    private Set<String> totalEmails = new HashSet<>();
    private List<String> totalLinks = new ArrayList<>();
    private int linkIndex = 0;

    Set<String> getTotalEmails() {
        return totalEmails;
    }

    void beginBreadthFirstSearch(String URL) {
        totalLinks.add(URL);
        breadthFirstSearchForEmails(totalLinks.get(linkIndex++));
    }

    private void breadthFirstSearchForEmails(String URL) {
        final int MAX_EMAIL_COUNT = 200; 
        final int INDEX_OF_EMAIL = 1;
        String currentURL;

        try {
            Document rootURL = Jsoup.connect(URL)
                    .userAgent("Chrome")
                    .get();
            Elements currentLinks = rootURL.select("a[href]");

            for (Element link : currentLinks) {
                currentURL = link.attr("abs:href");

                if (!totalLinks.contains(currentURL)) {
                    if (!currentURL.startsWith("mailto:"))
                        totalLinks.add(currentURL);
                    else if (totalEmails.size() < MAX_EMAIL_COUNT) {
                        totalEmails.add(currentURL.split("mailto:")[INDEX_OF_EMAIL]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (totalEmails.size() < MAX_EMAIL_COUNT) {
            breadthFirstSearchForEmails(totalLinks.get(linkIndex++));
        }
    }
}
