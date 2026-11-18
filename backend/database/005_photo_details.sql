BEGIN;
SET LOCAL search_path = public;

ALTER TABLE photos ADD COLUMN IF NOT EXISTS title varchar(100);
ALTER TABLE photos ADD COLUMN IF NOT EXISTS description varchar(2000);

COMMIT;
