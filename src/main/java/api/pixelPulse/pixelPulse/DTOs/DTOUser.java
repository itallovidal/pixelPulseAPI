package api.pixelPulse.pixelPulse.DTOs;

import api.pixelPulse.pixelPulse.enums.Genres;

public record DTOUser(String userID,
                      String email,
                      String password,
                      String gamesLife,
                      Genres favoriteGenre1,
                      Genres favoriteGenre2 ) {
}
