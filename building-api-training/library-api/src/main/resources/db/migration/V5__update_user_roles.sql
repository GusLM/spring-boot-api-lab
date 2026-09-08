-- Adjusts the users table to match User.role: List<UserRole>.
-- Existing ordinal values are migrated to the corresponding enum value.

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_type t
        JOIN pg_namespace n ON n.oid = t.typnamespace
        WHERE t.typname = 'user_role'
          AND n.nspname = 'public'
    ) THEN
        CREATE TYPE public.user_role AS ENUM ('ADMIN', 'USER', 'GUEST');
    END IF;
END $$;

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
