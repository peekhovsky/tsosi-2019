package by.peekhovsky.tsosi.lab1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import by.peekhovsky.tsosi.lab1.filter.Filter;
import by.peekhovsky.tsosi.lab1.filter.HarmonicMeanFilter;
import by.peekhovsky.tsosi.lab1.filter.MedianFilter;
import by.peekhovsky.tsosi.lab1.filter.NegativeFilter;
import by.peekhovsky.tsosi.lab1.util.ImageUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
  private ImageView imageView;

  @FXML
  private Label infoLabel;

  private BufferedImage image;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    FileChooser.ExtensionFilter extFilter =
      new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png");
    imgFileChooser.setSelectedExtensionFilter(extFilter);
  }

  @FXML
  void handleOpenNewImage(Event event) {
    Window window = ((Node) event.getTarget()).getScene().getWindow();
    File imageFile = imgFileChooser.showOpenDialog(window);
    setImage(imageFile);
  }

  @FXML
  void handleProcessImage(Event event) {
    Window window = ((Node) event.getTarget()).getScene().getWindow();
    File imageFile = imgFileChooser.showOpenDialog(window);
    setImage(imageFile);
  }

  @FXML
  void handleShowHistogram(Event event) {
  }

  @FXML
  void handleToNegative() {
    if (image != null) {
      Filter filter = new NegativeFilter();
      BufferedImage newImage = filter.filter(image);
      setImage(newImage);
    }
  }

  @FXML
  void handleToMedian() {
    if (image != null) {
      Filter filter = new MedianFilter();
      BufferedImage newImage = filter.filter(image);
      setImage(newImage);
    }
  }

  @FXML
  void handleToHarmonicMean() {
    if (image != null) {
      Filter filter = new HarmonicMeanFilter();
      BufferedImage newImage = filter.filter(image);
      setImage(newImage);
    }
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
  }
}
