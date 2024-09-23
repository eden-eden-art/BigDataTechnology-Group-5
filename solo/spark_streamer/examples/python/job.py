from pyspark.sql import SparkSession
from kafka import KafkaConsumer
from pymongo import MongoClient
import json

spark = SparkSession\
    .builder\
    .appName("kafka_eater")\
    .getOrCreate()

kafka_topic = "games"
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
  .option("spark.mongodb.collection", 'games') \
  .outputMode("append")

query = df.start().awaitTermination()

# client = MongoClient('mongodb://mongo:27017/')
# db = client['data']
# games_collection = db['games']

# if 'games' not in db.list_collection_names():
#     games_collection = db.create_collection('games')

# consumer = KafkaConsumer("games", bootstrap_servers='kafka:9093', auto_offset_reset='earliest')

# for msg in consumer:
#     record = json.loads(msg.value)
#     games_collection.insert_one(record)

# client.close()

# spark.close()
