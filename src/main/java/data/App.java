package data;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch();
            Page page = browser.newPage();
            page.navigate("https://playwright.dev/");
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
        }

        AddressTest addressTest = new AddressTest();
        addressTest.runTests();

        ExpanderElementsTest expanderElementsTest = new ExpanderElementsTest();
        expanderElementsTest.runTests();
    }

}
