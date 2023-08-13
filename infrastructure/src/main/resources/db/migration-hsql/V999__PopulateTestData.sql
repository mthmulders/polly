INSERT
    INTO
        poll(
            id,
            question,
            slug
        )
    VALUES(
        uuid(),
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
            uuid(),
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
            uuid(),
            id,
            'Of course!',
            2
        FROM
            poll
        WHERE
            slug = 'jakarta-mvc';
