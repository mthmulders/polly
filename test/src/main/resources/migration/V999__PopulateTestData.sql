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
            display_value
        ) SELECT
            gen_random_uuid(),
            id,
            'Yes!'
        FROM
            poll
        WHERE
            slug = 'jakarta-mvc';

INSERT
    INTO
        poll_option(
            id,
            poll_id,
            display_value
        ) SELECT
            gen_random_uuid(),
            id,
            'Of course!'
        FROM
            poll
        WHERE
            slug = 'jakarta-mvc';
