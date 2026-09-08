ALTER TABLE public.users
    ADD COLUMN email VARCHAR(150);

UPDATE public.users
SET email = CASE login
    WHEN 'admin' THEN 'admin@email.com'
    WHEN 'reader' THEN 'reader@email.com'
    ELSE concat(login, '@email.com')
END;

ALTER TABLE public.users
    ALTER COLUMN email SET NOT NULL;

ALTER TABLE public.users
    ADD CONSTRAINT uk_users_email UNIQUE (email);
