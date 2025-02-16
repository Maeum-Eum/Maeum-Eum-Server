package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerBookmarkRepository extends JpaRepository<ManagerBookmark , Long> {

    @Query("SELECT COUNT(mb) FROM ManagerBookmark mb WHERE mb.manager.managerId = :managerId")
    int countManagerBookmarkByManagerId(@Param("managerId") Long managerId);
}
