INSERT INTO public.users (id, email, name, password, created_at)
VALUES (1, 'amir_test@gmail.com', 'Amir', '123456', now())
    ON CONFLICT (id) DO NOTHING;