package com.hjj.daylearn.javadaylearn.day25_sumInArrayIndex;

import java.util.ArrayList;
import java.util.List;

public class SumInArrayIndex {

    public static void main(String[] args) {
        forScore();
    }

    public static void forScore() {
        int[] es = {1, 2, 4, 5, 4, 6, 2, 3, 5};
        int result = 8;

        List<List<Integer>> list = count(es, 0, -1, result);
        System.out.println(list);

        for (List<Integer> l : list) {
            for (Integer i : l) {
                System.out.print(es[i] + ",");
            }
            System.out.println();
        }
    }

    public static List<List<Integer>> count(int[] es, int sum, int currIndex, int result) {
        List<List<Integer>> indexLink = new ArrayList<>();
        for (int i = currIndex + 1; i < es.length; i++) {
            int s = es[i] + sum;
            if (s < result) {
                List<List<Integer>> iLink = count(es, s, i, result);

                for (List list : iLink) {
                    list.add(0, i);
                }

                if (iLink.size() > 0) {
                    indexLink.addAll(iLink);
                }

            } else if (s > result) {
                continue;
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                indexLink.add(list);
            }
        }

        return indexLink;
    }
}
