package kn.service.citylist.be.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import kn.service.citylist.be.Application;
import kn.service.citylist.be.controller.to.response.CityListTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static kn.service.citylist.be.util.JsonMapperUtil.convertStringToObject;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private Integer port;

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void getRoles_PositiveUserRole() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Authorization", getUserAuth());
        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(get("/v1/login")
                        .headers(header))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();

        List<String> response = convertStringToObject(mockHttpServletResponse.getContentAsString(), new TypeReference<>() {
        });
        assertTrue(response.contains("ROLE_VIEW"));
    }

    @Test
    public void getRoles_PositiveAdminRole() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Authorization", getAdminAuth());
        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(get("/v1/login")
                        .headers(header))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        List<String> response = convertStringToObject(mockHttpServletResponse.getContentAsString(), new TypeReference<>() {
        });
        assertTrue(response.contains("ROLE_ALLOW_EDIT"));

    }

    @Test
    public void getRoles_NegativeUnAuthorized() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Authorization", "invalid-cred");
        mockMvc.perform(get("/v1/login")
                        .headers(header))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getCityList_Positive() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Authorization", getUserAuth());
        mockMvc.perform(get("/v1/city-list?page=0&pageSize=100")
                        .headers(header))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getCityList_NegativeBadRequest() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Authorization", getUserAuth());
        mockMvc.perform(get("/v1/city-list?page=0")
                        .headers(header))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateCityList_Positive() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", getAdminAuth());
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

        multipartBodyBuilder.part("id", 1);
        multipartBodyBuilder.part("cityName", "Mumbai");
        Resource resource = new ClassPathResource("4_Bombay_bombay_mumby_image.jpg");
        multipartBodyBuilder.part("image", resource, MediaType.IMAGE_JPEG);
        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);

        ResponseEntity<CityListTO> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/api/enterprise-grade/v1/admin/city-list", HttpMethod.PUT, httpEntity, CityListTO.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void updateCityList_PositiveWithOutImage() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", getAdminAuth());
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

        multipartBodyBuilder.part("id", 1);
        multipartBodyBuilder.part("cityName", "Mumbai");

        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);

        ResponseEntity<CityListTO> responseEntity =
                restTemplate.exchange("http://localhost:" + port + "/api/enterprise-grade/v1/admin/city-list", HttpMethod.PUT, httpEntity, CityListTO.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void updateCityList_NegtiveWith403() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", getUserAuth());
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

        multipartBodyBuilder.part("id", 1);
        multipartBodyBuilder.part("cityName", "Mumbai");
        Resource resource = new ClassPathResource("4_Bombay_bombay_mumby_image.jpg");
        multipartBodyBuilder.part("image", resource, MediaType.IMAGE_JPEG);
        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody, headers);
        try {
            ResponseEntity<CityListTO> responseEntity =
                    restTemplate.exchange("http://localhost:" + port + "/api/enterprise-grade/v1/admin/city-list", HttpMethod.PUT, httpEntity, CityListTO.class);
        } catch (HttpClientErrorException e) {
            assertTrue(e.getStatusCode().value() == 403);
        }


    }

    private static String getUserAuth() {
        return "Basic " + Base64.encodeBase64String("selva:selva123".getBytes(StandardCharsets.UTF_8));
    }

    private static String getAdminAuth() {
        return "Basic " + Base64.encodeBase64String("kuehne:kuehne123".getBytes(StandardCharsets.UTF_8));
    }

}
