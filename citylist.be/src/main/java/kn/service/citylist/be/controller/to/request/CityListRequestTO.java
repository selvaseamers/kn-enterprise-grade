package kn.service.citylist.be.controller.to.request;

import lombok.Data;

@Data
public class CityListRequestTO {
    private long id;
    private String cityName;
    private String imageUrl;
}
