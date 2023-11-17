package com.kernel360.orury.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JasyptConfigTest {
    @Test
    void stringEncryptor() {
        String url = "";
        String username = "";
        String password = "";
        String jwt = "";
        String log = "";

        System.out.println(jasyptEncoding(url));
        System.out.println(jasyptEncoding(username));
        System.out.println(jasyptEncoding(password));
        System.out.println(jasyptEncoding(jwt));
        System.out.println(jasyptEncoding(log));
    }

    public String jasyptEncoding(String value) {

        String key = "orury-password";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        pbeEnc.setPassword(key);
        pbeEnc.setIvGenerator(new RandomIvGenerator());
        return pbeEnc.encrypt(value);
    }
}