CREATE
    TABLE
        ballot(
            id uuid PRIMARY KEY,
            poll_id uuid NOT NULL,
            client_identifier VARCHAR NOT NULL,
            ticket_id VARCHAR NOT NULL
        );

ALTER TABLE
    ballot ADD CONSTRAINT ballot_ticketId_uc UNIQUE(ticket_id);