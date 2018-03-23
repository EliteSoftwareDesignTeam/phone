package com.teamness.smane;

public interface Converter<F, T> {

    T convert(F t);

}
