package KMA.BeBookingApp.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class  OutboundUserResponse {
//    String id;
//    String email;
//    boolean verifiedEmail;
//    String name;
//    String givenName;
//    String familyName;
//    String picture;
//    String locale;

//    @JsonProperty("sub")
//    private String id;
//    private String email;
//
//    @JsonProperty("email_verified")
//    private boolean verifiedEmail;
//
//    private String name;
//
//    @JsonProperty("given_name")
//    private String givenName;
//
//    @JsonProperty("family_name")
//    private String familyName;
//
//    private String picture;
//
//    private String locale;

    private String id;
    private String email;
    private boolean emailVerified;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String locale;


}
