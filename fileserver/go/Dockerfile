FROM golang

WORKDIR /app

COPY . .

RUN go build -o server .

ENTRYPOINT [ "/app/server" ]