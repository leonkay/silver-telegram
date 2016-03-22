package com.leonkay.fun.web.content;

import java.util.List;

/**
 * Defines the API for scraping the contents of a string for a specified values
 * @author leonkay
 */
public interface ContentScrapper {

    /**
     * Scrape the contents for values with criteria defined by the instance of this class.
     * @param content - String content to scrape. Can be the contents of a webpage.
     */
    void scrape(String content);

    /**
     * Return the List of Scraped Values
     * @return The list of scraped values.
     */
    List<String> getScrapedValues();
}
