package kn.service.citylist.be.config.init;

import kn.service.citylist.be.entity.CityListDE;
import kn.service.citylist.be.repository.CityListRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

import static kn.service.citylist.be.constant.CommonConstant.COMMA_DELIMITER;

@Component
public class CityListInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LogManager.getLogger(CityListInitializer.class.getName());
    private final CityListRepository cityListRepository;

    public CityListInitializer(CityListRepository cityListRepository) {
        this.cityListRepository = cityListRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        LOGGER.info("Initializing CSV File Data");

        try (BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource("cities.csv").getFile()))) {
            br.lines().map(record -> {
                String[] values = record.split(COMMA_DELIMITER);
                return new CityListDE(Long.parseLong(values[0]), values[1], values[2]);
            }).forEach(cityListRepository::save);

            LOGGER.info("Initialization Completed");
        }
    }
}
