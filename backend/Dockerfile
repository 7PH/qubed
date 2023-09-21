FROM openjdk

WORKDIR /backend

COPY . .

RUN ./gradlew build

CMD [ "./gradlew", "run" ]
