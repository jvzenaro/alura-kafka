package br.com.alura.ecommerce;

import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {

    private Supplier<T> supplier;

    private final Gson gson = new GsonBuilder().create();

    public GsonSerializer() {
        this.supplier = new Supplier<T>() {

            @Override
            public T get() {
                return null;
            }
        };
    }

    @Override
    public byte[] serialize(String topic, T data) {
        return gson.toJson(data).getBytes();
    }

}