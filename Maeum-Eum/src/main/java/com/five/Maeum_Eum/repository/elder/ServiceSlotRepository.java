package com.five.Maeum_Eum.repository.elder;

import com.five.Maeum_Eum.entity.user.elder.Elder;
import com.five.Maeum_Eum.entity.user.elder.ServiceSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ServiceSlotRepository extends JpaRepository<ServiceSlot, Long> {
    boolean existsByElderAndServiceSlotDayIn(Elder elder, List<Integer> serviceSlotDay);
    void deleteByElder(Elder elder);
}
