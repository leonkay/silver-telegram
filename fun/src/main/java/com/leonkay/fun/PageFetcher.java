package com.leonkay.fun;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;

import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.state.StateVertex;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leonkay on 3/15/16.
 *
 * Utility class used to fetch the contents of a URL
 */
public class PageFetcher {

    // REGEX leveraged from emailregex.com
    private static final Pattern EMAIL_CRAWLER = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    private String pageContent;

    private PageFetcher() {}

    public static PageFetcher fetch(HttpClient client, final String url) throws IOException {

        PageFetcher rtn = new PageFetcher();

        CrawljaxConfiguration.CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
        builder.addPlugin(new OnNewStatePlugin() {
            @Override
            public void onNewState(CrawlerContext crawlerContext, StateVertex stateVertex) {
                System.out.println("---------------------------------");

                try {
                    System.out.println(stateVertex.getUrl());
                    String content = crawlerContext.getCurrentState() != null ? crawlerContext.getCurrentState().getDom() : "";

                    Matcher matcher = EMAIL_CRAWLER.matcher(content);
                    while(matcher.find()) {
                        System.out.println(matcher.group());
                    }

                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.println("---------------------------------");

            }
        });
        builder.crawlRules().click("span");
        builder.crawlRules().click("div");
        builder.crawlRules().waitAfterEvent(500, TimeUnit.MILLISECONDS);
        builder.crawlRules().waitAfterReloadUrl(500, TimeUnit.MILLISECONDS);
        builder.crawlRules().dontClick("a").withText("Twitter").withText("Facebook").withText("LinkedIn");
        builder.setBrowserConfig(new BrowserConfiguration(EmbeddedBrowser.BrowserType.PHANTOMJS, 1));


        CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
        crawljax.call();

        return rtn;
    }
}
