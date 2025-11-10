package com.bookapp.backend.domain.model.list;

import com.bookapp.backend.domain.model.base.BaseModel;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class List extends BaseModel {

    private User user;
    private String name;
    private java.util.List<String> items = new java.util.ArrayList<>();
    private java.util.List<Book> booksList = new java.util.ArrayList<>();

    public List(Long id, User user, String name) {
        super(id);
        this.user = Objects.requireNonNull(user, "Benutzer darf nicht null sein");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Listenname darf nicht leer sein");
        }
        this.name = name;
    }

    public List(@NotNull(message = "ListID muss zugewiesen worden sein") long listId) {
        super(listId);
    }

    public static java.util.List<List> of(List... lists) {
        return java.util.List.of(lists);
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Listenname darf nicht leer sein");
        }
        this.name = newName;
    }
    public List(Long id) {
        super(id);
    }
    public int size() {
        return items.size();
    }


}
