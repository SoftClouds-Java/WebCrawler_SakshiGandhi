package nl.sakshig.datascrapper;


import nl.sakshig.excelwriter.ExcelWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.List;

import nl.sakshig.RestTemplateAPI.RestClientAPI;


@Component
public class WebScraper {

    RestClientAPI restClientAPI;

    public void scrapper() {

        RestClientAPI restClientAPI = new RestClientAPI();


        // Set the path to the ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\saksh\\Downloads\\chromedriver_win32\\chromedriver.exe");

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        // Add any necessary options (e.g., headless)

        // Create a new instance of ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // Load the web page
            driver.get("https://reg.salesforce.com/flow/plus/cnx23/sessioncatalog/page/Catalog");
            WebDriverWait wait = new WebDriverWait(driver, 120); // Specify the maximum wait time

            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("search-results")));
            element.click();

            List<WebElement> liElements = element.findElements(By.tagName("li"));

            for (WebElement liElement : liElements) {
                String tileText = "";
                String abstractText = "";
                WebElement classCatalogElement = liElement.findElement(By.className("catalog-result-title-text"));
                tileText = classCatalogElement.getText();
                WebElement classAbstractElement = liElement.findElement(By.className("description"));
                abstractText = classAbstractElement.getText();

                WebElement element2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("arrow-toggle-collapsed")));
                element2.click();
                String attrRole = "";
                List<WebElement> attributeRole = liElement.findElements(By.className("attribute-Role"));
                if (attributeRole.size() > 0) {
                    int index = attributeRole.get(0).getText().indexOf(":");
                    attrRole = attributeRole.get(0).getText().substring(index + 1);

                }
                List<WebElement> attributeIndustry = liElement.findElements(By.className("attribute-Industry"));
                String attributeIndus = "";
                if (attributeIndustry.size() > 0) {
                    int index = attributeIndustry.get(0).getText().indexOf(":");

                    attributeIndus = attributeIndustry.get(0).getText().substring(index + 1);

                }
                String attrProduct = "";
                List<WebElement> attributeProduct = liElement.findElements(By.className("attribute-Product"));
                if (attributeProduct.size() > 0) {
                    int index = attributeProduct.get(0).getText().indexOf(":");

                    attrProduct = attributeProduct.get(0).getText().substring(index + 1);

                }
                String attributeTopic = "";
                List<WebElement> attributeTopics = liElement.findElements(By.className("attribute-Topic"));
                if (attributeTopics.size() > 0) {
                    int index = attributeTopics.get(0).getText().indexOf(":");

                    attributeTopic = attributeTopics.get(0).getText().substring(index + 1);

                }
                String attrSessionType = "";
                List<WebElement> attributeSessionType = liElement.findElements(By.className("attribute-SessionType"));
                if (attributeSessionType.size() > 0) {
                    int index = attributeSessionType.get(0).getText().indexOf(":");

                    attrSessionType = attributeSessionType.get(0).getText().substring(index + 1);

                }


                List<WebElement> speakerName = liElement.findElements(By.tagName("p"));

                if (speakerName.size() > 0) {


                    String designation = "";
                    String name = "";
                    for (int i = 0; i < speakerName.size(); i++) {


                        int firstIndex = speakerName.get(i).getText().indexOf(",");
                        int secondIndex = speakerName.get(i).getText().indexOf(",", firstIndex + 1);
                        if (secondIndex != -1) {
                            designation = speakerName.get(i).getText().substring(firstIndex + 1, secondIndex);
                        }

                        // Xpath for linkedin List
                        String linkedInLink = "";
                        name = "//button/span[text()='" + speakerName.get(i).getText().substring(0, firstIndex).trim() + "']/../../button[contains(@data-test, 'rf-button-speaker-trigger')]";

                        if (!speakerName.get(i).getText().substring(0, firstIndex).trim().contains("'")) {
                            List<WebElement> buttonElement = liElement.findElements(By.xpath(name));
                            if (buttonElement.size() > 0) {
                                int cutOffIndex = buttonElement.get(0).getAttribute("data-test").indexOf("rf-button-speaker-trigger-") + "rf-button-speaker-trigger-".length();
                                linkedInLink = restClientAPI.httpRestTemplate(buttonElement.get(0).getAttribute("data-test").substring(cutOffIndex));
                            }
                            // Adding exception for element with space in between names
                            else {
                                int spaceIndex = speakerName.get(i).getText().indexOf(" ");
                                if (spaceIndex != -1) {
                                    String postExceptionName = speakerName.get(i).getText().substring(0, spaceIndex + 1) + " " + speakerName.get(i).getText().substring(spaceIndex + 1);
                                    name = "//button/span[text()='" + postExceptionName.substring(0, firstIndex).trim() + "']/../../button[contains(@data-test, 'rf-button-speaker-trigger')]";

                                    List<WebElement> buttonElementException = liElement.findElements(By.xpath(name));
                                    if (buttonElementException.size() > 0) {
                                        int cutOffExceptionIndex = buttonElementException.get(0).getAttribute("data-test").indexOf("rf-button-speaker-trigger-") + "rf-button-speaker-trigger-".length();
                                        linkedInLink = restClientAPI.httpRestTemplate(buttonElementException.get(0).getAttribute("data-test").substring(cutOffExceptionIndex));
                                    }
                                }
                            }
                        }


                        System.out.println("Title Text: " + tileText + " Abstract Text " + abstractText + " " +
                                attrRole + " " + attributeIndus + " Name : " + speakerName.get(i).getText().substring(0, firstIndex) +
                                "  " + attrProduct + " " + attributeTopic
                                + " " + attrSessionType + " " + designation
                                + " [Company : " + speakerName.get(i).getText().substring(secondIndex + 1) + "]" + linkedInLink);

                        if (i == 0) {
                            ExcelWriter.writer(tileText, abstractText, attrRole, attributeIndus, speakerName.get(i).getText().substring(0, firstIndex)
                                    , attrProduct, attributeTopic, attrSessionType, speakerName.get(i).getText().substring(secondIndex + 1), designation, linkedInLink);
                        } else {
                            ExcelWriter.writer("", "", "", "", speakerName.get(i).getText().substring(0, firstIndex)
                                    , "", "", "", speakerName.get(i).getText().substring(secondIndex + 1), designation, linkedInLink);
                        }
                    }
                } else {
                    System.out.println("Title Text: " + tileText + " Abstract Text " + abstractText + " " +
                            attrRole + " " + attributeIndus + " Name : " +
                            "  " + attrProduct + " " + attributeTopic
                            + " " + attrSessionType
                            + " [Company : " + "]");

                    ExcelWriter.writer(tileText, abstractText, attrRole, attributeIndus, ""
                            , attrProduct, attributeTopic, attrSessionType, "", "", "");
                }
            }
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



