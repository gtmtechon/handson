import pandas as pd
import numpy as np

# 모의 데이터 생성
np.random.seed(42)
temperatures = np.linspace(10, 30, 100).round(1) # 10도에서 30도까지 100개 데이터
# 수온이 높을수록 산소 공급 단계가 높아지는 경향 (+노이즈)
optimal_oxygen_levels = np.clip(
    (temperatures - 10) / 2 + np.random.normal(0, 0.5, 100), 
    1, 10
).round().astype(int)

data = pd.DataFrame({
    'temperature_celsius': temperatures,
    'optimal_oxygen_level': optimal_oxygen_levels
})

# 일부 데이터에 대한 이상치 또는 특정 규칙 추가 (선택 사항)
data.loc[data['temperature_celsius'] > 28, 'optimal_oxygen_level'] = np.clip(
    data['optimal_oxygen_level'] + 1, 1, 10
)
data.loc[data['temperature_celsius'] < 12, 'optimal_oxygen_level'] = np.clip(
    data['optimal_oxygen_level'] - 1, 1, 10
)

data.to_csv('data/water_temp_data.csv', index=False)
print("water_temp_data.csv 생성 완료:")
print(data.head())