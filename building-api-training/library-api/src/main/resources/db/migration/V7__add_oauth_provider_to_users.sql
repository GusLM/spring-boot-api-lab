ALTER TABLE public.users
    ALTER COLUMN password DROP NOT NULL;

ALTER TABLE public.users
    ADD COLUMN auth_provider VARCHAR(50),
    ADD COLUMN provider_id VARCHAR(150);

ALTER TABLE public.users
    ADD CONSTRAINT uk_users_auth_provider_provider_id
        UNIQUE (auth_provider, provider_id);