package com.service.translation;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Content;
import com.google.genai.types.Part;

public class Translator {
    // Récupère la clé depuis les variables d'environnement
    private final String API_KEY = System.getenv("GEMINI_API_KEY");

    public String translate(String text) {
        // Vérification de sécurité pour la clé
        if (API_KEY == null || API_KEY.isEmpty()) {
            return "Erreur : La variable d'environnement GEMINI_API_KEY est manquante.";
        }
        try {
            // Initialisation du client
            Client client = Client.builder().apiKey(API_KEY).build();

            // Configuration système pour forcer la Darija
            GenerateContentConfig config = GenerateContentConfig.builder()
                .systemInstruction(Content.fromParts(Part.fromText("Translate only to Moroccan Darija dialect using Arabic script.")))
                .temperature(1.0f) // Recommandé pour Gemini 3
                .build();

            try {
                // Appel au modèle Gemini 3 Flash Preview
                GenerateContentResponse response = client.models.generateContent("gemini-3-flash-preview", text, config);
                
                // On retourne directement ici si ça réussit
                return response.text(); 
                
            } catch (Exception e) {
                // Gestion spécifique du quota Free Tier (20 requêtes/min)
                if (e.getMessage().contains("429")) {
                    return "Désolé, l'IA fait une petite pause (Quota atteint). Réessayez dans 1 minute !";
                }
                throw e; // Relance l'erreur pour le catch général
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur technique : " + e.getMessage();
        }
    }
}
