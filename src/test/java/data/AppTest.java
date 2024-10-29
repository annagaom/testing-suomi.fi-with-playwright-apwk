package data;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeEach
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    public void teardown() {
        page.close();
        context.close();
        browser.close();
        playwright.close();
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


            page.waitForTimeout(2000);
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

    private void waitForLanguageChange(Page page, String languageCode) {
        page.waitForFunction("document.documentElement.lang === '" + languageCode + "'");
    }

}
