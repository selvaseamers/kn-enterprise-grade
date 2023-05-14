package kn.service.citylist.be.controller.to.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CityListPaginatedResponseTO {

    private String total;
    private int pageSize;
    private int page;
    private List<CityListTO> cityList;


}
