package com.dbfurniture.repository;

import com.dbfurniture.entity.CaseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseReviewRepository extends JpaRepository<CaseReview, String> {
    List<CaseReview> findAllByOrderBySortAsc();
}
