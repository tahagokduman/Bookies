package com.bookapp.backend.application.config;

import com.bookapp.backend.application.service.*;
import com.bookapp.backend.domain.model.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class MostPopularBooksComparator implements Comparator<Book> {
    private final CommentUseCases commentService;
    private final LikedBookUseCases likedBookService;
    private final LikedCommentUseCases likedCommentService;
    private final ListFollowUseCases listFollowUseCases;
    private final ListUseCases listUseCases;
    @Override
    public int compare(Book o1, Book o2) {
        double score1 = getPopularityScore(o1);
        double score2 = getPopularityScore(o2);
        return Double.compare(score2, score1);
    }

    private double getPopularityScore(Book book) {
        long likeCount = likedBookService.countByBookId(book.getId());
        long commentCount = commentService.getCommentsPaging(0, Integer.MAX_VALUE, book.getId()).getTotalElements();
        long likedCommentCount = likedCommentService.countLikedCommentsByBookId(book.getId());
        long listCount = listUseCases.countListsContainingBook(book.getId());
        long listFollowCount = listFollowUseCases.countFollowersOfListsContainingBook(book.getId());

        return (likeCount * 0.4) +
                (commentCount * 0.2) +
                (likedCommentCount * 0.15) +
                (listCount * 0.15) +
                (listFollowCount * 0.1);
    }
}
