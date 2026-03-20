package com.learning.books_api.entities;

import java.io.Serial;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Book {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private Category category;
    private Publisher publisher;
    private Instant launchDate;

    private Set<Author> authors = new HashSet<>();

    public Book(Long id, String title, Category category, Publisher publisher, Instant launchDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.publisher = publisher;
        this.launchDate = launchDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Instant getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Instant launchDate) {
        this.launchDate = launchDate;
    }

    public Set<Author> getAuthors() {
        return Set.copyOf(authors);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(Long id) {
        authors.remove(authors.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
