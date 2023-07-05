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
    poll_instance(
    id,
    poll_id,
    slug
) SELECT
      gen_random_uuid(),
      id,
      'test-run'
FROM
    poll
WHERE
        poll.slug = 'jakarta-mvc';