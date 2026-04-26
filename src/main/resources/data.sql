-- Clear existing data (if any)
DELETE FROM ASSET;
DELETE FROM AUDIT_LOG;

-- Insert sample assets
INSERT INTO ASSET (ID, DEVICE_TYPE, SERIAL_NUMBER, MODEL_SPECIFICATIONS, OWNER_NAME, DEPARTMENT, CONDITION, ISSUE_DATE, RETURN_DATE, STATUS) VALUES 
(1, 'Laptop', 'SN001', 'Dell XPS 15', null, null, 'New', null, null, 'AVAILABLE'),
(2, 'Laptop', 'SN002', 'HP EliteBook', 'TUYISHIMIRE Fabrigas', 'IT', 'Good', CURRENT_DATE, null, 'ASSIGNED'),
(3, 'Projector', 'SN003', 'Epson EB-FH06', null, null, 'Good', null, null, 'AVAILABLE'),
(4, 'Phone', 'SN004', 'iPhone 14', 'NYIRABIZIMANA Consessa', 'Sales', 'New', CURRENT_DATE, null, 'ASSIGNED');

-- Insert audit log entries
INSERT INTO AUDIT_LOG (ID, ACTION, ASSET_ID, DETAILS, PERFORMED_BY, TIMESTAMP) VALUES 
(1, 'CREATE', 1, 'Asset created: SN001', 'system', CURRENT_TIMESTAMP),
(2, 'CREATE', 2, 'Asset created: SN002', 'system', CURRENT_TIMESTAMP),
(3, 'CREATE', 3, 'Asset created: SN003', 'system', CURRENT_TIMESTAMP),
(4, 'CREATE', 4, 'Asset created: SN004', 'system', CURRENT_TIMESTAMP),
(5, 'ISSUE', 2, 'Asset issued to TUYISHIMIRE Fabrigas (IT)', '24RP04278', CURRENT_TIMESTAMP),
(6, 'ISSUE', 4, 'Asset issued to NYIRABIZIMANA Consessa (Sales)', '24RP09087', CURRENT_TIMESTAMP);

-- Reset ID sequences (H2 specific)
ALTER TABLE ASSET ALTER COLUMN ID RESTART WITH 100;
ALTER TABLE AUDIT_LOG ALTER COLUMN ID RESTART WITH 100;
