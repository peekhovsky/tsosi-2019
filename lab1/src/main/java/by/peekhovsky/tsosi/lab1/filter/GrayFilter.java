package by.peekhovsky.tsosi.lab1.filter;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class GrayFilter implements Filter {

  public static final double RED_FACTOR = 0.3;
  public static final double GREEN_FACTOR = 0.59;
  public static final double BLUE_FACTOR = 0.11;

  @Override
  public BufferedImage filter(BufferedImage image, Integer value) {
    int height = image.getHeight();
    int width = image.getWidth();

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = image.getRGB(x, y);
        int medium = ((int) ((rgb >> 16 & 0xff) * RED_FACTOR)
            + (int) ((rgb >> 8 & 0xff) * GREEN_FACTOR)
            + (int) ((rgb & 0xff) * BLUE_FACTOR));
        Color color = new Color(medium, medium, medium);
        newImage.setRGB(x, y, color.getRGB());
      }
    }
    return newImage;
  }
}
