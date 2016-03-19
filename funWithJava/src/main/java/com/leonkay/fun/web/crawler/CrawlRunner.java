package com.leonkay.fun.web.crawler;

import com.leonkay.fun.web.content.ContentScrapper;
import com.leonkay.fun.web.content.EmailScrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility that manages a list of supported {@link ContentScrapper}, verifying a URL
 * is valid to attempt to visit.
 * @author leonkay
 */
public class CrawlRunner {

    private static final Log LOG = LogFactory.getLog(CrawlRunner.class);

    /**
     * Regex to verify that url starts with either http:// or https://, case insensitive,
     * and has an authority in the url e.g. the "google" in http://google.com
     */
    private static final Pattern PROTOCOLS = Pattern.compile("^[hH][tT][tT][pP][sS]?://.+");

    /**
     * The Scrappers that will parse the web page content while crawling and retrieve
     * values that meet the content scrappers criteria.
     */
    private List<ContentScrapper> supportedScrappers;

    /**
     * Constructor that defaults the supported scrappers to be the
     * {@link EmailScrapper}.
     */
    public CrawlRunner() {
        this(Arrays.asList(new EmailScrapper()));
    }

    /**
     * Constructor that initializes the list of supported scrappers.
     */
    public CrawlRunner(final List<ContentScrapper> scrappers) {
        this.supportedScrappers = scrappers;
    }

    /**
     * Attempts to Crawl a URL. If the url does not contain a protocol i.e. http:// or https://
     * then it will prepend "http://" to the url before attempting to crawl it.
     * @param url - The URL to Crawl
     */
    public boolean crawl(String url) {
        LOG.info("Navigating to the URL : " + url);

        try {
            String toVisit = PROTOCOLS.matcher(url).matches() ? url : "http://" + url;

            if (PROTOCOLS.matcher(toVisit).matches()) {
                LOG.debug("Scrapping " + toVisit);

                new PageCrawler(supportedScrappers).fetch(toVisit);
                return true;
            }
            else {
                LOG.debug("Specified URL only contains a protocol, and is not a valid url");
                return false;
            }
        }
        catch (IOException ex) {
            LOG.error("An Error Occurred scrubbing URL " + url, ex);
            return false;
        }
    }
}
