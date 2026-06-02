CREATE TABLE inventory (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    watch_id    UUID NOT NULL UNIQUE,
    sku         VARCHAR(100) NOT NULL UNIQUE,
    quantity    INT NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    reserved    INT NOT NULL DEFAULT 0 CHECK (reserved >= 0),
    version     INT NOT NULL DEFAULT 0,
    updated_at  TIMESTAMP DEFAULT now(),
    CONSTRAINT chk_reserved_lte_quantity CHECK (reserved <= quantity)
);

CREATE TABLE inventory_history (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    inventory_id    UUID NOT NULL REFERENCES inventory(id),
    watch_id        UUID NOT NULL,
    change_type     VARCHAR(30) NOT NULL,
    quantity_before INT NOT NULL,
    quantity_after  INT NOT NULL,
    reserved_before INT NOT NULL,
    reserved_after  INT NOT NULL,
    reason          TEXT,
    event_id        UUID,
    created_at      TIMESTAMP DEFAULT now()
);

-- Trigger function
CREATE OR REPLACE FUNCTION log_inventory_change()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.quantity <> NEW.quantity OR OLD.reserved <> NEW.reserved THEN
        INSERT INTO inventory_history (
            inventory_id, watch_id,
            change_type,
            quantity_before, quantity_after,
            reserved_before, reserved_after
        ) VALUES (
            NEW.id, NEW.watch_id,
            TG_ARGV[0],
            OLD.quantity, NEW.quantity,
            OLD.reserved, NEW.reserved
        );
    END IF;
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for inventory table (passing 'UPDATE' as argument for simplicity, or we can just let app layer pass the change type, but here we hardcode 'UPDATE' as default for trigger)
CREATE TRIGGER trg_inventory_history
BEFORE UPDATE ON inventory
FOR EACH ROW EXECUTE FUNCTION log_inventory_change('UPDATE');
