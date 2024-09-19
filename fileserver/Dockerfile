FROM oven/bun

WORKDIR /app

COPY . .

RUN bun i

VOLUME [ "data/" ]

ENTRYPOINT [ "bun", "run", "/app/index.ts" ]