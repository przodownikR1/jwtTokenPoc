package pl.scalatech.auth.jwtsecurity.infrastucture.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.scalatech.auth.jwtsecurity.port.UserRepositoryPort;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepo;

    private static Collection<? extends GrantedAuthority> getAuthorities(UserAuth user) {
        String[] userRoles = user.getRoles()
                                 .stream()
                                 .toArray(s -> new String[s]);
        return AuthorityUtils.createAuthorityList(userRoles);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth user = userRepo.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getAuthorities(user));
    }
    //Map AuthUserRequest to UserAuth

}