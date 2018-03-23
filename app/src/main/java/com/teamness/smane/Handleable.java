package com.teamness.smane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Handleable<F, T> {
    protected List<Pair<Converter<F, T>, Handler<T>>> handlers = new ArrayList<>();

    public void addHandler(Converter<F, T> converter, Handler<T> handler) {
        handlers.add(new Pair<>(converter, handler));
    }

    protected void handle(F data) {
        for(Pair<Converter<F, T>, Handler<T>> consumer : handlers) consumer.second.handle(consumer.first.convert(data));
    }

}