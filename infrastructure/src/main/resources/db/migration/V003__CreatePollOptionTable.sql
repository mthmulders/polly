CREATE
    TABLE
        poll_option(
            id uuid PRIMARY KEY,
            poll_id uuid NOT NULL,
            display_value VARCHAR NOT NULL
        );
