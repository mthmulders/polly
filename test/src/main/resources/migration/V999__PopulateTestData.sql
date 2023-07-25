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
