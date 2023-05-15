package kn.service.citylist.be.controller;

import kn.service.citylist.be.controller.to.response.CityListPaginatedResponseTO;
import kn.service.citylist.be.controller.to.response.CityListTO;
import kn.service.citylist.be.service.CityListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityListControllerTest {

    @InjectMocks
    CityListController cityListController;

    @Mock
    CityListService cityListService;

    private MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", null, new byte[0]);

    @Test
    public void getCityList_Positive() {
        when(cityListService.getLimitedCityList(anyInt(), anyInt()))
                .thenReturn(CityListPaginatedResponseTO.builder()
                        .cityList(List.of(CityListTO.builder().id(1).cityName("test1").imageUrl("dummy-url1").build(),
                                CityListTO.builder().id(1).cityName("test2").imageUrl("dummy-url2").build()))
                        .page(1)
                        .pageSize(100)
                        .total(1000)
                        .build());
        ResponseEntity<CityListPaginatedResponseTO> response = cityListController.getCityList(1, 100);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(2, response.getBody().getCityList().size());
        assertEquals(1, response.getBody().getPage());
        assertEquals(100, response.getBody().getPageSize());
        assertEquals(1000, response.getBody().getTotal());
    }

    @Test
    public void getCityList_PositiveWithEmptyList() {
        when(cityListService.getLimitedCityList(anyInt(), anyInt()))
                .thenReturn(CityListPaginatedResponseTO.builder()
                        .cityList(List.of())
                        .page(0)
                        .pageSize(0)
                        .total(0)
                        .build());
        ResponseEntity<CityListPaginatedResponseTO> response = cityListController.getCityList(1, 100);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(0, response.getBody().getCityList().size());
        assertEquals(0, response.getBody().getPage());
        assertEquals(0, response.getBody().getPageSize());
        assertEquals(0, response.getBody().getTotal());
    }

    @Test
    public void updateCityList_Positive() {
        when(cityListService.updateCityDetailsAndGet(1, "mumbai", multipartFile))
                .thenReturn(CityListTO.builder().id(1).cityName("mumbai").imageUrl("dummy-url1").build());

        ResponseEntity<CityListTO> response = cityListController.updateCityList(1, "mumbai", multipartFile);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1, response.getBody().getId());
        assertEquals("mumbai", response.getBody().getCityName());
        assertEquals("dummy-url1", response.getBody().getImageUrl());
    }

    @Test
    public void updateCityList_PositiveWithEmptyList() {
        when(cityListService.updateCityDetailsAndGet(1, "mumbai", multipartFile))
                .thenReturn(CityListTO.builder().build());
        ResponseEntity<CityListTO> response = cityListController.updateCityList(1, "mumbai", multipartFile);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNull(response.getBody().getImageUrl());
    }


}