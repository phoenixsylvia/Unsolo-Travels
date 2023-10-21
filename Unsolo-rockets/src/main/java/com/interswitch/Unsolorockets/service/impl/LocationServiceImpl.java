package com.interswitch.Unsolorockets.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.interswitch.Unsolorockets.dtos.responses.LiveLocationDto;
import com.interswitch.Unsolorockets.exceptions.UserNotFoundException;
import com.interswitch.Unsolorockets.models.User;
import com.interswitch.Unsolorockets.respository.TravellerRepository;
import com.interswitch.Unsolorockets.service.LocationService;
import com.interswitch.Unsolorockets.utils.CustomUser;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import static com.interswitch.Unsolorockets.utils.UserUtil.getLoggedInUser;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final EmailMailServiceImpl mailService;
    private final TravellerRepository travellerRepository;
    @Value("${spring.google.maps.api}")
    private String googleMapURL;
    @Value("${spring.google.maps.api.key}")
    private String apiKey;
    @Value("${spring.google.maps.api.geocode}")
    private String mapApiGeocode;

    @Override
    public String extractTravellerCoordinates() {
        String jsonResponse = null;

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // Set the URL of the Geolocation API
            String apiUrl = googleMapURL + apiKey;

            // Create an HttpPost request
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.setHeader("Content-Type", "application/json");

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            // Check if the response is successful (HTTP status code 200)
            if (response.getStatusLine().getStatusCode() == 200) {
                jsonResponse = EntityUtils.toString(responseEntity);

            } else {
                System.err.println("Failed to retrieve geolocation data. Status code: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return jsonResponse;
    }

    @Override
    public LiveLocationDto extractCityAndState() {
        String latLng = extractTravellerCoordinates();
        LiveLocationDto liveLocation = new LiveLocationDto();

        if (latLng != null) {
            try {
                // Create an instance of ObjectMapper (Jackson library)
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse the JSON string into a JsonNode
                JsonNode jsonNode = objectMapper.readTree(latLng);

                // Extract the "location" object
                JsonNode locationNode = jsonNode.get("location");

                // Extract the "lat" and "lng" values from the "location" object
                double latitude = locationNode.get("lat").asDouble();
                double longitude = locationNode.get("lng").asDouble();

                String mapApiUrl = mapApiGeocode + latitude + "," + longitude + "&key=" + apiKey;

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(mapApiUrl);
                httpPost.setHeader("Content-Type", "application/json");

                HttpResponse response = httpClient.execute(httpPost);

                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    String jsonResponse = EntityUtils.toString(responseEntity);
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    JSONArray resultsArray = jsonObject.getJSONArray("results");

                    String city = null;
                    String state = null;

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject result = resultsArray.getJSONObject(i);
                        JSONArray addressComponents = result.getJSONArray("address_components");

                        for (int j = 0; j < addressComponents.length(); j++) {
                            JSONObject component = addressComponents.getJSONObject(j);
                            JSONArray types = component.getJSONArray("types");

                            if (types.toString().contains("neighborhood")) {
                                city = component.getString("long_name");
                            }

                            if (types.toString().contains("locality")) {
                                state = component.getString("long_name");
                            }
                        }
                    }

                    liveLocation.setCity(city);
                    liveLocation.setState(state);
                } else {
                    throw new RuntimeException("Failed to retrieve geolocation data.");
                }
            } catch (Exception e) {
                e.getMessage();
                throw new RuntimeException("An error occurred while retrieving geolocation data: {}", e);
            }
        } else {
            throw new RuntimeException("Failed to retrieve geolocation data. Coordinates are null.");
        }
        return liveLocation;
    }


    @Override
    public void emergencyAlert() throws UserNotFoundException {

        CustomUser userDetails = getLoggedInUser();
        String userDetailsUserName = userDetails.getUsername();

        User user = travellerRepository.findByEmail(userDetailsUserName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String firstname = user.getFirstName();
        String lastname = user.getLastName();

        String jsonResponse = extractTravellerCoordinates();

        if (jsonResponse != null) {
            try {
                // Parse the JSON response
                JSONObject json = new JSONObject(jsonResponse);

                // Create an instance of ObjectMapper (Jackson library)
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse the JSON string into a JsonNode
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                // Extract the "location" object
                JsonNode locationNode = jsonNode.get("location");

                // Extract the "lat" and "lng" values from the "location" object
                double latitude = locationNode.get("lat").asDouble();
                double longitude = locationNode.get("lng").asDouble();

                String receiverEmail = "unsolorockets@gmail.com";
                String subject = "Emergency Alert";
                String emailBody = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Emergency Alert</title>\n" +
                        "</head>\n" +
                        "<body style=\"font-family: Arial, sans-serif; background-color: #f0f0f0; margin: 0; padding: 0;\">\n" +
                        "\n" +
                        "<div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\">\n" +
                        "\n" +
                        "    <h2 style=\"color: #ff3300; margin-top: 0;\">Emergency Alert</h2>\n" +
                        "\n" +
                        "    <p style=\"font-size: 16px; color: #666666;\">\n" +
                        "        I am " + firstname + " " + lastname + ". Help! I am in danger.\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p style=\"font-size: 16px; color: #666666;\">\n" +
                        "        Please click the link below to view my current location on the map and come to my aid as soon as possible:\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p style=\"text-align: center;\">\n" +
                        "        <a href=\"https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude + "\"\n" +
                        "           style=\"display: inline-block; padding: 10px 20px; background-color: #007bff; color: #ffffff; text-decoration: none; border-radius: 5px;\">\n" +
                        "            View Location on Map\n" +
                        "        </a>\n" +
                        "    </p>\n" +
                        "\n" +
                        "    <p style=\"font-size: 16px; color: #666666;\">\n" +
                        "        Your immediate assistance will be greatly appreciated.\n" +
                        "    </p>\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";

                mailService.sendMail(receiverEmail, subject, emailBody, "text/html");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
