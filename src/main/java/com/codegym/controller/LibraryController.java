package com.codegym.controller;

import com.codegym.exception.BookOutOfStockException;
import com.codegym.model.Book;
import com.codegym.model.BorrowBookSession;
import com.codegym.repository.IBookRepository;
import com.codegym.repository.IBorrowBookSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    IBookRepository bookRepository;

    @Autowired
    IBorrowBookSessionRepository borrowBookSessionRepository;

    @ExceptionHandler(BookOutOfStockException.class)
    public ModelAndView redirectToErrorPageOutOfStock(Exception e){
        System.out.println(e.getMessage());
        return new ModelAndView("exception", "exception", e);
    }

    @GetMapping
    public ModelAndView showHomePage(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return new ModelAndView("home", "books", books);
    }

    @GetMapping("/borrow/{id}")
    public ModelAndView showBorrowPage(@PathVariable Long id){
        Optional<Book> book = bookRepository.findById(id);

        if (!book.isPresent())
            return new ModelAndView("error-404");
        return new ModelAndView("borrow", "book", book.get());
    }

    @PostMapping("/borrow/{id}")
    public ModelAndView borrowBook(@PathVariable Long id) throws BookOutOfStockException {
        Optional<Book> book = bookRepository.findById(id);

        if (!book.isPresent())
            return new ModelAndView("error-404");

        if (book.get().getQuantity() <= 0)
            throw new BookOutOfStockException();

        String code = "" + (int) Math.floor(Math.random()* 100000);
        BorrowBookSession borrowBookSession = new BorrowBookSession();
        borrowBookSession.setBook(book.get());
        borrowBookSession.setCode(code);
        borrowBookSessionRepository.save(borrowBookSession);

        book.get().setQuantity(book.get().getQuantity() - 1);
        bookRepository.save(book.get());

        String message = "Borrow success: "+ book.get().getName() + " | borrow code = "+ code + " (IMPORTANT - use this code when returning)";
        ModelAndView modelAndView = new ModelAndView("borrow");
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}
