package com.codegym.service;

import com.codegym.model.Book;
import com.codegym.repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class BookService implements IBookService {
    @Autowired
    IBookRepository loanBookRepository;

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return loanBookRepository.findAll(pageable);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return loanBookRepository.findById(id);
    }

    @Override
    public void save(Book book) {
        loanBookRepository.save(book);
    }

    @Override
    public void remove(Book book) {
        loanBookRepository.delete(book);
    }

    @Override
    public void remove(Long id) {
        loanBookRepository.deleteById(id);
    }
}
