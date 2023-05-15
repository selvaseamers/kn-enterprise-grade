package kn.service.citylist.be.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static kn.service.citylist.be.util.HelperUtil.ALLOW_EDIT;
import static kn.service.citylist.be.util.HelperUtil.ALLOW_VIEW;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserAuthControllerTest {

    @InjectMocks
    UserAuthController userAuthController;



    @Test
    public void getRoles_PositiveALLOW_VIEW(){
        ResponseEntity<List<String>> response= userAuthController.getRoles(ALLOW_VIEW);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().stream().anyMatch(data -> data.equals("ALLOW_VIEW")));
    }

    @Test
    public void getRoles_PositiveALLOW_EDIT(){
        ResponseEntity<List<String>> response= userAuthController.getRoles(ALLOW_EDIT);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertTrue(response.getBody().stream().anyMatch(data -> data.equals("ALLOW_EDIT")));
    }


}