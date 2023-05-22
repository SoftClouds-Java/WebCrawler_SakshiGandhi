package nl.sakshig.RestTemplateAPI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class RestClientAPI {

    public String httpRestTemplate(String link) {

        String url = "https://event.salesforce.com/api/speaker";
        String requestBody = "id=" + link;

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);

        String responseBody = null;
        try {
            // Set headers
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setHeader("Rfapiprofileid", "YbdLGjZLTW3DgJy4pvD1Mzr6qTBVa9KY");


            // Set the request body
            request.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));

            // Execute the request
            HttpResponse response = httpClient.execute(request);

            // Get the response body as a string
            responseBody = EntityUtils.toString(response.getEntity());

            // Handle the response
            System.out.println("Response status code: " + response.getStatusLine().getStatusCode());
            System.out.println("Response body: " + responseBody);

            // Remember to release resources when done
            EntityUtils.consume(response.getEntity());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseJson(responseBody);

    }

    public String parseJson(String responseBody) {
        Gson gson = new Gson();
        JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

        // Get the 'items' array
        JsonArray itemsArray = responseJson.getAsJsonArray("items");
        JsonObject linkedinValue = itemsArray.get(0).getAsJsonObject();

        if (linkedinValue.get("linkedIn") != null) {

            return linkedinValue.get("linkedIn").getAsString();
        } else return "";
    }
}
