package com.bookapp.backend.adapter.out.bookapi;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.domain.model.book.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookApiService {

    public List<Book> fetchBooksFromGoogleApi(int max) {
        List<Book> books = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        int fetched = 0;
        int startIndex = 0;

        while (fetched < max) {
            int toFetch = Math.min(40, max - fetched);
            String url = "https://www.googleapis.com/books/v1/volumes?q=subject:fiction&maxResults=" + toFetch + "&startIndex=" + startIndex;

            String response = restTemplate.getForObject(url, String.class);

            try {
                JsonNode root = objectMapper.readTree(response);
                JsonNode items = root.path("items");
                if (items == null || !items.isArray()) break;

                for (JsonNode item : items) {
                    if (fetched >= max) break;
                    JsonNode volumeInfo = item.path("volumeInfo");
                    Book book = new Book();

                    // Title

                    book.setTitle(volumeInfo.path("title").asText("Unknown Title"));

                    // ISBN
                    JsonNode industryIdentifiers = volumeInfo.path("industryIdentifiers");
                    if (industryIdentifiers.isArray() && industryIdentifiers.size() > 0) {
                        book.setIsbn(industryIdentifiers.get(0).path("identifier").asText("unknown-isbn-" + fetched));
                    } else {
                        book.setIsbn("unknown-isbn-" + fetched);
                    }

                    // Author
                    JsonNode authors = volumeInfo.path("authors");
                    if (authors.isArray() && authors.size() > 0) {
                        book.setAuthor(authors.get(0).asText("Unknown Author"));
                    } else {
                        book.setAuthor("Unknown Author");
                    }

                    // Publisher
                    book.setPublisher(volumeInfo.path("publisher").asText(null));

                    // Description
                    String shortDesc = volumeInfo.path("description").toString().length() > 1000 ?
                            volumeInfo.path("description").toString().substring(0, 1000)
                            : volumeInfo.path("description").toString();
                    book.setDescription(shortDesc);

                    // Page Count
                    book.setPageCount(volumeInfo.path("pageCount").asInt(0));

                    // Published Year
                    String publishedDate = volumeInfo.path("publishedDate").asText();
                    if (!publishedDate.isEmpty()) {
                        try {
                            book.setPublishedYear(Integer.parseInt(publishedDate.substring(0, 4)));
                        } catch (Exception ignored) {
                            book.setPublishedYear(null);
                        }
                    } else {
                        book.setPublishedYear(null);
                    }

                    // Genre
                    JsonNode categories = volumeInfo.path("categories");
                    if (categories.isArray() && categories.size() > 0) {
                        book.setGenre(categories.get(0).asText());
                    } else {
                        book.setGenre(null);
                    }

                    // Language
                    book.setLanguage(volumeInfo.path("language").asText(null));

                    // Cover Image URL
                    JsonNode imageLinks = volumeInfo.path("imageLinks");
                    if (imageLinks.has("thumbnail")) {
                        book.setCoverImageUrl(imageLinks.path("thumbnail").asText());
                    } else {
                        book.setCoverImageUrl(null);
                    }

                    books.add(book);
                    fetched++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

            startIndex += toFetch;
        }

        return books;
    }
}
