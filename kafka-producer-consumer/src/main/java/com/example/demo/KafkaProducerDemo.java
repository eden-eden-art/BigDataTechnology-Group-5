package com.example.demo;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.util.*;



import java.util.Properties;

public class KafkaProducerDemo {

//    public static void main(String[] args) {
//
//        // Kafka producer properties
//        Properties properties = new Properties();
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//
//        // Create a Kafka producer
//        Producer<String, String> producer = new KafkaProducer<>(properties);
//
//        // Create a producer record
//        ProducerRecord<String, String> record = new ProducerRecord<>("test_topic", "key", "Hello Kafka!");
//
//        // Send data - asynchronous
//        producer.send(record, new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata metadata, Exception e) {
//                if (e == null) {
//                    System.out.println("Record sent successfully with metadata: \n" +
//                            "Partition: " + metadata.partition() +
//                            "\nOffset: " + metadata.offset());
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // Flush and close producer
//        producer.flush();
//        producer.close();
//    }






//        private static final String BOOTSTRAP_SERVERS = "localhost:9092";
//
//        public static void main(String[] args) {
//
//            // Kafka producer properties
//            Properties producerProperties = new Properties();
//            producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//            producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//            producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//
//            Producer<String, String> producer = new KafkaProducer<>(producerProperties);
//
//            // Admin client to manage topics
//            Properties adminProperties = new Properties();
//            adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//            AdminClient adminClient = AdminClient.create(adminProperties);
//
//            // Assuming the zip file path and extraction directory are provided
//            String zipFilePath = "D:\\Desktop\\download.zip";
//            String extractionDirectory = "D:\\Desktop\\ExtractedDataFiles";
//
//            try {
//                // Unzip the file and get a list of files
//                List<File> unzippedFiles = FileUtil.unzip(zipFilePath, extractionDirectory);
//
//                for (File file : unzippedFiles) {
//                    // Extract the topic key (e.g., year) from the file
//                    String topicKey = FileUtil.extractKeyFromFile(file.getAbsolutePath());
//                    String topicName = "topic_" + topicKey;  // Create a topic name based on the key
//
//                    // Check if the topic exists and create if it doesn't
//                    if (!topicExists(adminClient, topicName)) {
//                        createTopic(adminClient, topicName, 1, (short) 1);
//                        System.out.println("Created topic: " + topicName);
//                    }
//
//                    // Read the data from the file and send to the corresponding Kafka topic
//                    List<String> fileData = FileUtil.readFile(file.getAbsolutePath());
//                    for (String line : fileData) {
//                        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, topicKey, line);
//                        producer.send(record, (metadata, exception) -> {
//                            if (exception == null) {
//                                System.out.println("Record sent to topic " + metadata.topic() +
//                                        " partition " + metadata.partition() +
//                                        " with offset " + metadata.offset());
//                            } else {
//                                exception.printStackTrace();
//                            }
//                        });
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                producer.flush();
//                producer.close();
//                adminClient.close();
//            }
//        }
//
//        private static boolean topicExists(AdminClient adminClient, String topicName) {
//            ListTopicsResult topics = adminClient.listTopics();
//            KafkaFuture<Set<String>> names = topics.names();
//            try {
//                return names.get().contains(topicName);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        private static void createTopic(AdminClient adminClient, String topicName, int numPartitions, short replicationFactor) {
//            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
//            CreateTopicsResult result = adminClient.createTopics(Collections.singleton(newTopic));
//            try {
//                result.all().get();
//            } catch (Exception e) {
//                e.printStackTrace();

//    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
//
//    public static void main(String[] args) {
//
//        // Kafka producer properties
//        Properties producerProperties = new Properties();
//        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//
//        Producer<String, String> producer = new KafkaProducer<>(producerProperties);
//
//        // Admin client to manage topics
//        Properties adminProperties = new Properties();
//        adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//        AdminClient adminClient = AdminClient.create(adminProperties);
//
//        //the zip file path and extraction directory are provided
//        String zipFilePath = "D:\\Desktop\\download.zip";
//        String extractionDirectory = "D:\\Desktop\\ExtractedDataFiles";
//
//        try {
//            // Unzip the file and get a list of files
//            List<File> unzippedFiles = FileUtil.unzip(zipFilePath, extractionDirectory);
//
//            for (File file : unzippedFiles) {
//                // Extract the topic key (year) from the file
//                String topicKey = FileUtil.extractKeyFromFile(file.getAbsolutePath());
//                // Extract year from the topic key
//                String year = extractYearFromFileName(file.getName());
//                if (year == null) {
//                    System.err.println("No valid year found in file name: " + file.getName());
//                    continue;
//                }
//
//                String topicName = "topic_" + year;  // Create a topic name based on the year
//
//                // Check if the topic exists and create if it doesn't
//                if (!topicExists(adminClient, topicName)) {
//                    createTopic(adminClient, topicName, 1, (short) 1);
//                    System.out.println("Created topic: " + topicName);
//                }
//
//                // Read the data from the file and send to the corresponding Kafka topic
//                List<String> fileData = FileUtil.readFile(file.getAbsolutePath());
//                for (String line : fileData) {
//                    ProducerRecord<String, String> record = new ProducerRecord<>(topicName, topicKey, line);
//                    producer.send(record, (metadata, exception) -> {
//                        if (exception == null) {
//                            System.out.println("Record sent to topic " + metadata.topic() +
//                                    " partition " + metadata.partition() +
//                                    " with offset " + metadata.offset());
//                        } else {
//                            exception.printStackTrace();
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            producer.flush();
//            producer.close();
//            adminClient.close();
//        }
//    }
//
//    private static boolean topicExists(AdminClient adminClient, String topicName) {
//        ListTopicsResult topics = adminClient.listTopics();
//        KafkaFuture<Set<String>> names = topics.names();
//        try {
//            return names.get().contains(topicName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private static void createTopic(AdminClient adminClient, String topicName, int numPartitions, short replicationFactor) {
//        NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
//        CreateTopicsResult result = adminClient.createTopics(Collections.singleton(newTopic));
//        try {
//            result.all().get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String extractYearFromFileName(String fileName) {
//        //  file name has a pattern like data_2023.txt
//        // Modify the regex according to your file name pattern
//        String pattern = ".*_(\\d{4}).*";
//        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
//        java.util.regex.Matcher matcher = regex.matcher(fileName);
//        if (matcher.matches()) {
//            return matcher.group(1); // Return the year part
//        }
//        return null; // If no year found, return null
//            }
//        }




