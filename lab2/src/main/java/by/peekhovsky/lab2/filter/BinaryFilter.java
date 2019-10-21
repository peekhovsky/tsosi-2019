package by.peekhovsky.lab2.filter;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class BinaryFilter implements Filter {

  @Override
  public BufferedImage filter(BufferedImage image) {
    int height = image.getHeight();
    int width = image.getWidth();

    BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = newImage.createGraphics();
    g.drawImage(image, 0, 0, null);

    for (int y = 1; y < height - 1; y++) {
      for (int x = 1; x < width - 1; x++) {
        List<Integer> pix = new ArrayList<>(9);
        pix.add(image.getRGB(x - 1, y - 1));
        pix.add(image.getRGB(x - 1, y));
        pix.add(image.getRGB(x - 1, y + 1));
        pix.add(image.getRGB(x, y - 1));
        pix.add(image.getRGB(x, y));
        pix.add(image.getRGB(x, y + 1));
        pix.add(image.getRGB(x + 1, y - 1));
        pix.add(image.getRGB(x + 1, y));
        pix.add(image.getRGB(x + 1, y + 1));
        pix.sort(Integer::compareTo);
        newImage.setRGB(x, y, pix.get(4));
      }
    }
    return image;
  }
}