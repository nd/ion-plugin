package ion;

import java.util.ArrayList;
import java.util.Random;

final class IntIntTable {
  private int mySize;
  private long[] myData;

  IntIntTable(int size) {
    if (size <= 1) {
      throw new IllegalArgumentException("Size must be > 1");
    }
    mySize = size;
    myData = new long[2 * size];
  }

  int get(int key) {
    int index = index(key, mySize);
    return (int) (myData[index] & 0xffffffffL) == key ? (int) myData[index + 1] : 0;
  }

  void put(int key, int value) {
    int index = index(key, mySize);
    long exitingKey = myData[index];
    if (exitingKey >> 32 == 0 || (exitingKey & 0xffffffffL) == key) {
      myData[index] = (1L << 32) | key;
      myData[index + 1] = value;
    } else {
      boolean collisionFound;
      int size = mySize;
      long[] data;
      do {
        collisionFound = false;
        size *= 2;
        data = new long[2 * size];
        for (int i = 0; i < myData.length - 1; i += 2) {
          long k = myData[i];
          long v = myData[i+1];
          if (k >> 32 == 1) {
            int newIndex = index((int) (k & 0xffffffffL), size);
            if (data[newIndex] >> 32 == 0) {
              data[newIndex] = k;
              data[newIndex+1] = v;
            } else {
              collisionFound = true;
              break;
            }
          }
        }
      } while (collisionFound);
      mySize = size;
      myData = data;
      put(key, value);
    }
  }

  private static int index(int key, int size) {
    return (int) (2 * (Integer.toUnsignedLong(key) % (size - 1)));
  }

  public static void main(String... args) {
    IntIntTable t = new IntIntTable(2);
    Random r = new Random();
    ArrayList<Integer> keys = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      int key = r.nextInt(100000000);
      keys.add(key);
      t.put(key, i+1);
    }
    for (Integer key : keys) {
      System.out.println(t.get(key));
    }
  }
}
