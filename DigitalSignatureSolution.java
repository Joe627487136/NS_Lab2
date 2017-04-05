package NS_Lab2;

/**
 * Created by zhouxuexuan on 3/4/17.
 */

import javax.xml.bind.DatatypeConverter;
import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import sun.misc.BASE64Encoder;

import static java.nio.charset.StandardCharsets.UTF_8;


public class DigitalSignatureSolution {

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

//TODO: generate a RSA keypair, initialize as 1024 bits, get public key and private key from this keypair.
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publickey = keyPair.getPublic();
        PrivateKey privatekey = keyPair.getPrivate();
//TODO: Calculate message digest, using MD5 hash function
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digests = md.digest(data.getBytes());
        byte[] digestb = md.digest(data2.getBytes());
//TODO: print the length of output digest byte[], compare the length of file smallSize.txt and largeSize.txt
        System.out.println("Digest small length: "+digests.length);
        System.out.println("Digest small : "+digests);
        System.out.println("Small file length: "+data.length());
        System.out.println("Digest big length: "+digestb.length);
        System.out.println("Digest big : "+digestb);
        System.out.println("Large file length: "+data2.length());
//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as encrypt mode, use PRIVATE key.
//TODO: encrypt digest message
//TODO: print the encrypted message (in base64format String using DatatypeConverter)
        String ciphertextS = encrypt(new String(digests),privatekey);
        System.out.println(ciphertextS);
        String ciphertextB = encrypt(new String(digestb),privatekey);
        System.out.println(ciphertextB);
//TODO: Create RSA("RSA/ECB/PKCS1Padding") cipher object and initialize is as decrypt mode, use PUBLIC key.
//TODO: decrypt message
        String decipheredMessageS = decrypt(ciphertextS,publickey);
        String decipheredMessageB = decrypt(ciphertextB,publickey);
//TODO: print the decrypted message (in base64format String using DatatypeConverter), compare with origin digest
        System.out.println(decipheredMessageS);
        System.out.println(decipheredMessageB);
        System.out.println(new String(digests));
        System.out.println(new String(digestb));
        if (decipheredMessageS.equals(new String(digests))&&decipheredMessageB.equals(new String(digestb))){
            System.out.println("Data Pass");
        }
        else {
            System.out.println("Different Byte found, Data Corrupted");
        }
    }
    public static String encrypt(String plainText, PrivateKey privateKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes());
        System.out.println("Signed bytes[] length: "+cipherText.length);
        System.out.println(cipherText);

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, PublicKey publicKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        //System.out.println("Signed bytes[] length: "+bytes.length);

        Cipher decriptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        decriptCipher.init(Cipher.DECRYPT_MODE, publicKey);

        return new String(decriptCipher.doFinal(bytes));
    }

}

