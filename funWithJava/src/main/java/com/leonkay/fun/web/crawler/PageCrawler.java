package com.leonkay.fun.web.crawler;

import com.crawljax.core.CrawlerContext;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;

import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.state.StateVertex;
import com.leonkay.fun.web.content.ContentScrapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Utility that executes the business logic of crawling a page, and applies the fetched content against
 * the list of scrappers.
 * @author leonkay
 */
public class PageCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(PageCrawler.class);

    /**
     * The list of supported scrappers to execute against the content
     */
    private List<ContentScrapper> scrapperList;

    /**
     * A list of known urls by this crawler, to avoid scrapping content multiple times.
     */
    private Set<String> knownUrls;

    /**
     * Constructo that will initialize the ContentScrapper list.
     * @param scrapperList -
     */
    public PageCrawler(List<ContentScrapper> scrapperList) {
        this.scrapperList = scrapperList;
        knownUrls = new HashSet<>();
    }

    /**
     * Executes the initial URL crawl request. This will handle the business logic
     * of crawling a webpage, scraping for content.
     * @param url to crawl
     * @return this instance of the PageCrawler
     * @throws IOException -
     */
    public PageCrawler fetch(final String url) throws IOException {

        final List<ContentScrapper> scrappers = getScrapperList();

        // leverage java 1.8 lambdas
        final OnNewStatePlugin scraperLambda = (CrawlerContext crawlerContext, StateVertex stateVertex) -> {
            String visitedUrl = stateVertex.getUrl();
            try {
                if (knownUrls.contains(visitedUrl)) {
                    LOG.debug("Already visited URL " + visitedUrl + ", don't need to scrape");
                }
                else {
                    LOG.debug("Navigated to URL " + visitedUrl);
                    knownUrls.add(visitedUrl);
                    String content = crawlerContext.getCurrentState() != null ? crawlerContext.getCurrentState().getDom() : "";

                    for (ContentScrapper scrapper : scrappers) {
                        scrapper.scrape(content);
                    }
                }
            }
            catch (Exception ex) {
                LOG.error("An error occurred visiting URL: " + visitedUrl, ex);
            }
        };

        // By Default, the CrawlJax library uses Firefox to execute its tests. This is more useful
        // than using the headless browser PhantomJS, which appears to fail more on bad json than firefox.
        CrawljaxConfiguration.CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
        builder.addPlugin(scraperLambda); // add the scraping plugin


        // wait a little bit after reloading to help avoid hammering target urls
        // and allow page initialization
        builder.crawlRules().waitAfterEvent(500, TimeUnit.MILLISECONDS);
        builder.crawlRules().waitAfterReloadUrl(500, TimeUnit.MILLISECONDS);

        CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
        crawljax.call();
        return this;
    }

    /**
     * @return the list of ContentScrappers
     */
    public List<ContentScrapper> getScrapperList() {
        return scrapperList;
    }

    /**
     * @return the list of URLs known(visited) by this Crawler
     */
    public Set<String> getKnownUrls() {
        return knownUrls;
    }
}
