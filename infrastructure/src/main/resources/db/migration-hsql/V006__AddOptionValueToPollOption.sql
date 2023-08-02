-- No need to bother about migrating data, there will not be any.
-- This script is only applied to in-memory databases.
ALTER TABLE
    poll_option ADD COLUMN option_value INTEGER NOT NULL;

ALTER TABLE
    poll_option ADD CONSTRAINT poll_option_value_uc UNIQUE(
        id,
        option_value
    );