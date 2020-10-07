package by.peekhovsky.tsosi.lab1.filter;


import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class NegativeFilter implements Filter {

  private static final int MAX_COLOR_VALUE = 255;

  @Override
  public BufferedImage filter(BufferedImage image) {
    int height = image.getHeight();
    int width = image.getWidth();

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int rgb = image.getRGB(x, y);
        Color color = new Color(rgb);

        int red = MAX_COLOR_VALUE - color.getRed();
        int green = MAX_COLOR_VALUE - color.getGreen();
        int blue = MAX_COLOR_VALUE - color.getBlue();

        newImage.setRGB(x, y, new Color(red, green, blue).getRGB());
      }
    }
    return newImage;
  }
}
