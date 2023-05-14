package kn.service.citylist.be.controller.to.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityListTO {
    private long id;
    private String cityName;
    private String imageUrl;
}
