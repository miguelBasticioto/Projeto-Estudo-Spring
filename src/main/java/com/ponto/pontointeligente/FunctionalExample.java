package com.ponto.pontointeligente;

import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionalExample {

    public static void main(String[] args) {

        functionalTest(success -> {

            System.out.println(success);

        }, error -> {

            System.out.println(error);
            return error;

        });

    }

    private static void functionalTest(Consumer<String> success, Function<String, String> error) {
        success.accept("Sucesso");
        error.apply("Erro");
    }

}
