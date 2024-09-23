# WC EXAMPLE
# import sys
# from operator import add

# from pyspark.sql import SparkSession


# if __name__ == "__main__":
#     if len(sys.argv) != 2:
#         print("Usage: wordcount <file>", file=sys.stderr)
#         sys.exit(-1)

#     spark = SparkSession\
#         .builder\
#         .appName("PythonWordCount")\
#         .getOrCreate()

#     lines = spark.read.text(sys.argv[1]).rdd.map(lambda r: r[0])
#     counts = lines.flatMap(lambda x: x.split(' ')) \
#                   .map(lambda x: (x, 1)) \
#                   .reduceByKey(add)
#     output = counts.collect()
#     for (word, count) in output:
#         print("%s: %i" % (word, count))

#     spark.stop()

# ---------

# KAFKA CONSUMER EXAMPLE

from pyspark.sql import SparkSession
from pyspark.sql.functions import from_json, col

# Create a SparkSession
spark = SparkSession.builder\
    .appName("KafkaStream")\
    .getOrCreate()

# Define the Kafka topic and bootstrap server
kafka_topic = "games"
kafka_bootstrap_servers = "kafka:9093"

# Read from the Kafka topic
df = spark.readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", kafka_bootstrap_servers) \
    .option("subscribe", kafka_topic) \
    .load()

# Log every message to the console
df.selectExpr("CAST(value AS STRING)").writeStream \
    .outputMode("append") \
    .format("console") \
    .option("truncate", False) \
    .start() \
    .awaitTermination()