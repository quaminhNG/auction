-- Insert Brands
INSERT INTO brands (id, name, description) VALUES
(gen_random_uuid(), 'Rolex', 'Swiss luxury watch manufacturer based in Geneva.'),
(gen_random_uuid(), 'Omega', 'Swiss luxury watchmaker based in Biel/Bienne.'),
(gen_random_uuid(), 'Patek Philippe', 'Swiss luxury watch and clock manufacturer.'),
(gen_random_uuid(), 'Seiko', 'Japanese maker of watches, clocks, electronic devices.'),
(gen_random_uuid(), 'Casio', 'Japanese multinational electronics manufacturing corporation.'),
(gen_random_uuid(), 'Audemars Piguet', 'Swiss manufacturer of luxury mechanical watches and clocks.');

-- Insert Categories
INSERT INTO categories (id, name, slug) VALUES
(gen_random_uuid(), 'Dress Watch', 'dress-watch'),
(gen_random_uuid(), 'Diver', 'diver'),
(gen_random_uuid(), 'Chronograph', 'chronograph'),
(gen_random_uuid(), 'Pilot', 'pilot');

-- Insert 20 Watches with diverse JSONB Specifications
INSERT INTO watches (brand_id, category_id, model_number, model_name, base_price, specifications) VALUES 

-- Rolex (Diver & Chronograph)
((SELECT id FROM brands WHERE name='Rolex'), (SELECT id FROM categories WHERE name='Diver'), '116610LN', 'Submariner Date', 10500.00, '{"movement_type": "Automatic", "dial_diameter_mm": 40, "glass": "Sapphire", "water_resistance_label": "300m / 30 ATM"}'),
((SELECT id FROM brands WHERE name='Rolex'), (SELECT id FROM categories WHERE name='Diver'), '126610LV', 'Submariner Kermit', 11500.00, '{"movement_type": "Automatic", "dial_diameter_mm": 41, "glass": "Sapphire", "water_resistance_label": "300m / 30 ATM"}'),
((SELECT id FROM brands WHERE name='Rolex'), (SELECT id FROM categories WHERE name='Chronograph'), '116500LN', 'Cosmograph Daytona', 14500.00, '{"movement_type": "Automatic", "dial_diameter_mm": 40, "glass": "Sapphire", "water_resistance_label": "100m / 10 ATM"}'),
((SELECT id FROM brands WHERE name='Rolex'), (SELECT id FROM categories WHERE name='Dress Watch'), '126234', 'Datejust 36', 8500.00, '{"movement_type": "Automatic", "dial_diameter_mm": 36, "glass": "Sapphire", "water_resistance_label": "100m / 10 ATM"}'),

-- Omega
((SELECT id FROM brands WHERE name='Omega'), (SELECT id FROM categories WHERE name='Chronograph'), '311.30.42.30.01.005', 'Speedmaster Moonwatch', 5350.00, '{"movement_type": "Manual", "dial_diameter_mm": 42, "glass": "Acrylic", "water_resistance_label": "50m"}'),
((SELECT id FROM brands WHERE name='Omega'), (SELECT id FROM categories WHERE name='Diver'), '210.30.42.20.01.001', 'Seamaster Diver 300M', 5200.00, '{"movement_type": "Automatic", "dial_diameter_mm": 42, "glass": "Sapphire", "water_resistance_label": "300m / 30 ATM"}'),
((SELECT id FROM brands WHERE name='Omega'), (SELECT id FROM categories WHERE name='Dress Watch'), '434.13.40.20.02.001', 'De Ville Prestige', 4200.00, '{"movement_type": "Automatic", "dial_diameter_mm": 40, "glass": "Sapphire", "water_resistance_label": "30m / 3 ATM"}'),
((SELECT id FROM brands WHERE name='Omega'), (SELECT id FROM categories WHERE name='Diver'), '233.30.41.21.01.001', 'Seamaster 300', 6500.00, '{"movement_type": "Automatic", "dial_diameter_mm": 41, "glass": "Sapphire", "water_resistance_label": "300m / 30 ATM"}'),

