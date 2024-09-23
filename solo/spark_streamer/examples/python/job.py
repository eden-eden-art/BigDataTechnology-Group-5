from pyspark.sql import SparkSession
from kafka import KafkaConsumer
from pymongo import MongoClient
import json

spark = SparkSession\
    .builder\
    .appName("kafka_eater")\
    .getOrCreate()

kafka_topic = "songs" # use for collection name
kafka_bootstrap_servers = "kafka:9093"

df = spark \
  .readStream \
  .format("kafka") \
  .option("kafka.bootstrap.servers", kafka_bootstrap_servers) \
  .option("subscribe", kafka_topic) \
  .load() \
  .writeStream \
  .format("mongodb") \
  .option("checkpointLocation", "/tmp/pyspark/") \
  .option("forceDeleteTempCheckpointLocation", "true") \
  .option("spark.mongodb.connection.uri", 'mongodb://mongo:27017/') \
  .option("spark.mongodb.database", 'data') \
  .option("spark.mongodb.collection", kafka_topic) \
  .outputMode("append")

query = df.start().awaitTermination()

