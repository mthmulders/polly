CREATE
    TABLE
        temp_polls(
            id uuid PRIMARY KEY,
            question VARCHAR NOT NULL,
            slug VARCHAR NOT NULL
        );

INSERT
    INTO
        temp_polls(
            id,
            question,
            slug
        ) SELECT
            pi.id,
            p.question,
            p.slug || '-' || pi.slug
        FROM
            poll_instance pi
        JOIN poll p ON
            p.id = pi.poll_id;

DELETE
FROM
    poll;

DROP
    TABLE
        poll_instance;

INSERT
    INTO
        poll(
            id,
            question,
            slug
        ) SELECT
            id,
            question,
            slug
        FROM
            temp_polls;

DROP
    TABLE
        temp_polls;