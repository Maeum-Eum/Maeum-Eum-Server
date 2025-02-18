package com.five.Maeum_Eum.repository.elder;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.SavedElders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedEldersRepository extends JpaRepository<SavedElders, Long> {
    Optional<SavedElders> findByElderAndCaregiver(Elder elder, Caregiver caregiver);
}
