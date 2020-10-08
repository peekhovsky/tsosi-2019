package by.peekhovsky.tsosi.lab1.util;


import java.awt.image.BufferedImage;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageUtils {
  private static final Random random = new Random();

  public static Image convertToFXImage(BufferedImage image) {
    WritableImage wr = null;
    if (image != null) {
      wr = new WritableImage(image.getWidth(), image.getHeight());
      PixelWriter pw = wr.getPixelWriter();
      for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
          pw.setArgb(x, y, image.getRGB(x, y));
        }
      }
    }
    return new ImageView(wr).getImage();
  }

  public static Random getRandom() {
    return random;
  }
}
