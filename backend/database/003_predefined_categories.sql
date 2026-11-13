BEGIN;
SET LOCAL search_path = public;

INSERT INTO categories (owner_id, name)
SELECT u.id, n.name FROM app_users u
CROSS JOIN (VALUES ('Potovanja'), ('Družina'), ('Prijatelji'), ('Dogodki'),
                   ('Narava'), ('Šport'), ('Vsakdan'), ('Drugo')) AS n(name)
ON CONFLICT DO NOTHING;

UPDATE categories c SET name = n.name
FROM (VALUES ('Potovanja'), ('Družina'), ('Prijatelji'), ('Dogodki'),
             ('Narava'), ('Šport'), ('Vsakdan'), ('Drugo')) AS n(name)
WHERE lower(c.name) = lower(n.name) AND c.name <> n.name;

UPDATE albums a SET category_id = other.id
FROM categories old, categories other
WHERE a.category_id = old.id AND other.owner_id = old.owner_id AND other.name = 'Drugo'
  AND old.name NOT IN ('Potovanja', 'Družina', 'Prijatelji', 'Dogodki', 'Narava', 'Šport', 'Vsakdan', 'Drugo');
DELETE FROM categories
WHERE name NOT IN ('Potovanja', 'Družina', 'Prijatelji', 'Dogodki', 'Narava', 'Šport', 'Vsakdan', 'Drugo');

ALTER TABLE categories DROP CONSTRAINT IF EXISTS chk_predefined_category;
ALTER TABLE categories ADD CONSTRAINT chk_predefined_category
    CHECK (name IN ('Potovanja', 'Družina', 'Prijatelji', 'Dogodki', 'Narava', 'Šport', 'Vsakdan', 'Drugo'));
COMMIT;
