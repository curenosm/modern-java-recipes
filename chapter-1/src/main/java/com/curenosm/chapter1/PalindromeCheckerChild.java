package com.curenosm.chapter1;

// This will detect a compile time error, since it's not a functional one
// @FunctionalInterface
public interface PalindromeCheckerChild extends PalindromeChecker {

  int anotherAbstractMethod();
}
