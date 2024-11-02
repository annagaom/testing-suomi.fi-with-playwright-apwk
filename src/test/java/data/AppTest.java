package data;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeEach
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @AfterEach
    public void teardown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    public void testVisualRegressionOnNavItems() {
        // Navigate to suomi.fi
        page.navigate("https://www.suomi.fi");

        // Select language menu
        Locator languageMenuButton = page.locator(".fi-language-menu");
        languageMenuButton.click();

        // Get language options
        Locator languageOptions = page.locator("button.fi-language-menu-item");
        List<Locator> languages = languageOptions.all();

        // Loop through all languages and take screenshots
        for (Locator language : languages) {
            language.click();
            String languageCode = language.getAttribute("lang");
            page.waitForTimeout(2000); // Adjust if necessary
            waitForLanguageChange(page, languageCode);

            Locator navItems = page.locator("nav.role-selection");
            List<Locator> items = navItems.all();
            for (Locator item : items) {
                item.hover(); // Hover to capture any visual effects or dropdowns
                String itemName = item.textContent().replaceAll("\\s+", "_");
                item.screenshot(new Locator.ScreenshotOptions()
                        .setPath(Paths.get("screenshots/" + languageCode + "_" + itemName + ".png")));

                assertTrue(Paths.get("screenshots/" + languageCode + "_" + itemName + ".png").toFile().exists(),
                        "Screenshot not created for " + itemName + " in " + languageCode);
            }
            languageMenuButton.click();
        }

        System.out.println("Visual regression test completed for all languages.");
    }

    @Test
    public void testAddressRetrievalAndValidation() {
        // Navigate to Suomi.fi and perform search
        page.navigate("https://www.suomi.fi/");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Search for "asuintalo" on Suomi.fi
        page.locator("#search-box").fill("asuintalo");
        page.locator("#id-main-search-button").click();

        // Wait for search results and click on the first result
        page.waitForSelector("#id-main-search-search-results", new Page.WaitForSelectorOptions().setTimeout(20000));
        page.locator("#id-main-search-search-results > li:nth-child(3) > h2 > a").click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // Capture and print the address
        String address = "";
        page.waitForSelector("div.ptv-highlight-box > div > div:nth-child(1) > p", new Page.WaitForSelectorOptions().setTimeout(20000));
        address = page.locator("div.ptv-highlight-box > div > div:nth-child(1) > p").innerText();
        System.out.println("Extracted Address: " + address);


        // Screenshot for the address section
        page.locator("div.ptv-highlight-box > div > div:nth-child(1)").screenshot(new Locator.ScreenshotOptions()
                .setPath(Paths.get("screenshots/suomi_address_section.png")));
        assertTrue(Paths.get("screenshots/suomi_address_section.png").toFile().exists(), "Address screenshot not created.");

        // Open Posti.fi in a new context to validate the address
        BrowserContext context = browser.newContext();
        Page postiPage = context.newPage();
        postiPage.navigate("https://www.posti.fi/fi/postinumerohaku");
        postiPage.waitForLoadState(LoadState.NETWORKIDLE); // Ensure the page has loaded completely

        // Fill the address in the search field
        postiPage.locator("#zipcode-search-bar").fill(address); // Adjust the selector
        postiPage.locator("#zipcode-search > div > div.searchWrapper-0-1-6 > div > div > div > div > div > div.sc-1ufh44s-7.kKaxEK > button").click(); // Adjust the selector
        postiPage.waitForLoadState(LoadState.NETWORKIDLE); // Wait for results

        // Validate the address appears in the search results
        String result = postiPage.locator("#searchResultContainer > div > div > div > table > tbody > tr").innerText(); // Adjust the selector
        System.out.println("Address validation result: " + result);

        //screen shot for the address validation
        postiPage.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/posti_address_validation.png")));

        // Check if the result is not empty
        if (!result.isEmpty()) {
            System.out.println("Address validation successful.");
        } else {
            System.out.println("Address validation failed.");
        }

        context.close();
        browser.close();
    }

    @Test
    public void testExpanderElements() {
        // Open Suomi.fi specific support page
        page.navigate("https://www.suomi.fi/ohjeet-ja-tuki/tuki-ja-neuvonta/yritys-suomi-puhelinpalvelu");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // First expander
        page.locator("#\\30  > div.sc-9fbwu8-0.cpdacf.sc-1ulzrr5-0.GDXEX.fi-expander_title-button").click();
        page.waitForFunction("document.querySelector('#\\\\30_content').innerText.trim().length > 0");

        // Verify only first expander content is visible
        String ekaResult = page.locator("#\\30_content").innerText();
        String tokaResult = page.locator("#\\31_content").innerText();
        String kolmasResult = page.locator("#\\32_content").innerText();

        assertTrue(!ekaResult.isEmpty(), "First expander content is visible.");
        assertTrue(tokaResult.isEmpty(), "Second expander content should not be visible.");
        assertTrue(kolmasResult.isEmpty(), "Third expander content should not be visible.");
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/first_expander_open.png")));
        System.out.println("Screenshot saved: first_expander_open.png");

        // Close the first expander
        page.locator("#\\30  > div.sc-9fbwu8-0.cpdacf.sc-1ulzrr5-0.GDXEX.fi-expander_title-button").click();
        page.waitForFunction("document.querySelector('#\\\\30_content').innerText.trim().length == 0");

        // Open second expander
        page.locator("#\\31  > div.sc-9fbwu8-0.cpdacf.sc-1ulzrr5-0.GDXEX.fi-expander_title-button").click();
        page.waitForFunction("document.querySelector('#\\\\31_content').innerText.trim().length > 0");

        // Verify only second expander content is visible
        ekaResult = page.locator("#\\30_content").innerText();
        tokaResult = page.locator("#\\31_content").innerText();
        kolmasResult = page.locator("#\\32_content").innerText();

        assertTrue(ekaResult.isEmpty(), "First expander content should not be visible.");
        assertTrue(!tokaResult.isEmpty(), "Second expander content is visible.");
        assertTrue(kolmasResult.isEmpty(), "Third expander content should not be visible.");
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/second_expander_open.png")));
        System.out.println("Screenshot saved: second_expander_open.png");

        // Close the second expander
        page.locator("#\\31  > div.sc-9fbwu8-0.cpdacf.sc-1ulzrr5-0.GDXEX.fi-expander_title-button").click();
        page.waitForFunction("document.querySelector('#\\\\31_content').innerText.trim().length == 0");

        // Open third expander
        page.locator("#\\32  > div.sc-9fbwu8-0.cpdacf.sc-1ulzrr5-0.GDXEX.fi-expander_title-button").click();
        page.waitForFunction("document.querySelector('#\\\\32_content').innerText.trim().length > 0");

        // Verify only third expander content is visible
        ekaResult = page.locator("#\\30_content").innerText();
        tokaResult = page.locator("#\\31_content").innerText();
        kolmasResult = page.locator("#\\32_content").innerText();

        assertTrue(ekaResult.isEmpty(), "First expander content should not be visible.");
        assertTrue(tokaResult.isEmpty(), "Second expander content should not be visible.");
        assertTrue(!kolmasResult.isEmpty(), "Third expander content is visible.");

        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/third_expander_open.png")));
        System.out.println("Screenshot saved: third_expander_open.png");

        // Close the third expander
        page.locator("#\\32  > div.sc-9fbwu8-0.cpdacf.sc-1ulzrr5-0.GDXEX.fi-expander_title-button").click();
        page.waitForFunction("document.querySelector('#\\\\32_content').innerText.trim().length == 0");

        // Expand all sections
        page.locator("#main > div:nth-child(2) > div > div > div.col-12.col-lg-8 > div > div.instruction-content-area > div:nth-child(3) > div > button").click();
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Retrieve text from expanded sections
         ekaResult = page.locator("#\\30 _content > div > div > p").innerText();
         tokaResult = page.locator("#\\31 _content > div > div > p:nth-child(1)").innerText();
         kolmasResult = page.locator("#\\32 _content > div > div > p:nth-child(1)").innerText();

        // Check results of expansion
        assertTrue(!ekaResult.isEmpty() && !tokaResult.isEmpty() && !kolmasResult.isEmpty(),
                "All expander elements opened successfully.");
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/all_expanders_open.png")));
        System.out.println("Screenshot saved: all_expanders_open.png");

        // Collapse all sections
        page.locator("#main > div:nth-child(2) > div > div > div.col-12.col-lg-8 > div > div.instruction-content-area > div:nth-child(3) > div > button").click();
        ekaResult = page.locator("#\\30 _content > div > div > p").innerText();
        tokaResult = page.locator("#\\31 _content > div > div > p:nth-child(1)").innerText();
        kolmasResult = page.locator("#\\32 _content > div > div > p:nth-child(1)").innerText();

        // Check results of collapse
        assertTrue(ekaResult.isEmpty() && tokaResult.isEmpty() && kolmasResult.isEmpty(),
                "All expander elements closed successfully.");
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/all_expanders_closed.png")));
        System.out.println("Screenshot saved: all_expanders_closed.png");
    }

    private void waitForLanguageChange(Page page, String languageCode) {
        page.waitForFunction("document.documentElement.lang === '" + languageCode + "'");
    }
}




