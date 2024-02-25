package com.br.comunicacaoms.productapi.config.interceptor;

import java.util.List;

public class Urls {

    public Urls() {}
    public static final List<String> PROTECTED_URLS = List.of(
            "api/products",
            "api/categories",
            "api/suppliers"
    );
}
