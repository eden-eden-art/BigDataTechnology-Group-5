package com.example.demo;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ListTopicsResult;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KafkaTopicChecker {
    public static void main(String[] args) {
//        Properties properties = new Properties();
//        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//
//        try (AdminClient adminClient = AdminClient.create(properties)) {
//            ListTopicsResult topics = adminClient.listTopics();
//            Set<String> topicNames = topics.names().get();
//            System.out.println("Topics in Kafka:");
//            topicNames.forEach(System.out::println);
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//}

        // Kafka AdminClient properties
//        Properties properties = new Properties();
//        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your Kafka broker address if different
//
//        // Create AdminClient
//        try (AdminClient adminClient = AdminClient.create(properties)) {
//            // List all topics in the Kafka cluster
//            ListTopicsResult topics = adminClient.listTopics();
//            Set<String> topicNames = topics.names().get();
//
//            // Create a map to group topics by year
//            Map<String, List<String>> topicsByYear = new HashMap<>();
//            Pattern pattern = Pattern.compile("topic_(\\d{4})");
//
//            for (String topic : topicNames) {
//                Matcher matcher = pattern.matcher(topic);
//                if (matcher.find()) {
//                    String year = matcher.group(1); // Extract year
//                    topicsByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(topic);
//                } else {
//                    topicsByYear.computeIfAbsent("undefined", k -> new ArrayList<>()).add(topic);
//                }
//            }
//
//            // Print topics grouped by year
//            for (Map.Entry<String, List<String>> entry : topicsByYear.entrySet()) {
//                System.out.println("Year: " + entry.getKey());
//                entry.getValue().forEach(System.out::println);
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
        final String BOOTSTRAP_SERVERS = "localhost:9092";
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        try (AdminClient adminClient = AdminClient.create(properties)) {
            ListTopicsResult topics = adminClient.listTopics();
            Set<String> topicNames = topics.names().get();

            // Map to group topics by year
            Map<String, List<String>> topicsByYear = new TreeMap<>(); // TreeMap to keep years in sorted order

            // Group topics by year
            for (String topicName : topicNames) {
                String year = extractYearFromTopicName(topicName);
                if (year != null) {
                    topicsByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(topicName);
                }
            }

            // Display topics grouped by year
            System.out.println("Topics in Kafka grouped by year:");
            for (Map.Entry<String, List<String>> entry : topicsByYear.entrySet()) {
                System.out.println("Year: " + entry.getKey());
                for (String topic : entry.getValue()) {
                    System.out.println("\tTopic: " + topic);
                }
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // Extract year from topic name, the format is 'topic_2023'
    private static String extractYearFromTopicName(String topicName) {
        String pattern = "topic_(\\d{4})";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(topicName);
        if (matcher.matches()) {
            return matcher.group(1); // Return the year part
        }
        return null; // If no year found, return null
        }
    }

