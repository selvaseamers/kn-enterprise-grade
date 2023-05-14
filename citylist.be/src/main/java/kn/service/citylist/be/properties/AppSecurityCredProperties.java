package kn.service.citylist.be.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "application.access.cred")
@Configuration
@Data
public class AppSecurityCredProperties {

    private Map<String, String> user;
    private Map<String, String> admin;
}
