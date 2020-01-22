package com.ponto.pontointeligente;

import java.util.function.Function;

public class FunctionalExample {

    public static void main(String[] args) {

        functionalTest(success -> {

            System.out.println(success);
            return null;

        }, error -> {

            System.out.println(error);
            return error;

        });

    }

    private static void functionalTest(Function<String, Void> success, Function<String, String> error) {
        success.apply("Sucesso");
        error.apply("Erro");
    }

}
