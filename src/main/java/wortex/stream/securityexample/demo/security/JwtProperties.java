package wortex.stream.securityexample.demo.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "server")
public class JwtProperties {
    private String secretKey;
    private long validityInMs;
}
