package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Rating;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

public class RatingServiceRestClient implements RatingService {
    private static final String URL = "http://localhost:8080/rest/rating";

    @Override
    public void setRating(Rating rating) {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(URL)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(rating, MediaType.APPLICATION_JSON), Response.class);
        }
        catch (Exception e) {
            throw new ScoreException("Error saving rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try {
            Client client = ClientBuilder.newClient();
            return client.target(URL)
                    .path("/" + game)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Integer.class);
        }
        catch (Exception e) {
            throw new ScoreException("Error loading rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) {
        try {
            Client client = ClientBuilder.newClient();
            return client.target(URL)
                    .path("/" + game + "/" + player)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Integer.class);
        }
        catch (Exception e) {
            throw new ScoreException("Error loading rating", e);
        }
    }
}