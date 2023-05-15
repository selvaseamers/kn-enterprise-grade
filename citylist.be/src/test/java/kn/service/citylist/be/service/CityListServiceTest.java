package kn.service.citylist.be.service;

import com.fasterxml.jackson.core.type.TypeReference;
import kn.service.citylist.be.controller.to.response.CityListPaginatedResponseTO;
import kn.service.citylist.be.controller.to.response.CityListTO;
import kn.service.citylist.be.entity.CityListDE;
import kn.service.citylist.be.repository.CityListRepository;
import kn.service.citylist.be.util.JsonMapperUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kn.service.citylist.be.util.JsonMapperUtil.convertJsonToObject;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityListServiceTest {

    @InjectMocks
    CityListService cityListService;

    @Mock
    CityListRepository cityListRepository;

    private static List<CityListDE> cityListDEPage;


    private MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", null, new byte[0]);

    @BeforeAll
    public static void setUp(){
        cityListDEPage = convertJsonToObject("city-list.json", new TypeReference<>(){});
    }

    @Test
    public void getLimitedCityList_Positive() {
        when(cityListRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(cityListDEPage));
        when(cityListRepository.findCount()).thenReturn(Optional.of(12));

        CityListPaginatedResponseTO response = cityListService.getLimitedCityList(0,10);
        assertEquals(12, response.getTotal());
        assertEquals(100, response.getCityList().size());

    }

    @Test
    public void getLimitedCityList_PositiveEmptyList() {
        when(cityListRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(List.of()));
        when(cityListRepository.findCount()).thenReturn(Optional.of(12));

        CityListPaginatedResponseTO response = cityListService.getLimitedCityList(0,10);
        assertEquals(12, response.getTotal());
        assertEquals(0, response.getCityList().size());

    }

    @Test
    public void getLimitedCityList_NegativeEmptyCount() {
        when(cityListRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(List.of()));
        when(cityListRepository.findCount()).thenReturn(Optional.empty());
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> cityListService.getLimitedCityList(0,10),
                "Expected doThing() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contentEquals("No value present"));
    }

    @Test
    public void updateCityDetailsAndGet_Positive() {
        CityListDE cityListDE = new CityListDE();
        cityListDE.setId(1L);
        cityListDE.setCityName("mumbai");
        cityListDE.setImageUrl("dummy-url");
        when(cityListRepository.findById(anyLong())).thenReturn(Optional.of(cityListDE));
        when(cityListRepository.saveAndFlush(any())).thenReturn(cityListDE);
        CityListTO response =  cityListService.updateCityDetailsAndGet(1,"mumbai", multipartFile);
        assertEquals(1, response.getId());
    }

    @Test
    public void updateCityDetailsAndGet_PositiveWithOutFile() {
        CityListDE cityListDE = new CityListDE();
        cityListDE.setId(1L);
        cityListDE.setCityName("mumbai");
        cityListDE.setImageUrl("dummy-url");
        when(cityListRepository.findById(anyLong())).thenReturn(Optional.of(cityListDE));
        when(cityListRepository.saveAndFlush(any())).thenReturn(cityListDE);
        CityListTO response =  cityListService.updateCityDetailsAndGet(1,"mumbai", null);
        assertEquals(1, response.getId());
    }

}