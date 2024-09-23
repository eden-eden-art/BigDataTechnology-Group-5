# Run

```bash
docker-compose up -d

cd spark_streamer
./start.sh # starts spark streaming ingestion
cd ..

cd streamy_boy
bun run index.ts # starts producing messages
```

# Pipeline

```
REST API 
    <- streamy_boy 
    ~> kafka 
    -:> kafka_eater(spark stream ; spark sql) 
    ~> mongo cluster 
    <- aggro (kafka_stats?)
    <- zeppelin
```