package com.caston.challenge.demo;

/**
 * Main class for creating and running a basic demo of the project with an HTTP
 * server. Intended for development purposes.
 */
public final class DemoMain {
  private static int DEMO_PORT = 8000;
  private static String DEMO_PATH = "/demo";

  public static void main(String[] args) throws Exception {
    System.out.println("Creating demo server");
    System.out.println("Direct your browser to http://localhost:"
        + DEMO_PORT + DEMO_PATH);
    DemoHttpHandler.blockingCreateAndStart(DEMO_PATH, DEMO_PORT);
  }
}
