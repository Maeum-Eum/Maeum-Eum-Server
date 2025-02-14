package com.five.Maeum_Eum.repository.manager;

import com.five.Maeum_Eum.entity.user.manager.ManagerBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerBookmarkRepository extends JpaRepository<ManagerBookmark , Long> {
}
