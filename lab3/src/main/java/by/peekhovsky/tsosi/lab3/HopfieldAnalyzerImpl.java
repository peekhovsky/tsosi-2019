package by.peekhovsky.tsosi.lab3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HopfieldAnalyzerImpl implements HopfieldAnalyzer {

  private final int size;
  private final Neuron[] neurons;
  private final int[][] weightMatrix;
  private final Random random = new Random();

  public HopfieldAnalyzerImpl(List<Integer[]> letters, int size) {
    this.size = size;
    neurons = new Neuron[size];

    weightMatrix = new int[size][size];

    for (var i = 0; i < neurons.length; i++) {
      neurons[i] = new Neuron(i);
    }

    letters.forEach(this::initCoefficients);
  }

  public Integer[] findImage(Integer[] img) {
    var times = 10000;
    var isImageRecognized = true;
    var recognizedImage = img.clone();
    var lastOutputValues = new int[size];

    while (!isImageRecognized || times-- > 0) {
      //  insert input values
      for (var i = 0; i < recognizedImage.length; i++) {
        neurons[i].setX(recognizedImage[i]);
        lastOutputValues[i] = neurons[i].getY();
      }

      //  calculate S and V
      for (var i = 0; i < recognizedImage.length; i++) {
        var newState = 0;
        for (var j = 0; j < this.size; j++) {
          newState += weightMatrix[neurons[i].getIndex()][j] * neurons[j].getX();
        }
        neurons[i].setState(newState);
        neurons[i].changeState();
      }
      isImageRecognized = true;

      //  is equal
      for (var i = 0; i < lastOutputValues.length; i++) {
        if (lastOutputValues[i] != neurons[i].getY()) {
          isImageRecognized = false;
          break;
        }
      }

      for (var i = 0; i < recognizedImage.length; i++) {
        recognizedImage[i] = neurons[i].getY();
      }
    }

    return recognizedImage;
  }

  public Integer[] crashImage(Integer[] img, int percentage) {
    Set<Integer> indexesForChange = new HashSet<>();
    var crashedImage = img.clone();
    var indexes = generateIndexList(img.length);

    var pixIndex = (img.length * percentage) / 100;
    for (var i = 0; i < pixIndex; i++) {
      int randomIndex = random.nextInt(indexes.size());
      indexesForChange.add(indexes.get(randomIndex));
      indexes.remove(randomIndex);
    }

    indexesForChange.forEach(index -> crashedImage[index] *= -1);
    return crashedImage;
  }

  private void initCoefficients(Integer[] img) {
    for (var i = 0; i < img.length; i++) {
      for (var j = 0; j < img.length; j++) {
        if (i == j) {
          weightMatrix[i][j] = 0;
        } else {
          weightMatrix[i][j] += img[i] * img[j];
        }
      }
    }
    showMatrix(weightMatrix);
  }

  private void showMatrix(int[][] matrix) {
    System.out.println("MATRIX");
    for (var i = 0; i < matrix.length; i++) {
      System.out.println();
      for (var j = 0; j < matrix.length; j++) {
        if (matrix[i][j] == -1) {
          System.out.print(matrix[i][j] + " ");
        } else {
          System.out.print(" " + matrix[i][j] + " ");
        }
      }
    }
  }

  private List<Integer> generateIndexList(int length) {
    List<Integer> indexes = new ArrayList<>();
    for (var i = 0; i < length; i++) {
      indexes.add(i);
    }
    return indexes;
  }
}
