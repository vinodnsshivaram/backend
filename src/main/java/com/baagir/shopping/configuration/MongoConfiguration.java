package com.baagir.shopping.configuration;

import com.mongodb.MongoClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@ConfigurationProperties("mongodb")
public class MongoConfiguration {
    private String host;
    private String database;
    private Integer port;
    private String password;
    private String username;
    private String uri;


    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        if (uri != null)
            mongoClient = new MongoClient(uri);
        else if (host != null && port != null)
            mongoClient = new MongoClient(host, port);
        else if (host != null && port == null)
            mongoClient = new MongoClient(host, 27017);
        return new SimpleMongoDbFactory(mongoClient, database);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    public MongoClient mongoClient() {
        if (uri != null)
            return new MongoClient(uri);
        else if (host != null && port != null)
            return new MongoClient(host, port);
        else if (host != null && port == null)
            return new MongoClient(host, 27017);
        return new MongoClient("localhost", 27017);
    }

    protected String getDatabaseName() {
        return database;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public Integer getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "MongoConfiguration{" +
                "host='" + host + '\'' +
                ", database='" + database + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
