package by.peekhovsky.tsosi.lab3;

import lombok.Data;

@Data
public class Neuron {

  private final int index;
  private int state;
  private int x;
  private int y;

  public Neuron(int index) {
    this.index = index;
  }

  public void changeState() {
    y = Integer.compare(state, 0);
  }
}
