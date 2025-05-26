package com.batch_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.batch_service.entity.Batch;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
}
