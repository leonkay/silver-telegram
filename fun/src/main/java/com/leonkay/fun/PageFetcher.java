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

/**
 * Created by leonkay on 3/15/16.
 *
 * Utility class used to fetch the contents of a URL
 */
public class PageFetcher {

    private static final ResponseHandler<String> BASIC_HANDLER = new BasicResponseHandler();

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

                    //System.out.println(content);
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
