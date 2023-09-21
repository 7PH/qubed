FROM node:18-alpine
EXPOSE 8081

WORKDIR /frontend

COPY package*.json .

RUN npm install

COPY . .

RUN npm run build

CMD [ "./node_modules/.bin/http-server", "dist", "-p", "8081" ]
