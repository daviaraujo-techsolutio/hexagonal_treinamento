#!/bin/bash

# ============================================
# Script de Testes - Customer API (Aula 26)
# Arquitetura Hexagonal
# ============================================

BASE_URL="http://localhost:8080/api/v1/customers"
CUSTOMER_ID=""

echo "============================================"
echo "       Customer API - Testes"
echo "============================================"
echo ""

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para imprimir resultado
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# ============================================
# 1. POST - Criar Cliente
# ============================================
echo -e "${YELLOW}1. Testando POST /api/v1/customers${NC}"
echo "   Criando cliente: Danilo, CEP: 38400000"

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Danilo",
    "zipCode": "38400000",
    "cpf": "12345678911"
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -eq 200 ]; then
    print_result 0 "POST - Cliente criado com sucesso (HTTP $HTTP_CODE)"
else
    print_result 1 "POST - Falha ao criar cliente (HTTP $HTTP_CODE)"
    echo "   Response: $BODY"
fi
echo ""

# ============================================
# 2. Buscar ID do cliente no MongoDB
# ============================================
echo -e "${YELLOW}2. Buscando ID do cliente no MongoDB${NC}"

CUSTOMER_ID=$(docker exec mongodb mongosh -u root -p example --authenticationDatabase admin hexagonal --quiet --eval "db.customers.findOne({name:'Danilo'})._id.toString()" 2>/dev/null | tr -d '"')

if [ -n "$CUSTOMER_ID" ]; then
    print_result 0 "ID encontrado: $CUSTOMER_ID"
else
    print_result 1 "Não foi possível encontrar o ID do cliente"
    exit 1
fi
echo ""

# ============================================
# 3. GET - Buscar Cliente por ID
# ============================================
echo -e "${YELLOW}3. Testando GET /api/v1/customers/{id}${NC}"

RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/$CUSTOMER_ID")
HTTP_CODE=$(echo "$RESPONSE" | tail -1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -eq 200 ]; then
    print_result 0 "GET - Cliente encontrado (HTTP $HTTP_CODE)"
    echo "   Response: $BODY"
else
    print_result 1 "GET - Falha ao buscar cliente (HTTP $HTTP_CODE)"
fi
echo ""

# ============================================
# 4. Verificar mensagem no Kafka
# ============================================
echo -e "${YELLOW}4. Verificando mensagem no Kafka (tp-cpf-validation)${NC}"

KAFKA_MSG=$(docker exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic tp-cpf-validation --from-beginning --timeout-ms 3000 2>/dev/null | tail -1)

if [ -n "$KAFKA_MSG" ]; then
    print_result 0 "Mensagem encontrada no Kafka: $KAFKA_MSG"
else
    print_result 1 "Nenhuma mensagem encontrada no Kafka"
fi
echo ""

# ============================================
# 5. PUT - Atualizar Cliente
# ============================================
echo -e "${YELLOW}5. Testando PUT /api/v1/customers/{id}${NC}"
echo "   Atualizando para: Sebastião, CEP: 38400001"

RESPONSE=$(curl -s -w "\n%{http_code}" -X PUT "$BASE_URL/$CUSTOMER_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sebastião",
    "zipCode": "38400001",
    "cpf": "12345678911"
  }')

HTTP_CODE=$(echo "$RESPONSE" | tail -1)

if [ "$HTTP_CODE" -eq 204 ]; then
    print_result 0 "PUT - Cliente atualizado com sucesso (HTTP $HTTP_CODE)"
else
    print_result 1 "PUT - Falha ao atualizar cliente (HTTP $HTTP_CODE)"
fi
echo ""

# ============================================
# 6. GET - Verificar atualização
# ============================================
echo -e "${YELLOW}6. Verificando atualização (GET)${NC}"

RESPONSE=$(curl -s "$BASE_URL/$CUSTOMER_ID")
echo "   Response: $RESPONSE"

if echo "$RESPONSE" | grep -q "Sebastião"; then
    print_result 0 "Nome atualizado para Sebastião"
else
    print_result 1 "Nome não foi atualizado"
fi

if echo "$RESPONSE" | grep -q "São Paulo"; then
    print_result 0 "Cidade atualizada para São Paulo"
else
    print_result 1 "Cidade não foi atualizada"
fi
echo ""

# ============================================
# 7. DELETE - Remover Cliente
# ============================================
echo -e "${YELLOW}7. Testando DELETE /api/v1/customers/{id}${NC}"

RESPONSE=$(curl -s -w "\n%{http_code}" -X DELETE "$BASE_URL/$CUSTOMER_ID")
HTTP_CODE=$(echo "$RESPONSE" | tail -1)

if [ "$HTTP_CODE" -eq 204 ]; then
    print_result 0 "DELETE - Cliente removido com sucesso (HTTP $HTTP_CODE)"
else
    print_result 1 "DELETE - Falha ao remover cliente (HTTP $HTTP_CODE)"
fi
echo ""

# ============================================
# 8. GET - Verificar remoção
# ============================================
echo -e "${YELLOW}8. Verificando remoção (GET após DELETE)${NC}"

RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/$CUSTOMER_ID")
HTTP_CODE=$(echo "$RESPONSE" | tail -1)

if [ "$HTTP_CODE" -eq 500 ]; then
    print_result 0 "Cliente não encontrado após DELETE (HTTP $HTTP_CODE - esperado)"
else
    print_result 1 "Comportamento inesperado (HTTP $HTTP_CODE)"
fi
echo ""

# ============================================
# Resumo
# ============================================
echo "============================================"
echo "       Testes Concluídos!"
echo "============================================"
