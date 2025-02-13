package com.five.Maeum_Eum.service.user;

import com.five.Maeum_Eum.entity.user.caregiver.Caregiver;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.jwt.CaregiverUserDetails;
import com.five.Maeum_Eum.jwt.ManagerUserDetails;
import com.five.Maeum_Eum.repository.caregiver.CaregiverRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final ManagerRepository managerRepository;
    private final CaregiverRepository caregiverRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = managerRepository.findByLoginId(username).orElse(null);
        if (manager != null) {
            return new ManagerUserDetails(manager);
        }

        Caregiver caregiver = caregiverRepository.findByLoginId(username).orElse(null);

        if(caregiver != null) {
            return new CaregiverUserDetails(caregiver);
        }


            throw new UsernameNotFoundException("User not found");


    }
}
