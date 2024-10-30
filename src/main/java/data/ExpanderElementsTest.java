package data;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;

public class ExpanderElementsTest {

    public void runTests() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)); // Set to false for debugging
            Page page = browser.newPage();

            String ekaResult = "";
            String tokaResult = "";
            String kolmasResult = "";

            // Open Suomi.fi
            page.navigate("https://www.suomi.fi/ohjeet-ja-tuki/tuki-ja-neuvonta/yritys-suomi-puhelinpalvelu");
            page.waitForLoadState(LoadState.NETWORKIDLE); // Ensure the page has loaded completely

            // Click "Open All" button to expand all sections
            Locator openAllButton = page.locator("#main > div:nth-child(2) > div > div > div.col-12.col-lg-8 > div > div.instruction-content-area > div:nth-child(3) > div > button");
            openAllButton.click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Capture text content of each expanded section
            ekaResult = page.locator("#\\30 _content > div > div > p").innerText();
            tokaResult = page.locator("#\\31 _content > div > div > p:nth-child(1)").innerText();
            kolmasResult = page.locator("#\\32 _content > div > div > p:nth-child(1)").innerText();

            // Check if all Expanders are opened successfully and take screenshot
            if (!ekaResult.isEmpty() && !tokaResult.isEmpty() && !kolmasResult.isEmpty()) {
                System.out.println("All Expander elements were successfully opened.");
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/all_expanders_open.png")));
                System.out.println("Screenshot saved: all_expanders_open.png");
            } else {
                System.out.println("Failed to open all Expander elements.");
            }

            // Click "Close All" button to collapse all sections
            openAllButton.click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Capture text content again to verify collapse
            ekaResult = page.locator("#\\30 _content > div > div > p").innerText();
            tokaResult = page.locator("#\\31 _content > div > div > p:nth-child(1)").innerText();
            kolmasResult = page.locator("#\\32 _content > div > div > p:nth-child(1)").innerText();

            // Check if all Expanders are closed successfully and take screenshot
            if (ekaResult.isEmpty() && tokaResult.isEmpty() && kolmasResult.isEmpty()) {
                System.out.println("All Expander elements were successfully closed.");
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/all_expanders_closed.png")));
                System.out.println("Screenshot saved: all_expanders_closed.png");
            } else {
                System.out.println("Failed to close all Expander elements.");
            }

            // Close the browser
            browser.close();
        }
    }
}
