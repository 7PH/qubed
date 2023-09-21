FROM node:18-alpine

WORKDIR /frontend

COPY package.json package.json

RUN npm install

COPY . .

EXPOSE 8000

CMD [ "npm", "run", "dev" ]
