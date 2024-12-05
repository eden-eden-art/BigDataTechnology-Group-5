package com.example.demo;


import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class FileUtil {

    // Unzip a given zip file to a target directory
    public static List<File> unzip(String zipFilePath, String destDirectory) throws IOException {
        List<File> unzippedFiles = new ArrayList<>();
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    File unzippedFile = new File(filePath);
                    unzippedFiles.add(unzippedFile);
                    extractFile(zis, filePath);
                }
                zis.closeEntry();
                entry = zis.getNextEntry();
            }
        }
        return unzippedFiles;
    }

    // Helper method to extract a single file from the zip
    private static void extractFile(ZipInputStream zis, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[1024];
            int read = 0;
            while ((read = zis.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    // Read data from a file and return it as a list of strings
    public static List<String> readFile(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    // Extract a key from the file content, for example, the year
    public static String extractKeyFromFile(String filePath) throws IOException {
        // Implement the logic to extract the key (e.g., year) from the file
        // For this example, let's assume the file name contains the year as YYYY
        String fileName = new File(filePath).getName();
        return fileName.split("_")[0];  // Assuming the file name starts with the year
    }
}
