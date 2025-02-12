package com.five.Maeum_Eum.repository.elder;

import com.five.Maeum_Eum.entity.user.elder.SavedElders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedEldersRepository extends JpaRepository<SavedElders, Long> {
}