        private static final String BOOTSTRAP_SERVERS = "localhost:9092";

        public static void main(String[] args) {

            // Kafka producer properties
            Properties producerProperties = new Properties();
            producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
            producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            Producer<String, String> producer = new KafkaProducer<>(producerProperties);

            // Admin client to manage topics
            Properties adminProperties = new Properties();
            adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
            AdminClient adminClient = AdminClient.create(adminProperties);

            // The zip file path and extraction directory are provided
            String zipFilePath = "D:\\Desktop\\download.zip";
            String extractionDirectory = "D:\\Desktop\\ExtractedDataFiles";

            try {
                // Unzip the file and get a list of files
                List<File> unzippedFiles = FileUtil.unzip(zipFilePath, extractionDirectory);

                for (File file : unzippedFiles) {
                    // Extract the topic key (year) from the file
                    String topicKey = FileUtil.extractKeyFromFile(file.getAbsolutePath());
                    System.out.println("Extracted topic key: " + topicKey); // Log the extracted topic key

                    // Extract year from the topic key
                    String year = extractYearFromFileName(file.getName());
                    if (year == null) {
                        System.err.println("No valid year found in file name: " + file.getName());
                        continue; // Skip this file if no valid year is found
                    }

                    String topicName = "topic_" + year;  // Create a topic name based on the year
                    System.out.println("Topic name to be used: " + topicName); // Log the topic name

                    // Check if the topic exists and create it if it doesn't
                    if (!topicExists(adminClient, topicName)) {
                        createTopic(adminClient, topicName, 1, (short) 1);
                        System.out.println("Created topic: " + topicName);
                    }

                    // Read the data from the file and send to the corresponding Kafka topic
                    List<String> fileData = FileUtil.readFile(file.getAbsolutePath());
                    if (fileData == null || fileData.isEmpty()) {
                        System.err.println("No data found in file: " + file.getName());
                        continue; // Skip if the file data is empty or null
                    }

                    for (String line : fileData) {
                        if (topicKey == null || line == null) {
                            System.err.println("Invalid record: Key or Value is null. Key: " + topicKey + ", Value: " + line);
                            continue; // Skip if key or value is null
                        }

                        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, topicKey, line);
                        producer.send(record, (metadata, exception) -> {
                            if (exception == null) {
                                System.out.println("Record sent to topic " + metadata.topic() +
                                        " partition " + metadata.partition() +
                                        " with offset " + metadata.offset());
                            } else {
                                System.err.println("Error sending record to topic " + metadata.topic() + ": " + exception.getMessage());
                                exception.printStackTrace();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                producer.flush();
                producer.close();
                adminClient.close();
            }
        }

        private static boolean topicExists(AdminClient adminClient, String topicName) {
            ListTopicsResult topics = adminClient.listTopics();
            KafkaFuture<Set<String>> names = topics.names();
            try {
                return names.get().contains(topicName);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private static void createTopic(AdminClient adminClient, String topicName, int numPartitions, short replicationFactor) {
            NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
            CreateTopicsResult result = adminClient.createTopics(Collections.singleton(newTopic));
            try {
                result.all().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static String extractYearFromFileName(String fileName) {
            // File name has a pattern like data_2023.txt

            String pattern = ".*_(\\d{4}).*";
            java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher matcher = regex.matcher(fileName);
            if (matcher.matches()) {
                return matcher.group(1); // Return the year part
            }
            return null; // If no year found, return null
        }
    }




