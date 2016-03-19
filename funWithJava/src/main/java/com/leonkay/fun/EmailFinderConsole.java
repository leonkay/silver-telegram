package com.leonkay.fun;

import com.leonkay.fun.web.crawler.CrawlRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executable that accepts a List of urls as arguments that it will crawl, searching
 * for values.
 * @author leonkay
 */
public class EmailFinderConsole {

    /**
     * Main Executable that takes in a URL as its first argument. The program will navigate
     * to the url and any urls linked to by the application, searching for email addresses.
     * @param args - the arguments to this command line application. Should only be a URL string.
     */
    public static void main(String ... args) {

        Logger logger = LoggerFactory.getLogger(EmailFinderConsole.class);

        if (args != null && args.length > 0) {
            CrawlRunner runner = new CrawlRunner();
            for (String url : args) {
                runner.crawl(url);
            }
            System.exit(0);
        }
        else {
            logger.error("ERROR: Please provide a URL as an argument to this application");
            System.exit(-1);
        }
    }
}
