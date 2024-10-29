package data;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;

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

            // Avaa kaikki
            page.locator("#main > div:nth-child(2) > div > div > div.col-12.col-lg-8 > div > div.instruction-content-area > div:nth-child(3) > div > button").click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            ekaResult = page.locator("#\\30 _content > div > div > p").innerText();
            tokaResult = page.locator("#\\31 _content > div > div > p:nth-child(1)").innerText();
            kolmasResult = page.locator("#\\32 _content > div > div > p:nth-child(1)").innerText();

            // Tarkista tulokset
            if (!ekaResult.isEmpty() && !tokaResult.isEmpty() && !kolmasResult.isEmpty()) {
                System.out.println("Kaikki laajentaja elementit avattiin \"Avaa kaikki\" painikkeella..");
            } else {
                System.out.println("Kaikkien laajentaja elementtien avaaminen ei onnistunut.");
            }

            // Sulje kaikki
            page.locator("#main > div:nth-child(2) > div > div > div.col-12.col-lg-8 > div > div.instruction-content-area > div:nth-child(3) > div > button").click();
            ekaResult = page.locator("#\\30 _content > div > div > p").innerText();
            tokaResult = page.locator("#\\31 _content > div > div > p:nth-child(1)").innerText();
            kolmasResult = page.locator("#\\32 _content > div > div > p:nth-child(1)").innerText();

            // Tarkista tulokset
            if (ekaResult.isEmpty() && tokaResult.isEmpty() && kolmasResult.isEmpty()) {
                System.out.println("Kaikki laajentaja elementit suljettiin \"Sulje kaikki\" painikkeella.");
            } else {
                System.out.println("Kaikkien laajentaja elementtien sulkeminen ei onnistunut.");
            }

            // Sulje sivusto
            browser.close();
        }
    }
}
