package com.wjlee.kpmoney.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wjlee.kpmoney.model.SprinkleModel;

@Repository
public interface SprinkleRepository extends JpaRepository<SprinkleModel, String> {
	
	// 뿌리기 정보 조회(토큰, 아이디, 뿌려진지 7일이내 조건)
	SprinkleModel findByTokenAndUserIdAndRegDateGreaterThanEqual(String token, String userId, Date regDate);
	
	// 토큰 기존재 유무 확인
	Optional<SprinkleModel> findByTokenAndRoomId(String token, String roomId); 
}