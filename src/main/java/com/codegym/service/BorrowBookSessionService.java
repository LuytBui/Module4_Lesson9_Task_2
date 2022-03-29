package com.codegym.service;

import com.codegym.model.BorrowBookSession;
import com.codegym.repository.IBorrowBookSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class BorrowBookSessionService implements IBorrowBookSessionService{
    @Autowired
    IBorrowBookSessionRepository borrowBookSessionRepository;

    @Override
    public Page<BorrowBookSession> findAll(Pageable pageable) {
        return borrowBookSessionRepository.findAll(pageable);
    }

    @Override
    public Optional<BorrowBookSession> findById(Long id) {
        return borrowBookSessionRepository.findById(id);
    }

    @Override
    public void save(BorrowBookSession borrowBookSession) {
        borrowBookSessionRepository.save(borrowBookSession);
    }

    @Override
    public void remove(BorrowBookSession borrowBookSession) {
        borrowBookSessionRepository.delete(borrowBookSession);
    }

    @Override
    public void remove(Long id) {
        borrowBookSessionRepository.deleteById(id);
    }
}
