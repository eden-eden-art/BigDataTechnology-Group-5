from pyspark.sql import SparkSession

def main():
    # Create a SparkSession
    spark = SparkSession.builder.appName("Word Count App").getOrCreate()

    # Load the /etc/passwd file into an RDD
    lines = spark.sparkContext.textFile("file:///etc/passwd")

    # Split each line into words and count the occurrences of each word
    word_counts = lines.flatMap(lambda line: line.split()).map(lambda word: (word, 1)).reduceByKey(lambda a, b: a + b)

    # Print the word counts
    word_counts.foreach(print)

    # Stop the SparkSession
    spark.stop()

if __name__ == "__main__":
    main()
