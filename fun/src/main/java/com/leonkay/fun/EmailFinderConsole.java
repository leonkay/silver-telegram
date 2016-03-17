package com.leonkay.fun;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.URL;

/**
 * Created by leonkay on 3/14/16.
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

            String cur = System.getProperty("user.dir");
            URL re = EmailFinderConsole.class.getClassLoader().getResource("/logback.xml");
            logger.debug("Test");
            for (String url : args) {
                WebCrawler finder = new WebCrawler();
                finder.visit(url);
            }
        }
        else {
            System.out.println("ERROR: Please provide a URL as an argument to this application");
        }
    }
}
