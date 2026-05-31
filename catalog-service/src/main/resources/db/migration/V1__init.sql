CREATE TABLE brands (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    logo_url    VARCHAR(500),
    created_at  TIMESTAMP DEFAULT now()
);

CREATE TABLE categories (
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name      VARCHAR(100) NOT NULL,
    parent_id UUID REFERENCES categories(id),
    slug      VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE watches (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    brand_id        UUID NOT NULL REFERENCES brands(id),
    category_id     UUID REFERENCES categories(id),
    model_number    VARCHAR(100) NOT NULL,
    model_name      VARCHAR(255) NOT NULL,
    description     TEXT,
    base_price      NUMERIC(12,2),
    status          VARCHAR(20) DEFAULT 'ACTIVE',
    specifications  JSONB NOT NULL DEFAULT '{}',
    images          JSONB NOT NULL DEFAULT '[]',
    created_at      TIMESTAMP DEFAULT now(),
    updated_at      TIMESTAMP DEFAULT now()
);

-- GIN index cho tìm kiếm trong JSONB
CREATE INDEX idx_watches_specs ON watches USING GIN (specifications);

-- B-tree index cho các filter thông thường
CREATE INDEX idx_watches_brand ON watches(brand_id);
CREATE INDEX idx_watches_status ON watches(status);
