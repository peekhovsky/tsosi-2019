package by.peekhovsky.tsosi.lab3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HopfieldAnalyzerImpl implements HopfieldAnalyzer {

  private final int size;
  private final Neuron[] neurons;
  private final int[] lastOutputValues;
  private final int[][] weightMatrix;
  private final Random random = new Random();

  public HopfieldAnalyzerImpl(List<Integer[]> letters, int size) {
    this.size = size;
    neurons = new Neuron[size];
    lastOutputValues = new int[size];
    weightMatrix = new int[size][size];

    for (int i = 0; i < neurons.length; i++) {
      neurons[i] = new Neuron(i);
    }

    letters.forEach(value -> initCoefficients(value));
  }


  private void initCoefficients(Integer[] img) {
    for (int i = 0; i < img.length; i++) {
      for (int j = 0; j < img.length; j++) {
        if (i == j) {
          weightMatrix[i][j] = 0;
        } else {
          weightMatrix[i][j] += img[i] * img[j];
        }
      }
    }
  }

  public Integer[] findImage(Integer[] img) {
    int times = 10000;
    boolean isImageRecognized = true;
    Integer[] recognizedImage = img.clone();

    while (!isImageRecognized || times-- > 0) {
      //заносим входящие значения
      for (int i = 0; i < recognizedImage.length; i++) {
        neurons[i].setX(recognizedImage[i]);
        lastOutputValues[i] = neurons[i].getY();
      }

      //сначало вычисляем S потом У
      for (int i = 0; i < recognizedImage.length; i++) {
        int newState = 0;
        for (int j = 0; j < this.size; j++) {
          newState += weightMatrix[neurons[i].getIndex()][j] * neurons[j].getX();
        }
        neurons[i].setState(newState);
        neurons[i].changeState();
      }
      isImageRecognized = true;

      //проверяем на равенство входного и выходного векторов
      for (int i = 0; i < lastOutputValues.length; i++) {
        if (lastOutputValues[i] != neurons[i].getY()) {
          isImageRecognized = false;
          break;
        }
      }

      for (int i = 0; i < recognizedImage.length; i++) {
        recognizedImage[i] = neurons[i].getY();
      }
    }

    return recognizedImage;
  }

  public Integer[] crashImage(Integer[] img, int percentage) {
    List<Integer> indexes = generateIndexList(img.length);
    Set<Integer> indexesForChange = new HashSet<>();
    Integer[] crashedImage = img.clone();

    int pixIndex = (img.length * percentage) / 100;
    for (int i = 0; i < pixIndex; i++) {
      int randomIndex = random.nextInt(indexes.size());
      indexesForChange.add(indexes.get(randomIndex));
      indexes.remove(randomIndex);
    }

    indexesForChange.forEach(index -> crashedImage[index] *= -1);
    return crashedImage;
  }

  private List<Integer> generateIndexList(int length) {
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < length; i++) {
      indexes.add(i);
    }
    return indexes;
  }
}
