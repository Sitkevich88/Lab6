package utils;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException, ClassNotFoundException {

        new Server(19111).run();
    }
}