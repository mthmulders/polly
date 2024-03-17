ALTER TABLE
    poll_option ADD FOREIGN KEY(poll_id) REFERENCES poll(id);

ALTER TABLE
    ballot ADD FOREIGN KEY(poll_id) REFERENCES poll(id);