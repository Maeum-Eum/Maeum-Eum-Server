package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerBookmarkRepository extends JpaRepository<ManagerBookmark , Long> {

    @Query("SELECT COUNT(mb) FROM ManagerBookmark mb WHERE mb.manager.managerId = :managerId")
    int countManagerBookmarkByManagerId(@Param("managerId") Long managerId);

    @Query("SELECT mb FROM ManagerBookmark mb WHERE mb.manager.managerId =:managerId AND mb.elder.elderId =:elderId")
    List<ManagerBookmark> findByManagerIdAndElderId(@Param("managerId") Long managerId, @Param("elderId") Long elderId);


    @Query("SELECT CASE WHEN COUNT(mb) > 0 THEN TRUE ELSE FALSE END FROM ManagerBookmark mb WHERE mb.manager.managerId = :managerId AND mb.caregiver.caregiverId = :caregiverId")
    boolean findByManagerIdAndCaregiverId(@Param("managerId") Long managerId, @Param("caregiverId") Long caregiverId);
}
