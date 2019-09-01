
FROM node:10.16-alpine AS builder
WORKDIR /app
COPY . ./
RUN yarn
RUN yarn build:dev

FROM nginx:1.17-alpine
COPY --from=builder /app/public /usr/share/nginx/html
COPY nginx.conf /etc/nginx/
EXPOSE 9080
CMD ["nginx", "-g", "daemon off;"]