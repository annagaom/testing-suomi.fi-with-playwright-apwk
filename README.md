Playwright Test Project for Java
Ryhmän jäsenet
Anna
Petra
Wilma
Kaisla
Project Overview
This project uses Playwright for testing, specifically targeting suomi.fi for searching "asuintalo" details and verifying addresses with posti.fi.

Setup Instructions
Clone the repository.
git clone https://gitlab.metropolia.fi/annagao/testing-suomi.fi-with-playwright-apwk
``'
2. Tarkista ja asennetaan dependencies:

   ```bash
   mvn dependency:resolve
   npx playwright install
Asenna tarvittavat npm-paketit:

npm install
To run the tests,:

npm test
start appllication:

npm start
