CREATE
    TABLE
        vote(
            id uuid PRIMARY KEY,
            poll_id uuid NOT NULL,
            option_id uuid NOT NULL,
            ballot_id uuid NOT NULL,
            FOREIGN KEY(poll_id) REFERENCES poll(id),
            FOREIGN KEY(option_id) REFERENCES poll_option(id),
            FOREIGN KEY(ballot_id) REFERENCES ballot(id)
        );

ALTER TABLE
    ballot ADD COLUMN used_at TIMESTAMP;