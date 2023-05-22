package nl.sakshig.RestTemplateAPI;

import junit.framework.TestCase;
import org.junit.Test;

public class RestClientAPITest extends TestCase {


    @Test
    public void testRestClient() {
        RestClientAPI restClientAPI = new RestClientAPI();
        String link = restClientAPI.httpRestTemplate("1660077294404001gXdi_167407930480000cnx23");
        System.out.println(link);
    }
}