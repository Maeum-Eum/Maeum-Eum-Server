package com.five.Maeum_Eum.repository.center;
import com.five.Maeum_Eum.entity.center.Center;
import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByAddress(String Address);
    Optional<Center> findByCenterName(String centerName);
}
