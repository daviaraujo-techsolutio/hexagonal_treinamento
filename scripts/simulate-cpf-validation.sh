#!/bin/bash

# ============================================
# Script para Simular Validação de CPF
# Envia mensagem para o tópico tp-cpf-validated
# ============================================

echo "============================================"
echo "   Simulador de Validação de CPF"
echo "============================================"
echo ""

# Verifica se o ID foi passado como parâmetro
if [ -z "$1" ]; then
    echo "Uso: $0 <customer_id> [cpf_valido: true/false]"
    echo ""
    echo "Exemplo:"
    echo "  $0 64f1234567890abcdef12345 true"
    echo ""

    # Tenta buscar o último cliente criado
    echo "Buscando último cliente no MongoDB..."
    CUSTOMER=$(docker exec mongodb mongosh -u root -p example --authenticationDatabase admin hexagonal --quiet --eval "JSON.stringify(db.customers.findOne({}, {sort: {_id: -1}}))" 2>/dev/null)

    if [ -n "$CUSTOMER" ] && [ "$CUSTOMER" != "null" ]; then
        echo "Último cliente encontrado:"
        echo "$CUSTOMER" | python3 -m json.tool 2>/dev/null || echo "$CUSTOMER"
        echo ""

        CUSTOMER_ID=$(echo "$CUSTOMER" | python3 -c "import sys, json; print(json.load(sys.stdin)['_id']['\$oid'])" 2>/dev/null)
        CUSTOMER_NAME=$(echo "$CUSTOMER" | python3 -c "import sys, json; print(json.load(sys.stdin)['name'])" 2>/dev/null)
        CUSTOMER_CPF=$(echo "$CUSTOMER" | python3 -c "import sys, json; print(json.load(sys.stdin)['cpf'])" 2>/dev/null)

        echo "Execute novamente com:"
        echo "  $0 $CUSTOMER_ID true"
    else
        echo "Nenhum cliente encontrado. Crie um cliente primeiro."
    fi
    exit 1
fi

CUSTOMER_ID=$1
IS_VALID_CPF=${2:-true}

# Busca dados do cliente
echo "Buscando dados do cliente: $CUSTOMER_ID"
CUSTOMER=$(docker exec mongodb mongosh -u root -p example --authenticationDatabase admin hexagonal --quiet --eval "JSON.stringify(db.customers.findOne({_id: ObjectId('$CUSTOMER_ID')}))" 2>/dev/null)

if [ -z "$CUSTOMER" ] || [ "$CUSTOMER" == "null" ]; then
    echo "❌ Cliente não encontrado!"
    exit 1
fi

CUSTOMER_NAME=$(echo "$CUSTOMER" | python3 -c "import sys, json; print(json.load(sys.stdin)['name'])" 2>/dev/null)
CUSTOMER_CPF=$(echo "$CUSTOMER" | python3 -c "import sys, json; print(json.load(sys.stdin)['cpf'])" 2>/dev/null)

echo "✓ Cliente encontrado: $CUSTOMER_NAME"
echo ""

# Monta a mensagem JSON
MESSAGE=$(cat <<EOF
{
  "id": "$CUSTOMER_ID",
  "name": "$CUSTOMER_NAME",
  "zipCode": "38400000",
  "cpf": "$CUSTOMER_CPF",
  "isValidCpf": $IS_VALID_CPF
}
EOF
)

echo "Enviando mensagem para tp-cpf-validated:"
echo "$MESSAGE" | python3 -m json.tool 2>/dev/null || echo "$MESSAGE"
echo ""

# Envia para o Kafka
echo "$MESSAGE" | docker exec -i kafka kafka-console-producer --bootstrap-server localhost:9092 --topic tp-cpf-validated 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✓ Mensagem enviada com sucesso!"
    echo ""
    echo "Aguarde alguns segundos e verifique o cliente:"
    echo "  curl http://localhost:8080/api/v1/customers/$CUSTOMER_ID"
else
    echo "❌ Erro ao enviar mensagem!"
fi
