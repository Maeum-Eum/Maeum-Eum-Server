package com.five.Maeum_Eum.service.user.manager;


import com.five.Maeum_Eum.dto.user.manager.response.ManagerBasicDto;
import com.five.Maeum_Eum.entity.user.manager.Manager;
import com.five.Maeum_Eum.exception.CustomException;
import com.five.Maeum_Eum.exception.ErrorCode;
import com.five.Maeum_Eum.jwt.JWTUtil;
import com.five.Maeum_Eum.repository.manager.ManagerBookmarkRepository;
import com.five.Maeum_Eum.repository.manager.ManagerContactRepository;
import com.five.Maeum_Eum.repository.manager.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final JWTUtil jwtUtil;
    private final ManagerRepository managerRepository;
    private final ManagerContactRepository managerContactRepository;
    private final ManagerBookmarkRepository managerBookmarkRepository;

    // token으로  사용자 role 알아내기
    private String findRole(String token){
        return jwtUtil.getRole(token);
    }

    // token으로 사용자 loginId 알아내기
    private String findLoginId(String token){
        return jwtUtil.getId(token);
    }

    // 관리자의 기본 정보 조회
    public ManagerBasicDto getManagerBasicInfo(String token) {
        if(!findRole(token).equals("ROLE_MANAGER")){ // 사용자가 관리자 역할이 아닐 때
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
        }

        Manager manager = managerRepository.findByLoginId(findLoginId(token))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 요양보호사에게 보낸 연락 개수
        int sentContacts = managerContactRepository.countManagerContactByManagerId(manager.getManagerId());

        // 요양보호사 북마크 개수
        int bookmarks = managerBookmarkRepository.countManagerBookmarkByManagerId(manager.getManagerId());

        return ManagerBasicDto.from(manager , manager.getCenter() , sentContacts , bookmarks);
    }
}
