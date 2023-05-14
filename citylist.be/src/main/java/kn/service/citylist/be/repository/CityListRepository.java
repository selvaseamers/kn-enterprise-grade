package kn.service.citylist.be.repository;

import kn.service.citylist.be.entity.CityListDE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityListRepository extends JpaRepository<CityListDE, Long> {
}
