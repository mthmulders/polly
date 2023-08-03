INSERT
    INTO
        poll(
            id,
            question,
            slug
        )
    VALUES(
        gen_random_uuid(),
        'Do you like Jakarta MVC?',
        'jakarta-mvc'
    );

INSERT
    INTO
        poll_option(
            id,
            poll_id,
            display_value,
            option_value
        ) SELECT
            gen_random_uuid(),
            id,
            'Yes!',
            1
        FROM
            poll
        WHERE
            slug = 'jakarta-mvc';

INSERT
    INTO
        poll_option(
            id,
            poll_id,
            display_value,
            option_value
        ) SELECT
            gen_random_uuid(),
            id,
            'Of course!',
            2
        FROM
            poll
        WHERE
            slug = 'jakarta-mvc';
