ALTER TABLE
    poll_option ADD COLUMN option_value INTEGER;

WITH option_values AS(
    SELECT
        *,
        ROW_NUMBER() OVER(
            PARTITION BY poll_id
        ) AS rn
    FROM
        poll_option
) UPDATE
    poll_option
SET
    option_value =(
        SELECT
            rn
        FROM
            option_values
        WHERE
            option_values.id = poll_option.id
    );

ALTER TABLE
    poll_option ALTER COLUMN option_value
SET
    NOT NULL;

ALTER TABLE
    poll_option ADD CONSTRAINT poll_option_value_uc UNIQUE(
        id,
        option_value
    );