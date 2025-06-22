from flask import Flask, render_template_string
import requests

app = Flask(__name__)

@app.route('/')
def index():
    # Open API 호출
    url = "https://iotmon-comm-be.azurewebsites.net/api/devices/selectAll"
    response = requests.get(url)
    data = response.json()

    # 예외 처리: 리스트 형태가 아니면 빈 리스트로
    if not isinstance(data, list):
        data = []

    # 컬럼 추출 (첫 row 기준)
    columns = data[0].keys() if data else []

    # HTML 템플릿 - 테이블 렌더링
    html_template = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>IoT Devices</title>
        <style>
            table { border-collapse: collapse; width: 100%; }
            th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
        </style>
    </head>
    <body>
        <h2>IoT Devices (총 {{ data|length }}건)</h2>
        <table>
            <thead>
                <tr>
                    {% for col in columns %}
                    <th>{{ col }}</th>
                    {% endfor %}
                </tr>
            </thead>
            <tbody>
                {% for row in data %}
                <tr>
                    {% for col in columns %}
                    <td>{{ row[col] }}</td>
                    {% endfor %}
                </tr>
                {% endfor %}
            </tbody>
        </table>
    </body>
    </html>
    """
    return render_template_string(html_template, data=data, columns=columns)

if __name__ == '__main__':
    app.run(debug=True)