-- Patek Philippe
((SELECT id FROM brands WHERE name='Patek Philippe'), (SELECT id FROM categories WHERE name='Dress Watch'), '5196G', 'Calatrava', 22500.00, '{"movement_type": "Manual", "dial_diameter_mm": 37, "glass": "Sapphire", "water_resistance_label": "30m / 3 ATM"}'),
((SELECT id FROM brands WHERE name='Patek Philippe'), (SELECT id FROM categories WHERE name='Dress Watch'), '5227J', 'Calatrava Date', 34500.00, '{"movement_type": "Automatic", "dial_diameter_mm": 39, "glass": "Sapphire", "water_resistance_label": "30m / 3 ATM"}'),
((SELECT id FROM brands WHERE name='Patek Philippe'), (SELECT id FROM categories WHERE name='Chronograph'), '5711/1A', 'Nautilus', 35000.00, '{"movement_type": "Automatic", "dial_diameter_mm": 40, "glass": "Sapphire", "water_resistance_label": "120m"}'),
((SELECT id FROM brands WHERE name='Patek Philippe'), (SELECT id FROM categories WHERE name='Chronograph'), '5980/1AR', 'Nautilus Chronograph', 65000.00, '{"movement_type": "Automatic", "dial_diameter_mm": 40, "glass": "Sapphire", "water_resistance_label": "120m"}'),

-- Seiko
((SELECT id FROM brands WHERE name='Seiko'), (SELECT id FROM categories WHERE name='Diver'), 'SKX007', 'SKX Diver', 350.00, '{"movement_type": "Automatic", "dial_diameter_mm": 42, "glass": "Mineral", "water_resistance_label": "200m"}'),
((SELECT id FROM brands WHERE name='Seiko'), (SELECT id FROM categories WHERE name='Dress Watch'), 'SARB033', 'Seiko Spirit', 500.00, '{"movement_type": "Automatic", "dial_diameter_mm": 38, "glass": "Sapphire", "water_resistance_label": "100m / 10 ATM"}'),
((SELECT id FROM brands WHERE name='Seiko'), (SELECT id FROM categories WHERE name='Pilot'), 'SND255', 'Flightmaster', 150.00, '{"movement_type": "Quartz", "dial_diameter_mm": 42, "glass": "Mineral", "water_resistance_label": "100m / 10 ATM"}'),
((SELECT id FROM brands WHERE name='Seiko'), (SELECT id FROM categories WHERE name='Chronograph'), 'SSC813', 'Prospex Speedtimer', 675.00, '{"movement_type": "Quartz", "dial_diameter_mm": 39, "glass": "Sapphire", "water_resistance_label": "100m / 10 ATM"}'),

-- Casio
((SELECT id FROM brands WHERE name='Casio'), (SELECT id FROM categories WHERE name='Dress Watch'), 'A158WA', 'Vintage Collection', 25.00, '{"movement_type": "Quartz", "dial_diameter_mm": 33, "glass": "Acrylic", "water_resistance_label": "30m / 3 ATM"}'),
((SELECT id FROM brands WHERE name='Casio'), (SELECT id FROM categories WHERE name='Diver'), 'MDV106', 'Duro', 50.00, '{"movement_type": "Quartz", "dial_diameter_mm": 44, "glass": "Mineral", "water_resistance_label": "200m"}'),
((SELECT id FROM brands WHERE name='Casio'), (SELECT id FROM categories WHERE name='Chronograph'), 'GA2100', 'G-Shock CasiOak', 99.00, '{"movement_type": "Quartz", "dial_diameter_mm": 45, "glass": "Mineral", "water_resistance_label": "200m"}'),

-- Audemars Piguet
((SELECT id FROM brands WHERE name='Audemars Piguet'), (SELECT id FROM categories WHERE name='Dress Watch'), '15202ST', 'Royal Oak Jumbo', 33000.00, '{"movement_type": "Automatic", "dial_diameter_mm": 39, "glass": "Sapphire", "water_resistance_label": "50m"}'),
((SELECT id FROM brands WHERE name='Audemars Piguet'), (SELECT id FROM categories WHERE name='Chronograph'), '26331ST', 'Royal Oak Chronograph', 38000.00, '{"movement_type": "Automatic", "dial_diameter_mm": 41, "glass": "Sapphire", "water_resistance_label": "50m"}');
