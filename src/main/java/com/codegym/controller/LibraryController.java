package com.codegym.controller;

import com.codegym.exception.BookOutOfStockException;
import com.codegym.exception.BorrowSessionNotExistException;
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
    public ModelAndView redirectToErrorPageOutOfStock(Exception e) {
        System.out.println(e.getMessage());
        return new ModelAndView("exception", "exception", e);
    }

    @ExceptionHandler(BorrowSessionNotExistException.class)
    public ModelAndView redirectToErrorPageSessionNotExist(Exception e) {
        System.out.println(e.getMessage());
        return new ModelAndView("exception", "exception", e);
    }

    @GetMapping
    public ModelAndView showHomePage(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return new ModelAndView("home", "books", books);
    }

    @GetMapping("/borrow/{id}")
    public ModelAndView showBorrowPage(@PathVariable Long id) {
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

        String code = "" + (int) Math.floor(Math.random() * 100000);
        BorrowBookSession borrowBookSession = new BorrowBookSession();
        borrowBookSession.setBook(book.get());
        borrowBookSession.setCode(code);
        borrowBookSessionRepository.save(borrowBookSession);

        book.get().setQuantity(book.get().getQuantity() - 1);
        bookRepository.save(book.get());

        String message = "Borrow success: " + book.get().getName() + " | borrow code = " + code + " (IMPORTANT - use this code when returning)";
        ModelAndView modelAndView = new ModelAndView("borrow");
        modelAndView.addObject("message", message);
        return modelAndView;
    }

    @GetMapping("/return")
    public ModelAndView showReturnBookPage(@RequestParam(name = "code") Optional<String> code, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("list-borrow-sessions");
        Page<BorrowBookSession> borrowBookSessions;
        if (!code.isPresent()) {
            // No "code" in Reques Params: show all
            borrowBookSessions = borrowBookSessionRepository.findAll(pageable);
            modelAndView.addObject("borrowSessions", borrowBookSessions);
        } else {
            // "code" is present
            borrowBookSessions = borrowBookSessionRepository.findAllByCode(code.get(), pageable);
            modelAndView.addObject("borrowSessions", borrowBookSessions);
        }
        return modelAndView;
    }

    @GetMapping("/return/{id}")
    public ModelAndView showReturnBookPage(@PathVariable Long id) throws BorrowSessionNotExistException {
        Optional<BorrowBookSession> borrowBookSession = borrowBookSessionRepository.findById(id);

        if (!borrowBookSession.isPresent())
            throw new BorrowSessionNotExistException();

        Book book = borrowBookSession.get().getBook();
        ModelAndView modelAndView = new ModelAndView("return");
        modelAndView.addObject("book", book);
        modelAndView.addObject("borrowSession", borrowBookSession.get());
        return modelAndView;
    }

    @PostMapping("/return/{id}")
    public ModelAndView returnBook(@PathVariable Long id) throws BorrowSessionNotExistException {
        Optional<BorrowBookSession> borrowBookSession = borrowBookSessionRepository.findById(id);

        if (!borrowBookSession.isPresent())
            throw new BorrowSessionNotExistException();

        Book book = borrowBookSession.get().getBook();
        book.setQuantity(book.getQuantity() + 1);
        borrowBookSessionRepository.delete(borrowBookSession.get());
        bookRepository.save(book);

        String message = "Return success: Book name = " + book.getName() + " | borrow code = " + borrowBookSession.get().getCode() + ".";
        ModelAndView modelAndView = new ModelAndView("return");
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}
