BEGIN;
SET LOCAL search_path = public;
ALTER TABLE categories DROP CONSTRAINT IF EXISTS chk_predefined_category;

COMMIT;
