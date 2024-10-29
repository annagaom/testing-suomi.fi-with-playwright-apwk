package data;


import com.microsoft.playwright.*;
import java.nio.file.Paths;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(true));

            App app = new App();

        }
    }

}