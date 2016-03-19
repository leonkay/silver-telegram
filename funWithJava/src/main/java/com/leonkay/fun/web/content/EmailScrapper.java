package com.leonkay.fun.web.content;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link ContentScrapper} that will scrape the content looking for
 * strings that match the RFC 5322 standard format for email addresses.
 * @author leonkay
 */
public class EmailScrapper implements ContentScrapper {

    /**
     * Regex from <a href="http://emailregex.com">EmailRegex.com</a> that represents the
     * <a href="https://www.ietf.org/rfc/rfc5322.txt">RFC 5322 Official Standard</a> that defines
     * the standard format for email addresses.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    /**
     * Logger for Class to support debugging.
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmailScrapper.class);

    /**
     * Logger for Reporting scraped values.
     */
    private static final Logger REPORT = LoggerFactory.getLogger("reporting");

    /**
     * A Collection of email addresses that have been found, in the order they were found.
     */
    private Set<String> emailAddresses = new LinkedHashSet<>();


    /**
     * Scrape the content for unique email addresses.
     * @param content - The Content being scraped for email addresses
     */
    @Override
    public void scrape(String content) {
        if (StringUtils.isBlank(content)) {
            LOG.info("No Content to Scrape");
        }
        else {
            LOG.info("Scraping Content ...");
            if (LOG.isDebugEnabled()) {
                LOG.debug("-- Content : " + content);
            }

            final Matcher emailMatcher = EMAIL_PATTERN.matcher(content);
            while (emailMatcher.find()) {
                final String foundValue = emailMatcher.group();
                LOG.info("Found Email Address : " + foundValue);

                // since we are looking for unique raw strings, we don't normalize
                // the email address e.g. by transforming it to lowercase. However
                // in the future if looking for unique email addresses, that can be done here

                if (emailAddresses.contains(foundValue)) {
                    LOG.info("-- Email Address is not Unique, ignoring - " + foundValue);
                }
                else {
                    LOG.info("-- Email Address has not been found, reporting - " + foundValue);
                    emailAddresses.add(foundValue);
                    REPORT.info(foundValue);
                }
            }
        }
    }

    /**
     * Returns an immutable List of Scraped values. The values should be unique, and returned in the order found.
     * @return -
     */
    @Override
    public List<String> getScrapedValues() {
        final List<String> copy = new ArrayList<>(this.emailAddresses.size());
        copy.addAll(this.emailAddresses);
        return Collections.unmodifiableList(copy);
    }
}
