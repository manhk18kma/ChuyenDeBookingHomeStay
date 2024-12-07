package KMA.BeBookingApp.domain.user.dto.response;
import lombok.*;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleIosUser {
    @JsonProperty("sub")
    private String id;
    private String email;

    @JsonProperty("email_verified")
    private boolean verifiedEmail;

    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String picture;

    private String locale;
}
