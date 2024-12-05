package com.example.demo;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class KafkaConsumerDemo {

    public static void main(String[] args) {
//        // Kafka consumer properties
//        Properties properties = new Properties();
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        // Create a Kafka consumer
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
//
//        // Subscribe to the topic
//        //consumer.subscribe(Collections.singletonList("test_topic"));
//        consumer.subscribe(Pattern.compile("topic_.*"));
//        // Poll for new data
//        try {
//            while (true) {
//                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//                for (ConsumerRecord<String, String> record : records) {
//                    System.out.println("Received record: \n" +
//                            "Key: " + record.key() +
//                            "\nValue: " + record.value() +
//                            "\nPartition: " + record.partition() +
//                            "\nOffset: " + record.offset());
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error while consuming messages: " + e.getMessage());
//        } finally {
//            consumer.close(); // Close the consumer to release resources

    // Kafka consumer properties
    Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    // Create a Kafka consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    // Subscribe to topics that start with "topic_" followed by a year
        consumer.subscribe(Pattern.compile("topic_\\d{4}"));

    // Poll for new data
        try {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("Received record: \n" +
                        "Key: " + record.key() +
                        "\nValue: " + record.value() +
                        "\nPartition: " + record.partition() +
                        "\nOffset: " + record.offset() +
                        "\nTopic: " + record.topic());
            }
        }
    } catch (Exception e) {
        System.err.println("Error while consuming messages: " + e.getMessage());
    } finally {
        consumer.close(); // Close the consumer to release resources
        }
    }
}
