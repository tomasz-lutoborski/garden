FROM clojure:temurin-21-tools-deps AS builder
WORKDIR /app
COPY deps.edn .
RUN clojure -P
COPY src src
COPY posts posts
RUN clojure -X:uberjar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/garden.jar .
COPY posts posts
EXPOSE 3000
CMD ["java", "-jar", "garden.jar"]
