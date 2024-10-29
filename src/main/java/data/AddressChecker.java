package data;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

public class AddressChecker {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)); // Set to false for debugging
            Page page = browser.newPage();

            // Open Suomi.fi
            page.navigate("https://www.suomi.fi/");
            page.waitForLoadState(LoadState.NETWORKIDLE); // Ensure the page has loaded completely

            // Search for "asuintalo"
            page.locator("#search-box").fill("asuintalo"); // Adjust the selector
            page.locator("#id-main-search-button").click(); // Adjust the selector

            // Wait for the search results to load
            page.waitForSelector("#id-main-search-search-results", new Page.WaitForSelectorOptions().setTimeout(60000));

            // Click the first search result
            page.locator("#id-main-search-search-results > li:nth-child(1) > h2 > a").click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED); // Wait for the new page to load

            // Capture the address of the first "asuintalo" result
            String address = "";
            try {
                // Wait for the specific element to be visible
                page.waitForSelector("div.ptv-highlight-box > div > div:nth-child(1) > p", new Page.WaitForSelectorOptions().setTimeout(60000));

                // Capture the address text
                address = page.locator("div.ptv-highlight-box > div > div:nth-child(1) > p").innerText();
                System.out.println("Extracted Address: " + address);
            } catch (TimeoutError e) {
                System.out.println("Could not find the address element within the timeout.");
            }

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

            // Check if the result is not empty
            if (!result.isEmpty()) {
                System.out.println("Address validation successful.");
            } else {
                System.out.println("Address validation failed.");
            }

            context.close(); // Close the context for the second page
            browser.close(); // Close the browser
        }
    }
}
