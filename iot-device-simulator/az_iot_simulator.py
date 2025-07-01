# azure_iot_simulator.py

import asyncio
import json
import random
from datetime import datetime, timezone, timedelta

# Azure IoT Hub Device SDK
from azure.iot.device.aio import IoTHubDeviceClient
from azure.iot.device import Message

# --- Azure IoT Hub Device Connection Strings ---
# IMPORTANT: Replace these with your actual device connection strings from Azure IoT Hub.
# Each device needs its own unique connection string.
# 총 10개의 장치에 대한 연결 문자열을 여기에 추가해야 합니다.
DEVICE_CONNECTION_STRINGS = [
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-001;SharedAccessKey=YOUR_DEVICE_KEY_1", # North-East
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-002;SharedAccessKey=YOUR_DEVICE_KEY_2", # North-West
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-003;SharedAccessKey=YOUR_DEVICE_KEY_3", # South-East
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-004;SharedAccessKey=YOUR_DEVICE_KEY_4", # South-West
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-005;SharedAccessKey=YOUR_DEVICE_KEY_5", # North Midpoint
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-006;SharedAccessKey=YOUR_DEVICE_KEY_6", # East Midpoint
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-007;SharedAccessKey=YOUR_DEVICE_KEY_7", # South Midpoint
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-008;SharedAccessKey=YOUR_DEVICE_KEY_8", # West Midpoint
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-009;SharedAccessKey=YOUR_DEVICE_KEY_9", # Center 1
    "HostName=YOUR_IOT_HUB_NAME.azure-devices.net;DeviceId=lake-iot-010;SharedAccessKey=YOUR_DEVICE_KEY_10", # Center 2
]

# --- Device Information (for generating messages) ---
# This information is used to populate the message payload, not directly by the SDK for connection.
DEVICE_INFO = {
    "lake-iot-001": {"location": "Lake Victoria North-East", "coordinates": "0.75,34.25"},
    "lake-iot-002": {"location": "Lake Victoria North-West", "coordinates": "0.75,33.25"},
    "lake-iot-003": {"location": "Lake Victoria South-East", "coordinates": "-0.75,34.25"},
    "lake-iot-004": {"location": "Lake Victoria South-West", "coordinates": "-0.75,33.25"},
    "lake-iot-005": {"location": "Lake Victoria North Midpoint", "coordinates": "0.75,33.75"},
    "lake-iot-006": {"location": "Lake Victoria East Midpoint", "coordinates": "0.00,34.25"},
    "lake-iot-007": {"location": "Lake Victoria South Midpoint", "coordinates": "-0.75,33.75"},
    "lake-iot-008": {"location": "Lake Victoria West Midpoint", "coordinates": "0.00,33.25"},
    "lake-iot-009": {"location": "Lake Victoria Center 1", "coordinates": "0.10,33.80"},
    "lake-iot-010": {"location": "Lake Victoria Center 2", "coordinates": "-0.10,33.70"},
}

# --- Simulation Settings ---
SEND_INTERVAL_SECONDS = 60 # 데이터 전송 간격 (초) - 1분으로 변경

async def generate_and_send_message(device_client: IoTHubDeviceClient, device_id: str):
    """
    IoT 메시지를 생성하고 Azure IoT Hub로 전송합니다.
    """
    # 현재 시간을 ISO 8601 형식으로 생성 (UTC)
    current_time_utc = datetime.now(timezone.utc)
    timestamp = current_time_utc.isoformat(timespec='seconds') + 'Z' # 'Z' for UTC

    # 온도 데이터 (예: 20.0 ~ 30.0 사이의 랜덤 값)
    temperature = round(random.uniform(20.0, 30.0), 2)

    # 메타 정보 (예시)
    meta_info = {
        "sensorType": "DS18B20",
        "unit": "Celsius",
        "firmwareVersion": "1.2.0"
    }

    # 장치 정보 가져오기
    device_details = DEVICE_INFO.get(device_id, {"location": "Unknown", "coordinates": "N/A"})

    message_payload = {
        "timestamp": timestamp,
        "deviceId": device_id,
        "location": device_details["location"],
        "coordinates": device_details["coordinates"],
        "temperature": temperature,
        "metaInfo": meta_info
    }

    # JSON 문자열로 변환
    json_message = json.dumps(message_payload)
    message = Message(json_message)

    # 메시지 전송
    try:
        await device_client.send_message(message)
        print(f"[{datetime.now().strftime('%H:%M:%S')}] Device {device_id}: Sent message: {json_message}")
    except Exception as e:
        print(f"[{datetime.now().strftime('%H:%M:%S')}] Device {device_id}: Error sending message: {e}")

async def run_device_simulator(connection_string: str):
    """
    단일 장치 시뮬레이터를 실행합니다.
    """
    client = IoTHubDeviceClient.create_from_connection_string(connection_string)
    await client.connect()
    print(f"Device {client.id} connected to IoT Hub.")

    while True:
        await generate_and_send_message(client, client.id)
        await asyncio.sleep(SEND_INTERVAL_SECONDS)

async def main():
    """
    모든 장치 시뮬레이터를 동시에 실행합니다.
    """
    print("Starting Azure IoT Device Simulators...")
    print("Ensure you have replaced placeholder connection strings with your actual ones.")
    print("Press Ctrl+C to stop.")

    tasks = []
    for conn_str in DEVICE_CONNECTION_STRINGS:
        # Extract device ID from connection string for logging purposes
        device_id_part = [part for part in conn_str.split(';') if 'DeviceId=' in part]
        device_id = device_id_part[0].split('=')[1] if device_id_part else "UnknownDevice"
        
        # Check if device_id is in DEVICE_INFO, if not, add a placeholder
        if device_id not in DEVICE_INFO:
            DEVICE_INFO[device_id] = {"location": "Dynamically Added", "coordinates": "0.0,0.0"}

        tasks.append(run_device_simulator(conn_str))
    
    # Run all device simulators concurrently
    await asyncio.gather(*tasks)

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        print("\nSimulators stopped by user.")
    except Exception as e:
        print(f"An error occurred: {e}")