
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Leon.Kay on 3/22/16.
 * <p>
 * Unit Test to check if FireFox is installed
 */
public class FirefoxInstallTest {

    @Test
    public void testFirefoxInstalled() {
        try {
            String url = "http://localhost";
            CrawljaxConfiguration.CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
            CrawljaxRunner crawljax = new CrawljaxRunner(builder.build());
            crawljax.call();
        } catch (Exception ex) {
            Assert.assertThat(ex.getLocalizedMessage(), IsNot.not(new StringContains("Cannot find firefox binary in PATH")));
        }
    }
}
