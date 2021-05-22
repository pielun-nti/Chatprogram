package models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Do the base64 logic here.
 */
public class Base64Helper {
    /**
     * Base64 constructor.
     */
    public Base64Helper(){

    }
    /**
     *Encode Image to base64 string.
     * @param image BufferedImage
     * @param type Image type like png or jpg
     * @return String with base64 of the image
     */
    public String encodeImageToBase64String(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
            imageString = java.util.Base64.getEncoder().encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    /**
     * Decode base64 string to image
     * @param imageString The base64 string to convert to image
     * @return BufferedImage
     */
    public BufferedImage decodeBase64StringToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = java.util.Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
}
