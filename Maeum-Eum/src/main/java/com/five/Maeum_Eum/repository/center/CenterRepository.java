package com.five.Maeum_Eum.repository.center;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByAddress(String Address);
    Optional<Center> findByCenterName(String centerName);

    @Query("select c from Center c where c.centerId =:centerId")
    Optional<Center> findByCenterId(@Param("centerId") Long centerId);

    @Query("SELECT c FROM Center c WHERE c.centerName LIKE %:keyword%")
    List<Center> searchCentersByKeyword(@Param("keyword") String keyword);
}
