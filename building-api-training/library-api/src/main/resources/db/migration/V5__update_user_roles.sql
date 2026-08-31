-- Adjusts the users table to match User.role: List<UserRole>.
-- Existing ordinal values are migrated to the corresponding enum value.

CREATE TYPE public.user_role AS ENUM ('ADMIN', 'USER', 'GUEST');

ALTER TABLE public.users
    ADD COLUMN roles public.user_role[];

UPDATE public.users
SET roles = CASE role
    WHEN 0 THEN ARRAY['ADMIN']::public.user_role[]
    WHEN 1 THEN ARRAY['USER']::public.user_role[]
    WHEN 2 THEN ARRAY['GUEST']::public.user_role[]
END;

ALTER TABLE public.users
    ALTER COLUMN roles SET NOT NULL;

ALTER TABLE public.users
    DROP COLUMN role;
