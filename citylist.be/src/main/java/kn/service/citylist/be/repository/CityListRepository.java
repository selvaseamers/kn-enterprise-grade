package kn.service.citylist.be.repository;

import kn.service.citylist.be.entity.CityListDE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CityListRepository extends JpaRepository<CityListDE, Long> {

    @Query("select count(cl) from CityListDE as cl")
    Optional<Integer> findCount();
}
