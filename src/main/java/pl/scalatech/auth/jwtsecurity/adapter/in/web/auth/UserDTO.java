package pl.scalatech.auth.jwtsecurity.adapter.in.web.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserDTO {
    private String username;
    private String password;
}