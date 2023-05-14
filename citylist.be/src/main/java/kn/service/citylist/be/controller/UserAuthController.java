package kn.service.citylist.be.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserAuthController {
    private static final Logger LOGGER = LogManager.getLogger(UserAuthController.class.getName());

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCityList(Authentication authentication) {
        LOGGER.info("User Authenticated SuccessFully");
        return ResponseEntity.ok(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }


}
