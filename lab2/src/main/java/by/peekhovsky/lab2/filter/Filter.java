package by.peekhovsky.lab2.filter;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface Filter {
  BufferedImage filter(BufferedImage image);
}
