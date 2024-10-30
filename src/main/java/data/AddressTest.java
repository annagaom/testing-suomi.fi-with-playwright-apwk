package data;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;
import java.util.List;

public class AddressTest {
    public void runTests() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            // Open Suomi.fi
            page.navigate("https://www.suomi.fi/");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Locate all expandable elements on the page (e.g., accordions, sections, etc.)
            List<Locator> expanders = page.locator(".expander-element").all();  // Adjust selector if necessary
            System.out.println("Found " + expanders.size() + " expandable elements.");

            // Loop to expand, capture screenshot, and collapse each Expander element
            int index = 1;
            for (Locator expander : expanders) {
                // Expand the Expander
                expander.click();
                page.waitForTimeout(1000); // Wait briefly to allow content to load

                // Screenshot after expanding
                expander.screenshot(new Locator.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/expander_opened_" + index + ".png")));
                System.out.println("Screenshot saved: expander_opened_" + index + ".png");

                // Collapse the Expander
                expander.click();
                page.waitForTimeout(1000); // Wait briefly to ensure collapse is complete

                // Screenshot after collapsing
                expander.screenshot(new Locator.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/expander_closed_" + index + ".png")));
                System.out.println("Screenshot saved: expander_closed_" + index + ".png");


                index++;
            }
            browser.close(); // Close the browser
        }
    }
}
