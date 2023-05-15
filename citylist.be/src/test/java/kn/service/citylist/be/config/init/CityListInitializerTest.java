package kn.service.citylist.be.config.init;

import kn.service.citylist.be.entity.CityListDE;
import kn.service.citylist.be.repository.CityListRepository;
import kn.service.citylist.be.util.MockedAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityListInitializerTest {

    @InjectMocks
    CityListInitializer cityListInitializer;

    @Mock
    CityListRepository cityListRepository;

    private static MockedAppender mockedAppender;

    private static Logger logger;

    @BeforeAll
    public static void setUpInit() {
        mockedAppender = new MockedAppender();
        logger = (Logger) LogManager.getLogger(CityListInitializer.class);
        logger.addAppender(mockedAppender);
        logger.setLevel(Level.INFO);
    }

    @AfterAll
    public static void teardown() {
        logger.removeAppender(mockedAppender);
    }

    @BeforeEach
    public void setUp(){
        mockedAppender.message.clear();
    }

    @Test
    public void dataInitializer_Positive() throws Exception {
        when(cityListRepository.save(any())).thenReturn(new CityListDE());
        cityListInitializer.run(new DefaultApplicationArguments());
        assertTrue(mockedAppender.message.stream().anyMatch(info -> info.contains("Initializing CSV File Data")));
        assertTrue(mockedAppender.message.stream().anyMatch(info -> info.contains("Initialization Completed")));
    }


}