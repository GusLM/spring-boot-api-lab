-- Seed data for local development and demonstrations.
-- This migration runs after V1 and V2, so the audit columns are still named user_id.
-- Mock credentials: admin/password and reader/password.
-- The password column stores the BCrypt hash of "password".

INSERT INTO public.users (public_id, login, password, role, created_at, updated_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 0, '2026-01-01 09:00:00', NULL),
    ('22222222-2222-2222-2222-222222222222', 'reader', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1, '2026-01-01 09:05:00', NULL);

INSERT INTO public.books_genres (public_id, genre, registered_at, updated_at, user_id)
VALUES
    ('33333333-3333-3333-3333-333333333331', 'Fantasia', '2026-01-01 10:00:00', NULL, (SELECT id FROM public.users WHERE public_id = '11111111-1111-1111-1111-111111111111')),
    ('33333333-3333-3333-3333-333333333332', 'Ficção científica', '2026-01-01 10:01:00', NULL, (SELECT id FROM public.users WHERE public_id = '11111111-1111-1111-1111-111111111111')),
    ('33333333-3333-3333-3333-333333333333', 'Romance', '2026-01-01 10:02:00', NULL, (SELECT id FROM public.users WHERE public_id = '22222222-2222-2222-2222-222222222222'));

INSERT INTO public.authors (public_id, first_name, last_name, birthdate, nationality, registered_at, updated_at, user_id)
VALUES
    ('44444444-4444-4444-4444-444444444441', 'J. R. R.', 'Tolkien', '1892-01-03', 'Britânica', '2026-01-01 10:10:00', NULL, (SELECT id FROM public.users WHERE public_id = '11111111-1111-1111-1111-111111111111')),
    ('44444444-4444-4444-4444-444444444442', 'Ursula', 'Le Guin', '1929-10-21', 'Americana', '2026-01-01 10:11:00', NULL, (SELECT id FROM public.users WHERE public_id = '11111111-1111-1111-1111-111111111111')),
    ('44444444-4444-4444-4444-444444444443', 'Jane', 'Austen', '1775-12-16', 'Britânica', '2026-01-01 10:12:00', NULL, (SELECT id FROM public.users WHERE public_id = '22222222-2222-2222-2222-222222222222'));

INSERT INTO public.books (public_id, isbn, title, publication_date, genre_id, registered_at, updated_at, user_id)
VALUES
    ('55555555-5555-5555-5555-555555555551', '9780261102385', 'O Senhor dos Anéis', '1954-07-29', (SELECT id FROM public.books_genres WHERE public_id = '33333333-3333-3333-3333-333333333331'), '2026-01-01 10:20:00', NULL, (SELECT id FROM public.users WHERE public_id = '11111111-1111-1111-1111-111111111111')),
    ('55555555-5555-5555-5555-555555555552', '9780441478125', 'A Mão Esquerda da Escuridão', '1969-03-01', (SELECT id FROM public.books_genres WHERE public_id = '33333333-3333-3333-3333-333333333332'), '2026-01-01 10:21:00', NULL, (SELECT id FROM public.users WHERE public_id = '22222222-2222-2222-2222-222222222222')),
    ('55555555-5555-5555-5555-555555555553', '9780141439518', 'Orgulho e Preconceito', '1813-01-28', (SELECT id FROM public.books_genres WHERE public_id = '33333333-3333-3333-3333-333333333333'), '2026-01-01 10:22:00', NULL, (SELECT id FROM public.users WHERE public_id = '22222222-2222-2222-2222-222222222222'));

INSERT INTO public.books_authors (book_id, author_id)
VALUES
    ((SELECT id FROM public.books WHERE public_id = '55555555-5555-5555-5555-555555555551'), (SELECT id FROM public.authors WHERE public_id = '44444444-4444-4444-4444-444444444441')),
    ((SELECT id FROM public.books WHERE public_id = '55555555-5555-5555-5555-555555555552'), (SELECT id FROM public.authors WHERE public_id = '44444444-4444-4444-4444-444444444442')),
    ((SELECT id FROM public.books WHERE public_id = '55555555-5555-5555-5555-555555555553'), (SELECT id FROM public.authors WHERE public_id = '44444444-4444-4444-4444-444444444443'));
