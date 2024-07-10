package com.diego.sha;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Main {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;    
    public static List<String> hashes = new ArrayList<>();
    public static final String email = "daardibujos@gmail.com";
    
    public static void main(String[] args){
        String algorithm = "SHA3-256";
        
        try {
            Path dir = Paths.get("/home/xubuntu/Downloads/task2");            
            
            Files.list(dir).forEach(file -> {                
                if (Files.isRegularFile(file)) {                    
                    try {
                        byte[] sha3Hash = calculateSHA3_256(file.toFile());
                        hashes.add(bytesToHex(sha3Hash));
                        //System.out.println(file.getFileName() + " : " + bytesToHex(sha3Hash));                        
                    } catch (IOException | NoSuchAlgorithmException e) {
                        System.err.println("Error processing file " + file.getFileName() + ": " + e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
        }
        
        String concat = "";
        Collections.sort(hashes);
        for (String hash : hashes) {
            concat += hash;
        }
        concat += email;
        byte[] concatInBytes = digest(concat.getBytes(UTF_8), algorithm);
        String result  = bytesToHex(concatInBytes);
        System.out.println(result);
        
        //Hash result
        //9ccf0952e529e9b6ec00b896cd83bd8bae0654facaa849410df770779a38029f
    }
    
    public static byte[] digest(byte[] input, String algorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }
    
    private static byte[] calculateSHA3_256(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }
        return digest.digest();
    }

    private static String bytesToHex(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString().toLowerCase();
        }
    }
}
