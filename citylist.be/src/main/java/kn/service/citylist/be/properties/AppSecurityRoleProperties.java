package kn.service.citylist.be.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application.access.roles")
@Configuration
@Data
public class AppSecurityRoleProperties {

    private String user;
    private String admin;
}
