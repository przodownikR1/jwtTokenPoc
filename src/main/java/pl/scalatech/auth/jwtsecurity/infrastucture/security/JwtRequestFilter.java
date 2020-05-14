package pl.scalatech.auth.jwtsecurity.infrastucture.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.scalatech.auth.jwtsecurity.infrastucture.security.SecurityConstants.HEADER_STRING;


@Slf4j
@RequiredArgsConstructor
class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService jwtUserDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader(HEADER_STRING);

        String username = null;
        String jwtToken = JwtTokenProvider.resolveToken(request);
        if (requestTokenHeader != null) {
            try {
                username = jwtTokenProvider.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token", e);
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired", e);
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext()
                                                     .getAuthentication() == null) {

            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenProvider.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext()
                                     .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}