package NS_Lab2;

/**
 * Created by zhouxuexuan on 3/4/17.
 */

import java.awt.image.RenderedImage;
import java.lang.Object;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.BufferedImage;
import java.nio.*;
import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;


public class ImageEncryptionStartingCode {
    public static void main(String[] args) throws Exception{
        int image_width;
        int image_length;
        // read image file and save pixel value into int[][] imageArray
        File imgf = new File("/Users/zhouxuexuan/AndroidStudioProjects/Lab/lab/src/main/java/NS_Lab2/globe.bmp");
        BufferedImage img = ImageIO.read(imgf); // pass the image globe.bmp as first command-line argument.
        File outputfile = new File("/Users/zhouxuexuan/AndroidStudioProjects/Lab/lab/src/main/java/NS_Lab2/new2.bmp");
        //ImageIO.write(img, "bmp", outputfile);
        image_width = img.getWidth();
        image_length = img.getHeight();
        // byte[][] imageArray = new byte[image_width][image_length];
        int[][] imageArray = new int[image_width][image_length];
        for(int idx = 0; idx < image_width; idx++) {
            for(int idy = 0; idy < image_length; idy++) {
                int color = img.getRGB(idx, idy);
                imageArray[idx][idy] = color;
            }
        }
// TODO: generate secret key using DES algorithm
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(56);
        SecretKey secretKey = keyGen.generateKey();

// TODO: Create cipher object, initialize the ciphers with the given key, choose encryption algorithm/mode/padding,
//you need to try both ECB and CBC mode, use PKCS5Padding padding method
        Cipher desCipherE = Cipher.getInstance("DES/CBC/PKCS5Padding");
        desCipherE.init(Cipher.ENCRYPT_MODE,secretKey);
        //Cipher desCipherD = Cipher.getInstance("DES");
        //desCipherD.init(Cipher.DECRYPT_MODE,secretKey);
        // define output BufferedImage, set size and format
        BufferedImage outImage = new BufferedImage(image_width,image_length, BufferedImage.TYPE_3BYTE_BGR);

        for(int idx = 0; idx < image_width; idx++) {
            // convert each column int[] into a byte[] (each_width_pixel)
            byte[] each_width_pixel = new byte[4*image_length];
            for(int idy = 0; idy < image_length; idy++) {
                ByteBuffer dbuf = ByteBuffer.allocate(4);
                dbuf.putInt(imageArray[idx][idy]);
                byte[] bytes = dbuf.array();
                System.arraycopy(bytes, 0, each_width_pixel, idy*4, 4);
            }
// TODO: encrypt each column or row bytes
            byte[] encryptedImageBytes = desCipherE.doFinal(each_width_pixel);

// TODO: convert the encrypted byte[] back into int[] and write to outImage (use setRGB)
            byte[] encrypted_pixel = new byte[4];
            for(int idy = 0; idy < image_length; idy++) {
                System.arraycopy(encryptedImageBytes, idy*4, encrypted_pixel, 0, 4);
                ByteBuffer wrapped = ByteBuffer.wrap(encrypted_pixel);
                int newcolor = wrapped.getInt();
                outImage.setRGB(idx,idy,newcolor);
            }

        }
//write outImage into file
        ImageIO.write(outImage, "bmp", outputfile);//for the ECB mode output
    }
}

