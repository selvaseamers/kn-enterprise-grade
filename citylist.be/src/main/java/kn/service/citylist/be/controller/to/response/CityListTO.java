package kn.service.citylist.be.controller.to.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityListTO {
    private long id;
    private String cityName;
    private String imageUrl;
}
