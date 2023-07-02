CREATE
    TABLE
        poll(
            id uuid PRIMARY KEY,
            question VARCHAR NOT NULL,
            slug VARCHAR NOT NULL
        );

CREATE
    TABLE
        poll_instance(
            id uuid PRIMARY KEY,
            poll_id uuid NOT NULL,
            slug VARCHAR NOT NULL
        );

ALTER TABLE
    poll_instance ADD CONSTRAINT poll_instance_poll_fk FOREIGN KEY(poll_id) REFERENCES poll(id);
