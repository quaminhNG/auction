CREATE TABLE auctions (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    watch_id            UUID NOT NULL,
    seller_id           UUID NOT NULL,
    start_price         NUMERIC(12,2) NOT NULL,
    reserve_price       NUMERIC(12,2),          -- Giá tối thiểu để bán
    buy_now_price       NUMERIC(12,2),           -- Mua ngay
    current_highest_bid NUMERIC(12,2),
    highest_bidder_id   UUID,
    bid_count           INT DEFAULT 0,
    status              VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
        -- DRAFT → SCHEDULED → ACTIVE → ENDED → CANCELLED
    start_time          TIMESTAMP NOT NULL,
    end_time            TIMESTAMP NOT NULL,
    created_at          TIMESTAMP DEFAULT now(),
    updated_at          TIMESTAMP DEFAULT now(),
    version             INT NOT NULL DEFAULT 0   -- Optimistic locking
);

CREATE TABLE bids (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    auction_id  UUID NOT NULL REFERENCES auctions(id),
    bidder_id   UUID NOT NULL,
    amount      NUMERIC(12,2) NOT NULL,
    status      VARCHAR(20) DEFAULT 'ACTIVE',  -- ACTIVE, OUTBID, WINNER, CANCELLED
    ip_address  INET,
    created_at  TIMESTAMP DEFAULT now()
);

CREATE TABLE orders (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    auction_id      UUID REFERENCES auctions(id),
    buyer_id        UUID NOT NULL,
    watch_id        UUID NOT NULL,
    total_amount    NUMERIC(12,2) NOT NULL,
    status          VARCHAR(30) NOT NULL DEFAULT 'PENDING',
        -- PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED → CANCELLED
    idempotency_key VARCHAR(255) UNIQUE,       -- Chống đặt hàng 2 lần
    created_at      TIMESTAMP DEFAULT now(),
    updated_at      TIMESTAMP DEFAULT now()
);

-- Index tối ưu query tìm bids của một auction
CREATE INDEX idx_bids_auction_amount ON bids(auction_id, amount DESC);
CREATE INDEX idx_bids_bidder ON bids(bidder_id);
CREATE INDEX idx_auctions_status_time ON auctions(status, end_time);
