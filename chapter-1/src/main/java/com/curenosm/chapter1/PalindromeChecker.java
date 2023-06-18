package com.curenosm.chapter1;

/**
 * {code @FunctionalInterface} returns a compile time error in case
 * the interface was not correctly defined to match the definition of
 * functional interface. Also provides a statement generated with Javadocs.
 */
@FunctionalInterface
public interface PalindromeChecker {
  boolean isPalindrome(String s);

  /**
   * Default and static methods don't affect the definition of the functional
   * interface, so they're allowed.
   *
   * @return Greeting message.
   */
  default String sayHello() {
    return "Hello, world!";
  }

  static void myStaticMethod() {
    System.out.println("I'm a static method in an interface");
  }
}
