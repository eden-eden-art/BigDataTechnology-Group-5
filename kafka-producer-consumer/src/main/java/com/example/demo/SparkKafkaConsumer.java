package com.example.demo;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.Row;

import java.util.concurrent.TimeoutException;

public class SparkKafkaConsumer {

    public static void main(String[] args) throws TimeoutException {

        // Create a Spark session
        SparkSession spark = SparkSession.builder()
                .appName("SparkKafkaConsumer")
                .master("local[*]") // Use "local[*]" to run Spark locally with as many worker threads as logical cores on your machine.
                .getOrCreate();

        // Set log level to ERROR to reduce the amount of logs displayed in the console
        spark.sparkContext().setLogLevel("ERROR");

        // Read streaming data from Kafka
        Dataset<Row> kafkaData = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092") // Replace with your Kafka broker address
                .option("subscribePattern", "topic_\\d{4}") // Subscribe to all topics that start with "topic_" followed by a year
                .option("startingOffsets", "earliest") // Read data from the beginning
                .load();

        // Extract key and value as strings
        Dataset<Row> data = kafkaData.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

        // Display the streaming data in the console
        StreamingQuery query = data
                .writeStream()
                .format("console") // Output format can be "console" or any other supported format like "parquet", "json", etc.
                .option("truncate", "false") // Prevent truncation of long strings in the output
                .trigger(Trigger.ProcessingTime("10 seconds")) // Process data every 10 seconds
                .start();

        try {
            query.awaitTermination(); // Wait for the streaming query to finish
        } catch (StreamingQueryException e) {
            e.printStackTrace();
        }
    }
}
