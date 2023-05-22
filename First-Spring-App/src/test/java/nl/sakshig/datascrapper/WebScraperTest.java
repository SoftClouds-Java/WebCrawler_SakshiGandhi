package nl.sakshig.datascrapper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class WebScraperTest {

    @Test
    public void testMethod(){


        WebScraper webScraper = new WebScraper();

        webScraper.scrapper();

    }

}