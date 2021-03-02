package wortex.stream.securityexample.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping(path="/public")
    public String publicApi() {
        return "Hello there";
    }

}
