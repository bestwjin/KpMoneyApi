package com.wjlee.kpmoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wjlee.kpmoney.model.DistrbtModel;

@Repository
public interface DistrbtRepository extends JpaRepository<DistrbtModel, String> {

}
