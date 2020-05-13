package pl.scalatech.auth.jwtsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
class HomeController {

    @GetMapping("/")
    String index() {
        return "Hello world";
    }

    @GetMapping("/secContext")
    ResponseEntity<Map> secContext(Principal principal) {
        Map<String, String> context = new HashMap<>();
        context.put("userName", getUserName(principal));
        context.put("roles", getUserRoles(principal).stream()
                                                    .collect(joining(" ,")));
        return ok(context);
    }

    private String getUserName(Principal principal) {
        if (principal == null) {
            return "anonymous";
        } else {

            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                log.info("grantedAuthority : {}", grantedAuthority.getAuthority());
            }
            return principal.getName();
        }
    }

    private Collection<String> getUserRoles(Principal principal) {
        if (principal == null) {
            return Arrays.asList("none");
        } else {

            Set<String> roles = new HashSet<String>();

            final UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
            for (GrantedAuthority grantedAuthority : authorities) {
                roles.add(grantedAuthority.getAuthority());
            }
            return roles;
        }

    }
}
