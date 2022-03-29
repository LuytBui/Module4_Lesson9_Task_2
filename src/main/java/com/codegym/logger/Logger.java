package com.codegym.logger;

import com.codegym.model.Book;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {

    private static int accessTimes = 0;

    @AfterReturning("execution(public * com.codegym.repository.IBookRepository.save(..))")
    public void logAfterSaveBook(JoinPoint joinPoint) {
        Book book = (Book) joinPoint.getArgs()[0];

        System.out.println(String.format("Change in book quantity: Book ID = %d | Book name = %s | stock = %d",book.getId(), book.getName(), book.getQuantity()));
    }

    @Before("execution(public * com.codegym.controller.LibraryController.*(..))")
    public void countAccessTimes(JoinPoint joinPoint){
        accessTimes ++;
        String method = joinPoint.getSignature().getName();
        System.out.println(String.format("New access: %s; total access: %d", method, accessTimes));
    }
}
