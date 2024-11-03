# Playwright Test Project for Java

## Ryhmän jäsenet
- Anna Gao-Mäkinen
- Petra
- Wilma
- Kaisla

## Project Overview
This project uses Playwright for testing, specifically targeting `suomi.fi` for searching "asuintalo" details and verifying addresses with `posti.fi`.

### Setup Instructions
1. Clone the repository.
```
git clone https://gitlab.metropolia.fi/annagao/testing-suomi.fi-with-playwright-apwk
``'
2. Tarkista ja asennetaan dependencies:

   ```bash
   mvn dependency:resolve
   npx playwright install
   ```
3. Asenna tarvittavat npm-paketit:

   ```bash
   npm install
   ```
4. To run the tests,:
   ```bash
   npm test
   ```
5. start appllication:

   ```bash
   npm start
   ```

