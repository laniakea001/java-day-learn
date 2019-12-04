/**
 * 
 */
package com.hjj.daylearn.javadaylearn.day11_thread_pool.java8;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @see https://www.oracle.com/technetwork/java/javase/8-whats-new-2157071.html
 *
 * @see http://www.importnew.com/16436.html
 */
public class NewFeatures {
  public static void main(String[] args) {
    // Java SE 8 Features and Enhancements
    List<Integer> primes = Arrays.asList(new Integer[]{2, 3,5,7});
    int factor = 2;
    primes.forEach(element -> { System.out.println(factor*element); });
    primes.forEach(element -> System.out.println(factor*element));
    primes.forEach(e -> System.out.println("Hello World " + e));
  }
}
