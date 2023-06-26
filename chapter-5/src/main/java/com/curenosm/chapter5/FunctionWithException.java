package com.curenosm.chapter5;

@FunctionalInterface
public interface FunctionWithException<T, R, E extends Exception> {

  R apply (T t) throws E;
}
