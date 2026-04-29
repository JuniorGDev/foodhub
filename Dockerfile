# Etapa 1: build
# Usa uma imagem do mavem para fazer o estágio de build do projeto e apelida o estágio de builder
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia tudo que esta na pasta do projeto (. é a referencia do local onde esta e como não tem pasta declarada (./home/alguem) ele considera tudo que vem depois de .) 
# para dentro do container
COPY . .

# Comando que executa o build dentro do container. Mantivo o -DskipTests por enquanto. 
# PS: Lembrar de tirar quando existiram testes. 
RUN mvn clean install -DskipTests

# Etapa 2: runtime (leve)
# Define a imagem mais limpa, sem bibliotecas desnecessárias.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia APENAS o .jar criado no estágio anterior para a segunda nomeando ele de "app.jar" 
COPY --from=builder /app/target/*.jar app.jar

# Documenta que a aplicação usa a porta 8080 mas mesmo assim precisa declarar ela no docker-compose.yml para funcionar, aqui serve apenas de documentação.
EXPOSE 8080

# Comando que usar o java para executar o app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]