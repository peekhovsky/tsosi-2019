package by.peekhovsky.tsosi.lab3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public interface HopfieldAnalyzer {
  Integer[] findImage(Integer[] img);
  Integer[] crashImage(Integer[] img, int percent);
}
