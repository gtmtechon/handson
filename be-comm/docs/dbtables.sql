
GRANT ALL PRIVILEGES ON DATABASE gtmdb TO gtmtech;
-- waterbot, devices, temperature_readings, waterbot_status

-- 장비 정보 테이블
CREATE TABLE devices (
    device_id VARCHAR(255) PRIMARY KEY,
    device_name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 온도 데이터 테이블
CREATE TABLE temperature_readings (
    reading_id SERIAL PRIMARY KEY,
    device_id VARCHAR(255) REFERENCES devices(device_id) ON DELETE CASCADE,
    temperature NUMERIC(5, 2) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 추가 (조회 성능 향상)
CREATE INDEX idx_temperature_readings_device_id ON temperature_readings (device_id);
CREATE INDEX idx_temperature_readings_recorded_at ON temperature_readings (recorded_at);



--[robot]

CREATE TABLE IF not EXISTS waterbot (
    botId VARCHAR(128) PRIMARY KEY,
    botName  VARCHAR(150),
    productionDate VARCHAR(15),
    owner VARCHAR(150),
    botType VARCHAR(20),
    isWorking VARCHAR(10)
);


CREATE TABLE IF not EXISTS waterbot_status (
    botId VARCHAR(128) PRIMARY KEY,
    botName  VARCHAR(150),
    location VARCHAR(255),
    locationCooSys VARCHAR(20),
    status VARCHAR(20),
    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



---------------------------------------------------------
-- =========================================================
select * from temperature_readings;

select * from devices;



INSERT INTO public.devices (device_id,device_name,"location",registered_at) VALUES
	 ('72a52ee4866844c3a2b4badae994ed87','model-gen2','seoul-gangnam','2025-06-18 16:09:33.349248'),
	 ('8bdca40f6fc2449d942c7195457a6011','tes4123132','seokchon-x02-y03','2025-06-30 03:18:06.402365');


INSERT INTO public.temperature_readings (device_id,temperature,recorded_at) VALUES
	 ('72a52ee4866844c3a2b4badae994ed87',20.10,'2025-06-18 16:04:43.133272'),
	 ('72a52ee4866844c3a2b4badae994ed87',20.30,'2025-06-18 16:05:43.133272'),
	 ('72a52ee4866844c3a2b4badae994ed87',20.50,'2025-06-18 16:06:43.133272'),
	 ('72a52ee4866844c3a2b4badae994ed87',20.70,'2025-06-18 16:07:43.133272'),
	 ('72a52ee4866844c3a2b4badae994ed87',20.90,'2025-06-18 16:08:43.133272');


ALTER USER gtmtech WITH ENCRYPTED PASSWORD 'gtm123';



drop table current_device_state;

INSERT INTO public.device_state (id,temperature,location) values('8bdca40f6fc2449d942c7195457a6011',23.5,'songpa-222');

commit;

select * from current_device_state;

select * from devices;

