package by.peekhovsky.lab2.analyze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author Rastsislau Piakhouski 2019
 */
public class FigureKMediansAnalyzer {

  private static final Logger log = LoggerFactory.getLogger(FigureKMediansAnalyzer.class);

  private final Random random;

  private final double minClusterDistance;
  private final double distanceSubtrahend;

  public FigureKMediansAnalyzer(double minClusterDistance, double distanceSubtrahend) {
    this(new Random(), minClusterDistance, distanceSubtrahend);
  }

  public FigureKMediansAnalyzer(Random random, double minClusterDistance, double distanceSubtrahend) {
    this.minClusterDistance = minClusterDistance;
    this.random = random;
    this.distanceSubtrahend = distanceSubtrahend;
  }

  public Map<VectorFigure, Map<Integer, VectorFigure>> analyze(int numOfClusters, Map<Integer, VectorFigure> figures) {
    Map<VectorFigure, Map<Integer, VectorFigure>> clusters = new HashMap<>();
    setUpNewRandomCenters(numOfClusters, figures, clusters);
    addFiguresToClusters(figures, clusters);
    //showClusterFigures(clusters);

    while (true) {
      var prevClusterCenters = clusters.keySet();
      clusters = recalculateCenters(clusters);
      var newClusterCenters = clusters.keySet();
      addFiguresToClusters(figures, clusters);
      if (isClusterCenterSetsSimilar(prevClusterCenters, newClusterCenters)) {
        break;
      }
    }
    return clusters;
  }


  private void setUpNewRandomCenters(
      int numOfClusters,
      Map<Integer, VectorFigure> figures,
      Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {
    var values = figures.entrySet().toArray(new Map.Entry[0]);

    var attemptCounter = 0;
    var dynamicMinClusterDistance = minClusterDistance;

    for (int i = 0; i < numOfClusters; i++) {
      while (true) {
        @SuppressWarnings("unchecked")
        Map.Entry<Integer, VectorFigure> clusterCenterOfMassEntry = values[random.nextInt(values.length)];
        boolean isAppropriate = true;

        if (clusters.get(clusterCenterOfMassEntry.getValue()) != null) {
          continue;

        } else {
          for (Map.Entry<VectorFigure, Map<Integer, VectorFigure>> cluster : clusters.entrySet()) {
            log.info("DISTANCE: {}", cluster.getKey().distance(clusterCenterOfMassEntry.getValue()));
            if (cluster.getKey().distance(clusterCenterOfMassEntry.getValue()) < dynamicMinClusterDistance) {
              log.info("Distance is to small, trying again...");
              isAppropriate = false;
            }
          }
        }

        if (!isAppropriate) {
          attemptCounter++;
          if (attemptCounter == clusters.size()) {
            dynamicMinClusterDistance -= distanceSubtrahend;
            attemptCounter = 0;
          }
          continue;
        }

        Map<Integer, VectorFigure> newFigures = new HashMap<>();
        newFigures.put(clusterCenterOfMassEntry.getKey(), clusterCenterOfMassEntry.getValue());
        clusters.put(clusterCenterOfMassEntry.getValue(), newFigures);
        break;
      }
    }
  }

  private Map<VectorFigure, Map<Integer, VectorFigure>> recalculateCenters(Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {
    Map<VectorFigure, Map<Integer, VectorFigure>> newClusters = new HashMap<>();
    System.out.println("\n\n");
    clusters.forEach((cluster, figures) -> {
      log.info("Cluster: ");
      System.out.println(cluster);
      log.info("Figures: ");
      System.out.println(figures);

      Map<Integer, VectorFigure> newFigures = new HashMap<>();
      VectorFigure clusterCenter = calcCenterOfMassOfVectors(figures.values());
      newClusters.put(clusterCenter, newFigures);
    });

    return newClusters;
  }


  private VectorFigure calcCenterOfMassOfVectors(Collection<VectorFigure> vectorFigures) {
    List<Double> squares = new ArrayList<>();
    List<Double> perimeters = new ArrayList<>();
    List<Double> compactnesses = new ArrayList<>();
    List<Double> elongations = new ArrayList<>();
    int size = vectorFigures.size();
    int medianIndex = (int) Math.abs(size / 2.0);

    if (vectorFigures.isEmpty()) {
      return new VectorFigureImpl(0, 0, 0, 0);
    }

    for (VectorFigure vectorFigure : vectorFigures) {
      squares.add(vectorFigure.getSquare());
      perimeters.add(vectorFigure.getPerimeter());
      compactnesses.add(vectorFigure.getCompactness());
      elongations.add(vectorFigure.getElongation());
    }

    squares.sort(Double::compareTo);
    perimeters.sort(Double::compareTo);
    compactnesses.sort(Double::compareTo);
    elongations.sort(Double::compareTo);

    return new VectorFigureImpl(
        squares.get(medianIndex),
        perimeters.get(medianIndex),
        compactnesses.get(medianIndex),
        elongations.get(medianIndex));
  }


  private boolean isClusterCenterSetsSimilar(Set<VectorFigure> clusterCenters1, Set<VectorFigure> clusterCenters2) {
    VectorFigure[] clusterCentersArray1 = clusterCenters1.toArray(new VectorFigure[0]);
    VectorFigure[] clusterCentersArray2 = clusterCenters2.toArray(new VectorFigure[0]);

    if (clusterCentersArray1.length != clusterCentersArray2.length) {
      throw new IllegalArgumentException("Cluster centers lengths in not equal");
    }

    for (var i = 0; i < clusterCentersArray1.length; i++) {
      if (!clusterCentersArray1[i].equals(clusterCentersArray2[i])) {
        return false;
      }
    }
    return true;
  }


  private void addFiguresToClusters(
      Map<Integer, VectorFigure> figures,
      final Map<VectorFigure, Map<Integer, VectorFigure>> clusters) {

    figures.forEach((key, figure) -> {
      VectorFigure minClusterFigure = null;
      if (clusters.isEmpty()) {
        throw new IllegalArgumentException();
      }

      double minDistance = -1;
      for (VectorFigure cluster : clusters.keySet()) {
        double distance = figure.distance(cluster);
        if (distance < minDistance || minDistance == -1) {
          minDistance = distance;
          minClusterFigure = cluster;
        }
      }
      clusters.get(minClusterFigure).put(key, figure);
    });
  }

  private void showClusterFigures(Map<VectorFigure, Map<Integer, VectorFigure>> clusterFigures) {
    clusterFigures.forEach((cluster, figures) -> {
      log.info("Cluster: ");
      System.out.println(cluster);
      log.info("Figures: ");
      figures.forEach((key, figure) -> {
        System.out.println("Key: " + key);
        System.out.println("Figure: " + figure);
        System.out.println();
      });
    });
  }
}
