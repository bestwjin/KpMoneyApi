package com.wjlee.kpmoney.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wjlee.kpmoney.model.DistrbtModel;

@Repository
public interface DistrbtRepository extends JpaRepository<DistrbtModel, String> {
	Optional<List<DistrbtModel>> findByTokenAndReceivedYnFalse(String token);	
}
