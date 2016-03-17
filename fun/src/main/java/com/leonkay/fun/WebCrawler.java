package com.leonkay.fun;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Created by leonkay on 3/14/16.
 */
public class WebCrawler {

    private static final Log LOG = LogFactory.getLog(WebCrawler.class);

    private static final Pattern supportedProtocols = Pattern.compile("^[hH][tT][tT][pP][sS]?://.*");

    /**
     * Used to keep track of all accessed URLs by the application, to prevent
     * infinite looping, accessing the same website over and over again.
     */
    private Set<String> knownUrls = new TreeSet<String>();

    /**
     * A List of the Found Email Addresses in the order they were found.
     */
    private Set<String> emailAddresses = new LinkedHashSet<String>();

    /**
     * Constructor
     */
    public WebCrawler() {}

    /**
     * Visit a URL, searching for email addresses, and  navigates to deeper URLs referenced by the
     * original URL.
     * @param url -
     */
    public void visit(String url) {
        LOG.debug("Navigating to the URL : " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            String toVisit = supportedProtocols.matcher(url).matches() ? url : "http://" + url;

            if (!knownUrls.contains(toVisit)) {
                addUrl(toVisit);
                PageFetcher fetcher = PageFetcher.fetch(httpClient, toVisit);
            }
        }
        catch (IOException ex) {
            LOG.error("An Error Occurred scrubbing URL " + url, ex);
        }
        finally {
            try {
                httpClient.close();
            }
            catch (IOException ex) {
                LOG.error("An Error Occurred Closing the HTTP Client", ex);
            }
        }
    }

    /**
     * Adds a URL to the list of URL's navigated to by this URL Searcher
     * @param url -
     */
    protected void addUrl(String url) {
        if (url != null && !knownUrls.contains(url)) {
            // Always consider URLs to be Case Sensitive
            knownUrls.add(url);
        }
    }

}
