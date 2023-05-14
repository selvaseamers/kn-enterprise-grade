package kn.service.citylist.be.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "city_list")
public class CityListDE {
    @Id
    private Long id;

    private String cityName;

    private String imageUrl;
}
