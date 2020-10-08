package by.peekhovsky.tsosi.lab1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import by.peekhovsky.tsosi.lab1.filter.Filter;
import by.peekhovsky.tsosi.lab1.filter.HarmonicMeanFilter;
import by.peekhovsky.tsosi.lab1.filter.ImpulseMedianFilter;
import by.peekhovsky.tsosi.lab1.filter.MedianFilter;
import by.peekhovsky.tsosi.lab1.filter.NegativeFilter;
import by.peekhovsky.tsosi.lab1.util.ImageUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class UIController implements Initializable {

  private final FileChooser imgFileChooser = new FileChooser();

  @FXML
  private Button openImageButton;

  @FXML
  private Button toNegativeButton;

  @FXML
  private Button toMedianButton;

  @FXML
  private Button toHarmonicMeanButton;

  @FXML
  private Button toImpulseMedianButton;

  @FXML
  private ImageView imageView;

  @FXML
  private TextField medianMaskTextField;

  @FXML
  private Label infoLabel;

  @FXML
  private BarChart<String, Number> histogramChart;


  private BufferedImage image;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    FileChooser.ExtensionFilter extFilter =
      new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png");
    imgFileChooser.setSelectedExtensionFilter(extFilter);
    histogramChart.setLegendVisible(false);
  }

  @FXML
  void handleOpenNewImage(Event event) {
    Window window = ((Node) event.getTarget()).getScene().getWindow();
    File imageFile = imgFileChooser.showOpenDialog(window);
    setImage(imageFile);
  }

  @FXML
  void handleToNegative() {
    if (image != null) {
      Filter filter = new NegativeFilter();
      BufferedImage newImage = filter.filter(image, null);
      setImage(newImage);
    }
  }

  @FXML
  void handleToMedian() {
    if (image == null) {
      return;
    }

    int maskSize = 3;
    try {
      maskSize = Integer.parseInt(medianMaskTextField.getText());
    } catch (NumberFormatException e) {
      medianMaskTextField.setText(Integer.toString(maskSize));
    } finally {
      Filter filter = new MedianFilter();
      BufferedImage newImage = filter.filter(image, maskSize);
      setImage(newImage);
    }
  }

  @FXML
  void handleToHarmonicMean() {
    if (image == null) {
      return;
    }
    Filter filter = new HarmonicMeanFilter();
    BufferedImage newImage = filter.filter(image, null);
    setImage(newImage);
  }

  @FXML
  void handleToImpulseMedian() {
    if (image == null) {
      return;
    }
    Filter filter = new ImpulseMedianFilter();
    BufferedImage newImage = filter.filter(image, null);
    setImage(newImage);
  }


  private void drawHistogram() {
    histogramChart.getData().clear();

    if (image == null) {
      return;
    }
    Map<Integer, Integer> intensities = new HashMap<>();
    for (int i = 0; i < 255; i++) {
      intensities.put(i, 0);
    }
    final double RED_FACTOR = 0.3;
    final double GREEN_FACTOR = 0.59;
    final double BLUE_FACTOR = 0.11;

    for (int y = 0; y < image.getHeight(); y+=10) {
      for (int x = 0; x < image.getWidth(); x+=10) {
        int rgb = image.getRGB(x, y);
        int medium = ((int) ((rgb >> 16 & 0xff) * RED_FACTOR)
          + (int) ((rgb >> 8 & 0xff) * GREEN_FACTOR)
          + (int) ((rgb & 0xff) * BLUE_FACTOR));
        intensities.put(medium, intensities.get(medium) + 1);
      }
    }

    XYChart.Series<String, Number> series = new XYChart.Series<>();

    intensities.forEach((rgb, intensity) -> {
      series.getData().add(new XYChart.Data<>(rgb.toString(), intensity));
    });
    histogramChart.getData().add(series);
  }


  private void setImage(File imageFile) {
    if (Objects.isNull(imageFile)) {
      return;
    }
    try {
      BufferedImage img = ImageIO.read(imageFile);
      setImage(img);
    } catch (IOException e) {
      showMessage("Cannot read an image.");
    }
  }

  private void setImage(BufferedImage image) {
    this.image = image;
    setImageView(image);
    enableImageProcessingButtons();
    drawHistogram();
  }

  private void setImageView(BufferedImage bufferedImage) {
    imageView.setImage(ImageUtils.convertToFXImage(bufferedImage));
  }

  private void showMessage(String label) {
    infoLabel.setText(label);
  }

  private void removeMessage() {
    infoLabel.setText("");
  }

  private void enableImageProcessingButtons() {
    toNegativeButton.setDisable(false);
    toMedianButton.setDisable(false);
    toHarmonicMeanButton.setDisable(false);
    toImpulseMedianButton.setDisable(false);
  }
}
