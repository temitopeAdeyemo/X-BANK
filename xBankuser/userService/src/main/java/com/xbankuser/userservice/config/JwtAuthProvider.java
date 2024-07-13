package com.xbankuser.userservice.config;

import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthProvider implements AuthenticationProvider {
    private final UserRepository repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!authentication.isAuthenticated()){
            UserDetails userDetails = new DomainUserDetails().loadUserByUsername(authentication.getName());
            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class); // To show we will always pass UsernamePasswordAuthenticationToken class to authenticate method
    }

    class DomainUserDetails implements UserDetailsService {

        /**
         * Locates the user based on the username. In the actual implementation, the search
         * may possibly be case sensitive, or case insensitive depending on how the
         * implementation instance is configured. In this case, the <code>UserDetails</code>
         * object that comes back may have a username that is of a different case than what
         * was actually requested..
         *
         * @param username the username identifying the user whose data is required.
         * @return a fully populated user record (never <code>null</code>)
         * @throws UsernameNotFoundException if the user could not be found or the user has no
         *                                   GrantedAuthority
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            System.out.println("In userDetailsService **************** ");
            return  repository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        }
    }
}
