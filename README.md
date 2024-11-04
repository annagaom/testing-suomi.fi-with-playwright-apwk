# Playwright Test Project for Java

## Ryhmän jäsenet
- Anna 
- Petra
- Wilma
- Kaisla

## Project Overview
- Perform visual regression testing on all main navigation items (nav.role-selection), including their child elements, across all available languages.
- This project uses Playwright for testing, specifically targeting suomi.fi for searching "asuintalo" details and verifying addresses with posti.fi.
- For information pages containing expander elements, select a page with multiple expanders. Test that each expander can open and close individually, as well as using "open all" and "close all" buttons.

### Setup Instructions
1. Clone the repository.
   ```
   git clone https://gitlab.metropolia.fi/annagao/testing-suomi.fi-with-playwright-apwk
   ```
2. Tarkista ja asennetaan dependencies:
   ```bash
   mvn dependency:resolve
   ```
   ```
   npx playwright install
   ```
3. Asenna tarvittavat npm-paketit:
   ```bash
   npm install
   ```
   ```
   npx playwright install
   ```
4. To run the tests,:
   ```bash
   npm test
   ```
5. start appllication:
   ```bash
   npm start
   ```

