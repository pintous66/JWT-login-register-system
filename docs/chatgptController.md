# Documentação dos Endpoints do ChatgptController

## Base URL
```
/gpt
```

## Endpoints

### 1. Perguntar ao ChatGPT

**Endpoint:**
```
GET /gpt/ask
```

**Descrição:**
Faz uma pergunta ao ChatGPT e retorna a resposta.

**Requisição:**
```json
{
  "query": "string"
}
```

**Respostas:**
- `200 OK` - Retorna a resposta do ChatGPT.
- `400 BAD REQUEST` - Erro na requisição.

**Exemplo de Resposta:**
```json
{
  "response": "Aqui está a resposta da IA..."
}
