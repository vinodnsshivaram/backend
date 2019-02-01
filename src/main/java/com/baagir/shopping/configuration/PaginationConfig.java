package com.baagir.shopping.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class PaginationConfig {
    Environment env;

    @Autowired
    public PaginationConfig(Environment env){
        this.env = env;
    }

    public int getLimit(){
        return Integer.parseInt(env.getProperty("pagination.limit.default"));
    }

}
