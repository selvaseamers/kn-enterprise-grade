package kn.service.citylist.be.service;

import kn.service.citylist.be.controller.to.response.CityListPaginatedResponseTO;
import kn.service.citylist.be.controller.to.response.CityListTO;
import kn.service.citylist.be.entity.CityListDE;
import kn.service.citylist.be.exception.ApplicationException;
import kn.service.citylist.be.repository.CityListRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class CityListService {

    private static final Logger LOGGER = LogManager.getLogger(CityListService.class.getName());

    private final CityListRepository cityListRepository;

    private final Path fileStorageLocation;

    public CityListService(CityListRepository cityListRepository) {
        this.cityListRepository = cityListRepository;
        this.fileStorageLocation = Paths.get("build", "resources", "main", "img");
    }

    public CityListPaginatedResponseTO getLimitedCityList(int page, int pageSize) {

        LOGGER.info("Querying DB");
        List<CityListDE> cityListDEList = cityListRepository.findAll(PageRequest.of(page, pageSize)).toList();
        Optional<Integer> totalCount = cityListRepository.findCount();
        return CityListPaginatedResponseTO.builder()
                .page(page)
                .pageSize(pageSize)
                .total(totalCount.get())
                .cityList(cityListDEList.stream().map(de -> CityListTO.builder()
                                .cityName(de.getCityName())
                                .id(de.getId())
                                .imageUrl(de.getImageUrl())
                                .build())
                        .toList())
                .build();

    }

    public CityListTO updateCityDetailsAndGet(long id, String cityName, MultipartFile imageFile) {

        Optional<CityListDE> cityListDE = cityListRepository.findById(id);
        CityListTO.CityListTOBuilder cityListTOBuilder = CityListTO.builder().id(id);

        cityListDE.ifPresent(cityList -> {
            LOGGER.info("Existing city details {} ", cityList);
            cityList.setCityName(cityName);
            if (null != imageFile) {
                cityList.setImageUrl(saveImage(id, cityName, imageFile));
            }
            cityListRepository.saveAndFlush(cityList);
            cityListTOBuilder
                    .cityName(cityList.getCityName())
                    .imageUrl(cityList.getImageUrl());
            LOGGER.info("Updated city details {} ", cityList);
        });
        return cityListTOBuilder.build();
    }

    private String saveImage(long id, String cityName, MultipartFile file) {
        try {
            String newFileName = id + "_" + cityName + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), fileStorageLocation.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);
            return "http://localhost:8080/api/enterprise-grade/img/" + newFileName;
        } catch (Exception e) {
            LOGGER.error("Error while saving file");
            e.printStackTrace();
            throw new ApplicationException(e.getMessage());
        }
    }

}
