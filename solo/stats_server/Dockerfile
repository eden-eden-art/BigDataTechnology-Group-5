FROM oven/bun

WORKDIR /app

COPY . .

RUN bun i

EXPOSE 3005

ENTRYPOINT [ "bun", "run", "index.ts" ]