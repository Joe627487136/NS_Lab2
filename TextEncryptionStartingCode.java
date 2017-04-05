package NS_Lab2;

/**
 * Created by zhouxuexuan on 3/4/17.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;

import sun.misc.BASE64Encoder;
//     /Users/zhouxuexuan/AndroidStudioProjects/Lab/lab/src/main/java/NS_Lab2


public class TextEncryptionStartingCode {
    public static void main(String[] args) throws Exception {
        String data = "";
        String data2 = "";
        String line;
        File smallfile = new File("/Users/zhouxuexuan/AndroidStudioProjects/Lab/lab/src/main/java/NS_Lab2/smallFile.txt");
        File bigfile = new File("/Users/zhouxuexuan/AndroidStudioProjects/Lab/lab/src/main/java/NS_Lab2/largeFile.txt");
        BufferedReader bufferedReader = new BufferedReader( new FileReader(smallfile));//args[0] is the file you are going to encrypt.
        while((line= bufferedReader.readLine())!=null){
            data = data +"\n" + line;
        }
        BufferedReader bufferedReader2 = new BufferedReader( new FileReader(bigfile));//args[0] is the file you are going to encrypt.
        while((line= bufferedReader2.readLine())!=null){
            data2 = data2 +"\n" + line;
        }

//TODO: Print to screen contents of the file
        //System.out.println(data);
//TODO: generate secret key using DES algorithm
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        SecretKey secretKey = keyGen.generateKey();
//TODO: create cipher object, initialize the ciphers with the given key, choose encryption mode as DES
        Cipher desCipherE = Cipher.getInstance("DES");
        desCipherE.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] ciphertextsmall = data.getBytes();
        byte[] ciphertextbig = data2.getBytes();
//TODO: do encryption, by calling method Cipher.doFinal().
        byte[] ciphertextEsmall = desCipherE.doFinal(ciphertextsmall);
        byte[] ciphertextEbig = desCipherE.doFinal(ciphertextbig);
//TODO: print the length of output encrypted byte[], compare the length of file smallSize.txt and largeSize.txt
        System.out.println("Encrypted small text: " + ciphertextEsmall.length);
        System.out.println("Encrypted large text: " + ciphertextEbig.length);
        System.out.println("Small file: " + data.length());
        System.out.println("Large file: " + data2.length());
//TODO: do format conversion. Turn the encrypted byte[] format into base64format String using DatatypeConverter
        String Encoded64Strsmall = new BASE64Encoder().encode(ciphertextEsmall);
        String Encoded64Strbig = new BASE64Encoder().encode(ciphertextEbig);
//TODO: print the encrypted message (in base64format String format)
        System.out.println(Encoded64Strsmall);
        System.out.println(Encoded64Strbig);
//TODO: create cipher object, initialize the ciphers with the given key, choose decryption mode as DES
        Cipher desCipherD = Cipher.getInstance("DES");
        desCipherD.init(Cipher.DECRYPT_MODE,secretKey);
//TODO: do decryption, by calling method Cipher.doFinal().
        byte[] ciphertextDsmall = desCipherD.doFinal(ciphertextEsmall);
        byte[] ciphertextDbig = desCipherD.doFinal(ciphertextEbig);
//TODO: do format conversion. Convert the decrypted byte[] to String, using "String a = new String(byte_array);"
        String nsmall = new String(ciphertextDsmall);
        String nbig = new String(ciphertextDbig);
        System.out.println("");
//TODO: print the decrypted String text and compare it with original text
        if (nsmall.equals(data)&&nbig.equals(data2)){
            System.out.println("Data Pass");
        }
        else {
            System.out.println("Different Byte found, Data Corrupted");
        }
    }
}
