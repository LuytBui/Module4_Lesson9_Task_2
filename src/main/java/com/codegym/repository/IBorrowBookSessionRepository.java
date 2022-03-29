package com.codegym.repository;

import com.codegym.model.BorrowBookSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBorrowBookSessionRepository extends PagingAndSortingRepository<BorrowBookSession, Long> {
    Page<BorrowBookSession> findAllByCode(String code, Pageable pageable);
}
