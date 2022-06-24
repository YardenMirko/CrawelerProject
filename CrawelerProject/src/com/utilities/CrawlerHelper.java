package com.utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrawlerHelper {

    private final HashMap<String, HashMap<String, String>> mUrlToTitleCacheMap = new HashMap<>();

    public Map<String, String> crawlUrl(String URL) {
        if (mUrlToTitleCacheMap.containsKey(URL)) {
            return mUrlToTitleCacheMap.get(URL);
        } else {
            HashMap<String, String> result = new HashMap<>();
            try {
                Document doc = Jsoup.connect(URL).get();
                Elements availableLinksOnPage = doc.select("a[href]");
                for (Element ele : availableLinksOnPage) {
                    String href = ele.attr("href");
                    String title = extractTitle(href);
                    result.put(href, title);
                }
                mUrlToTitleCacheMap.put(URL, result);
                return result;
            } catch (IOException e) {

            }
            return result;
        }
    }

    private String extractTitle(String title) {
        String[] splittedArray = title.split("\\.");
        if (splittedArray.length > 1) {
            return splittedArray[1];
        } else {
            return "";
        }
    }
}

