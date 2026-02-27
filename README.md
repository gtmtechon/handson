# hands-on Projects

This directory contains several sample applications and utilities used for hands-on labs. Each subfolder hosts a different project or supporting materials.

## Subdirectories

- **be-comm**: A Java Spring Boot microservice demonstrating device communication and IoT monitoring. Includes the `iotmon` module with controllers, services, repositories, and entities for managing devices and water bot states. Build with Maven.

- **docs**: Documentation resources such as APIM configuration and nginx examples used across labs.

- **iot-device-simulator**: Python-based simulator (`az_iot_simulator.py`) for generating IoT device telemetry.

- **iot-temperature-monitor**: Full stack application divided into two parts:
  - **backend/temperaturemonitor**: Spring Boot service for tracking device temperatures. Built with Maven.
  - **frontend**: Vue.js application providing a UI for registering and monitoring devices. Install dependencies via `npm install` and run with `npm run serve`.

- **labs**: Contains lab exercises and associated materials.

- **package**: Likely contains packaged resources or templates.

## Common Build & Run

### Java Services

Navigate into a service directory (e.g., `be-comm/iotmon` or `iot-temperature-monitor/iot-temperature-monitor-backend/temperaturemonitor`) and run:
```bash
./mvnw clean package
./mvnw spring-boot:run
```

### Vue Frontend

```bash
cd iot-temperature-monitor/iot-temperature-monitor-frontend
npm install
npm run serve
```

### Python Simulator

```bash
python3 az_iot_simulator.py
```

## Notes

- `.gitignore` is present to filter out build artifacts and IDE files.
- Lab instructions located under `labs`.

Feel free to explore each subproject for details and updates.
