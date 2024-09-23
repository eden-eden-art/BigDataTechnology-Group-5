from pyspark.sql import SparkSession
from kafka import KafkaConsumer
from pymongo import MongoClient
import json

spark = SparkSession\
    .builder\
    .appName("kafka_stats")\
    .getOrCreate()

df = spark \
  .read \
  .format("mongodb") \
  .option("checkpointLocation", "/tmp/pyspark/") \
  .option("forceDeleteTempCheckpointLocation", "true") \
  .option("spark.mongodb.connection.uri", 'mongodb://mongo:27017/') \
  .option("spark.mongodb.database", 'data') \
  .option("spark.mongodb.collection", 'songs') \
  .load()

# df.show()
df.createOrReplaceTempView("temp")

# get avg lengths
avg = spark.sql('SELECT year, avg(duration) as average_duration from temp group by year order by year')

avg.write.format("mongodb") \
  .mode("append") \
  .option("spark.mongodb.connection.uri", 'mongodb://mongo:27017/') \
  .option("spark.mongodb.database", 'data') \
  .option("spark.mongodb.collection", 'durations') \
  .save()

# get avg words in titles
avgwords = spark.sql('SELECT year, AVG(LENGTH(name))AS average_titlelength FROM temp GROUP BY year ORDER BY year')

avgwords.write.format("mongodb") \
  .mode("append") \
  .option("spark.mongodb.connection.uri", 'mongodb://mongo:27017/') \
  .option("spark.mongodb.database", 'data') \
  .option("spark.mongodb.collection", 'titlelengths') \
  .save()

# get titles top words
topwords = spark.sql("SELECT TRIM(word) AS word, COUNT(*) AS count FROM (SELECT EXPLODE(SPLIT(name, ' ')) AS word FROM temp) AS exploded_words WHERE word <> '' GROUP BY word ORDER BY count DESC LIMIT 25")

topwords.write.format("mongodb") \
  .mode("append") \
  .option("spark.mongodb.connection.uri", 'mongodb://mongo:27017/') \
  .option("spark.mongodb.database", 'data') \
  .option("spark.mongodb.collection", 'titleheatmap') \
  .save()

# get top artists
topartists = spark.sql("SELECT TRIM(artist) AS artist, COUNT(*) AS count FROM (SELECT EXPLODE(SPLIT(artists, ',')) AS artist FROM temp) AS exploded_artists WHERE artist <> '' GROUP BY artist ORDER BY count DESC LIMIT 25")

topartists.write.format("mongodb") \
  .mode("append") \
  .option("spark.mongodb.connection.uri", 'mongodb://mongo:27017/') \
  .option("spark.mongodb.database", 'data') \
  .option("spark.mongodb.collection", 'popularartists') \
  .save()

# all.show()


# query = df.start()


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
