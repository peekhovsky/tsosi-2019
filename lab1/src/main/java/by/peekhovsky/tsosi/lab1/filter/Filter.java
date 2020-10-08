package by.peekhovsky.tsosi.lab1.filter;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface Filter {
  BufferedImage filter(BufferedImage image, Integer value);
}
