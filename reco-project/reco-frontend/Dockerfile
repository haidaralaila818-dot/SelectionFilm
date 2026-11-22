FROM node:20-alpine AS build
WORKDIR /build
COPY package.json ./
RUN npm install
COPY . .
RUN npx ng build

FROM nginx:alpine
COPY --from=build /build/dist/reco-frontend/browser /usr/share/nginx/html
EXPOSE 80
