package com.leonkay.fun.web.content;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

/**
 * Unit Test for Email Scrapper
 * @author leonkay
 * @date 3/19/16
 */
public class EmailScrapperTest {

    /**
     * Unit Test that parses content for two email addresses in the order they appear on the page.
     * @throws Exception - if there was an error getting the content to test
     */
    @Test
    public void emailTest() throws Exception {

        InputStream content = this.getClass().getResourceAsStream("EmailScrapperTest.html");

        String theString = IOUtils.toString(content, "UTF-8");

        Assert.assertNotNull(theString);

        EmailScrapper scrapper = new EmailScrapper();
        scrapper.scrape(theString);

        Assert.assertThat(scrapper.getScrapedValues(), IsNull.notNullValue());
        Assert.assertThat(scrapper.getScrapedValues().size(), IsEqual.equalTo(2));
        Assert.assertThat(scrapper.getScrapedValues().get(0), IsEqual.equalTo("lorem@ipsum.com"));
        Assert.assertThat(scrapper.getScrapedValues().get(1), IsEqual.equalTo("jupitor@mars.com"));
    }

    /**
     * Unit test that confirms that getScrapedValues() returns an immutable list
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testImmutable() {
        EmailScrapper scrapper = new EmailScrapper();
        scrapper.getScrapedValues().add("Test");
    }
}
