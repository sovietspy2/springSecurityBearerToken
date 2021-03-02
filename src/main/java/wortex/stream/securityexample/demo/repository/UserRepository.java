package wortex.stream.securityexample.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wortex.stream.securityexample.demo.model.User;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepository {

        @Autowired
        private PasswordEncoder passwordEncoder;

       public Optional<User> findByUsername(String username) {

           if ("x".equals(username)) {
               return Optional.of(new User(1l, "x", passwordEncoder.encode("password"), List.of("ROLE_ADMIN")));
           } else {
               return Optional.empty();
           }
        }

}
