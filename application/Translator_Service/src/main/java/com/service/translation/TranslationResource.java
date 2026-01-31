package com.service.translation;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;

@Path("translate")
public class TranslationResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON) // Expecting JSON input
    @Produces(MediaType.APPLICATION_JSON) // Returning JSON output
    public Response translate(String requestBody) {
        try {
            // Parse the JSON input
            JSONObject inputJson = new JSONObject(requestBody);
            String inputText = inputJson.optString("inputText", "");

            // Log the received text
            System.out.println("\nReceived text: " + inputText + "\n");

            if (inputText.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Input text is missing\"}")
                        .build();
            }

            // Instantiate the Transl class
            Translator translator = new Translator();
            String translatedText;

            try {
                // Call the translate method of the Transl class
                translatedText = translator.translate(inputText);
            } catch (Exception e) {
                // Handle any errors during translation
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"Translation failed: " + e.getMessage() + "\"}")
                        .build();
            }

            // Create a JSON response with the translated text
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("translatedText", translatedText);

            // Return the translated text as a JSON response
            return Response.ok(jsonResponse.toString(), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            // Handle JSON parsing errors
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid JSON input: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
