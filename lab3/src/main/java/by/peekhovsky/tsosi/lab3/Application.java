package by.peekhovsky.tsosi.lab3;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

  public static void main(String[] args) {
    runAnalyzer();
  }

  private static void runAnalyzer() {
    List<Letters> letters = Arrays.stream(Letters.values()).collect(Collectors.toList());
    HopfieldAnalyzer hopfield = new HopfieldAnalyzerImpl(
      letters.stream().map(Letters::getLetters).collect(Collectors.toList()),
      100);

    letters.forEach(value -> {
      Integer[] crashedImage;
      System.out.println("\n\n" + value);
      for (int i = 10; i <= 100; i += 10) {
        System.out.println("\n" + i + "%");
        crashedImage = hopfield.crashImage(value.getLetters(), i);
        showLetters(crashedImage);
        System.out.println();
        showLetters(hopfield.findImage(crashedImage));
      }
    });
  }

  private static void showLetters(Integer[] letter) {
    for (int i = 0; i < letter.length; i++) {
      if (i != 0 && i % 10 == 0) {
        System.out.println();
      }
      if (letter[i] == -1) {
        System.out.print(" _ ");
      } else {
        System.out.print(" # ");
      }
    }
    System.out.println();
  }
}
