package com.github.alexanderfefelov.bgbilling.plugin.search.server.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Permutator<T> {

    public List<List<T>> permutate(List<T> elements) {
        List<List<T>> accumulator = new ArrayList<>();
        permutate(accumulator, elements, elements.size());
        return accumulator;
    }

    private void permutate(List<List<T>> accumulator, List<T> elements, int n) {
        if (n == 1) {
            accumulator.add(new ArrayList<>(elements));
        } else {
            for (int i = 0; i < n - 1; i++) {
                permutate(accumulator, elements, n - 1);
                if (n % 2 == 0) {
                    Collections.swap(elements, i, n - 1);
                } else {
                    Collections.swap(elements, 0, n - 1);
                }
            }
            permutate(accumulator, elements, n - 1);
        }
    }

    public static void main(String[] args) {
        List<String> elements = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Permutator<String> permutator = new Permutator<>();
        List<List<String>> permutations = permutator.permutate(elements);
        System.out.println(permutations.toString());
    }

}
