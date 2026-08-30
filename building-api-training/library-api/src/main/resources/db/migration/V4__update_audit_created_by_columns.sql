-- Updates the creation audit columns to reflect the entity mapping:
-- Author, Book, and BookGenre use @JoinColumn(name = "created_by")

ALTER TABLE public.authors
    RENAME COLUMN user_id TO created_by;

ALTER TABLE public.books
    RENAME COLUMN user_id TO created_by;

ALTER TABLE public.books_genres
    RENAME COLUMN user_id TO created_by;

ALTER TABLE public.authors
    ADD CONSTRAINT fk_authors_created_by
        FOREIGN KEY (created_by)
            REFERENCES public.users (id);

ALTER TABLE public.books
    ADD CONSTRAINT fk_books_created_by
        FOREIGN KEY (created_by)
            REFERENCES public.users (id);

ALTER TABLE public.books_genres
    ADD CONSTRAINT fk_books_genres_created_by
        FOREIGN KEY (created_by)
            REFERENCES public.users (id);

CREATE INDEX idx_authors_created_by
    ON public.authors (created_by);

CREATE INDEX idx_books_created_by
    ON public.books (created_by);

CREATE INDEX idx_books_genres_created_by
    ON public.books_genres (created_by);
