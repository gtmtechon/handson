Azure Storage & OpenAI 기초 인프라 생성 (CLI)

이 가이드는 RAG 시스템 구축에 필요한 핵심 리소스를 Azure CLI를 통해 생성하는 순서를 담고 있습니다. 모든 리소스는 한국 중부(koreacentral) 리전을 기준으로 생성됩니다.

1. 기본 설정 및 리소스 그룹 생성

가장 먼저 모든 리소스를 담을 바구니인 리소스 그룹을 생성합니다.

# 변수 설정
RESOURCE_GROUP="rg-gov-chatbot"
LOCATION="koreacentral" # 한국 중부 리전 고정

# 리소스 그룹 생성
az group create --name $RESOURCE_GROUP --location $LOCATION


2. Azure Storage Account & Container 생성

PDF 파일을 업로드하고 Azure Functions 트리거의 소스가 될 저장소를 만듭니다.

STORAGE_NAME="stgovhandbook$(date +%s)" # 고유 이름 생성

# 스토리지 계정 생성 (LRS: 로컬 중복 저장)
az storage account create \
    --name $STORAGE_NAME \
    --resource-group $RESOURCE_GROUP \
    --location $LOCATION \
    --sku Standard_LRS \
    --kind StorageV2

# 연결 문자열 가져오기 (나중에 Function App 설정에 사용)
STORAGE_CONNECTION_STRING=$(az storage account show-connection-string --name $STORAGE_NAME --resource-group $RESOURCE_GROUP --query connectionString -o tsv)

# 컨테이너 생성 (파일 업로드용)
az storage container create \
    --name "handbook-data" \
    --account-name $STORAGE_NAME \
    --connection-string "$STORAGE_CONNECTION_STRING"

echo "Storage Connection String: $STORAGE_CONNECTION_STRING"


3. Azure OpenAI 리소스 생성

AI 모델을 배포할 Cognitive Services 계정을 생성합니다.

AOAI_NAME="aoai-gov-expert"

# OpenAI 리소스 생성
az cognitiveservices account create \
    --name $AOAI_NAME \
    --resource-group $RESOURCE_GROUP \
    --location $LOCATION \
    --kind OpenAI \
    --sku S0 \
    --custom-subdomain $AOAI_NAME

# 엔드포인트 및 키 확인
AOAI_ENDPOINT=$(az cognitiveservices account show --name $AOAI_NAME --resource-group $RESOURCE_GROUP --query "properties.endpoint" -o tsv)
AOAI_KEY=$(az cognitiveservices account keys list --name $AOAI_NAME --resource-group $RESOURCE_GROUP --query "key1" -o tsv)

echo "OpenAI Endpoint: $AOAI_ENDPOINT"
echo "OpenAI Key: $AOAI_KEY"


4. 모델 배포 (Model Deployment)

생성한 OpenAI 리소스 안에 실제 사용할 모델 2개를 배포합니다.

# 1. GPT-4o 배포 (답변 생성용)
az cognitiveservices account deployment create \
    --name $AOAI_NAME \
    --resource-group $RESOURCE_GROUP \
    --deployment-name "gpt-4o" \
    --model-name "gpt-4o" \
    --model-version "2024-05-13" \
    --model-format "OpenAI" \
    --sku-capacity 10 \
    --sku-name "Standard"

# 2. Text Embedding 모델 배포 (벡터화용)
az cognitiveservices account deployment create \
    --name $AOAI_NAME \
    --resource-group $RESOURCE_GROUP \
    --deployment-name "text-embedding-3-small" \
    --model-name "text-embedding-3-small" \
    --model-version "1" \
    --model-format "OpenAI" \
    --sku-capacity 20 \
    --sku-name "Standard"


💡 생성 후 확인할 체크리스트

[ ] Storage Connection String: Azure Functions의 AzureWebJobsStorage 및 커스텀 연결 정보에 입력해야 합니다.

[ ] AOAI Endpoint & Key: function_app.py 환경 변수에 등록해야 합니다.

[ ] Region 가용성: koreacentral 리전은 현재 gpt-4o 및 text-embedding-3-small 모델을 지원합니다. 다만, 특정 시점의 Quota(할당량) 상황에 따라 생성이 제한될 수 있으니 Azure Portal의 'Quota' 탭을 미리 확인하세요.
