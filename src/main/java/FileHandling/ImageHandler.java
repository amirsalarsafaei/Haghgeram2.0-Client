package FileHandling;

import Utils.Convertors;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.Configuration;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class ImageHandler {
    public static ImageView getImage(String property) {
        try {
            InputStream input = new FileInputStream("./config/ImageLinks.properties");
            java.util.Properties properties = new Properties();
            properties.load(input);
            input.close();
            FileInputStream inputImage = new FileInputStream(properties.getProperty(property));
            Image image = new Image(inputImage);
            ImageView imageView = new ImageView(image);
            inputImage.close();
            return imageView;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void saveImage(File file, int id, String type) {

        try {
            File dest = new File(FileHandler.loadLocation(type) + id);
            Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static ImageView loadImage(int id, String type) {
        FileInputStream inputImage = null;
        try {
            inputImage = new FileInputStream(FileHandler.loadLocation(type) + id);
            Image image = new Image(inputImage);
            ImageView imageView = new ImageView(image);
            inputImage.close();
            return imageView;
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return null;
    }

    public static ImageView getUserProfileImage(byte[] imageByte) {
        if (imageByte == null) {
            return getImage("no-profile");
        }
        return new ImageView(Convertors.load(imageByte));
    }

}
