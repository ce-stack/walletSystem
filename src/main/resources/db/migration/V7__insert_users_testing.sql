INSERT INTO public.users (id, email, name, password, created_at)

VALUES
    (1, 'amir_test_one@gmail.com', 'Amir wallet one', '123456', now()),
    (2, 'amir_test_two@gmail.com', 'Amir wallet two', '123456', now())
    ON CONFLICT (id) DO NOTHING;