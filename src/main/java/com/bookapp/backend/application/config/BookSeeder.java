package com.bookapp.backend.application.config;

import com.bookapp.backend.adapter.out.bookapi.BookApiService;
import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.ports.out.IBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class BookSeeder implements ApplicationRunner {

    private final IBookRepository bookRepository;
    private final BookApiService bookApiService;

    @Override
    public void run(ApplicationArguments args){
        if (bookRepository.count() == 0) {
            System.out.println("Books are fetching ...");
            List<Book> booksFromApi = bookApiService.fetchBooksFromGoogleApi(1000);
            System.out.println("Book are saving ...");
            bookRepository.saveAll(booksFromApi);
            System.out.println("Books fetched und saved successfully");
        }
    }
}

