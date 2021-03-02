package wortex.stream.securityexample.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import wortex.stream.securityexample.demo.security.JwtTokenProvider;
import wortex.stream.securityexample.demo.web.AuthenticationRequest;

import static org.springframework.http.ResponseEntity.ok;


import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    //private final UserRepository users;

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(authentication);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("invalid credentials");
        }
    }
}
