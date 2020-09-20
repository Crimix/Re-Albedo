package com.black_dog20.realbedo.util;

@FunctionalInterface
public interface TriConsumer<A,B,C> {
    void apply(A a, B b, C c);
}