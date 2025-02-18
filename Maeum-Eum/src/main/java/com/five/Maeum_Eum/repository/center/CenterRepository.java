package com.five.Maeum_Eum.repository.center;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByDetailAddress(String detailAddress);
    Optional<Center> findByCenterName(String centerName);

    @Query("select c from Center c where c.centerId =:centerId")
    Optional<Center> findByCenterId(@Param("centerId") Long centerId);

    List<Center> findByCenterNameContaining(String keyword);
}
