package mainpackage.security;

import lombok.Data;

@Data
public class UserCredentialsDto {
    private String email;
    private String password;
}
