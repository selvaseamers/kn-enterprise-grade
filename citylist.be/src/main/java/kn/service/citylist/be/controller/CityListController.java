package kn.service.citylist.be.controller;

import kn.service.citylist.be.controller.to.response.CityListPaginatedResponseTO;
import kn.service.citylist.be.controller.to.response.CityListTO;
import kn.service.citylist.be.service.CityListService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1")
public class CityListController {

    private static final Logger LOGGER = LogManager.getLogger(CityListController.class.getName());

    private final CityListService cityListService;

    public CityListController(CityListService cityListService) {
        this.cityListService = cityListService;
    }

    @GetMapping(value = "/city-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CityListPaginatedResponseTO> getCityList(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        LOGGER.info("Get City List for page {} and size {}", () -> page, () -> pageSize);
        CityListPaginatedResponseTO cityListPaginatedResponseTO = cityListService.getLimitedCityList(page, pageSize);
        LOGGER.info("Paginated Response {}", () -> cityListPaginatedResponseTO);
        return ResponseEntity.ok(cityListPaginatedResponseTO);
    }

    @PutMapping(value = "/admin/city-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CityListTO> updateCityList(@RequestParam(value = "id") long id, @RequestParam(value = "cityName") String cityName,
                                                     @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        LOGGER.info("Update City Details for id {} and name {}, {} ", id, cityName, imageFile);
        CityListTO cityListTO = cityListService.updateCityDetailsAndGet(id, cityName, imageFile);
        LOGGER.info("Updated City Details: {} ", () -> cityListTO);
        return ResponseEntity.ok(cityListTO);
    }


}
