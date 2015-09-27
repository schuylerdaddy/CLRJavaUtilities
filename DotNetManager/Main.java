package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.String;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException{

        try {
            DotNetServiceManager dnsm = new DotNetServiceManager();
            InputStream is =dnsm.getOutPutStream(HttpMethod.GET, "https://www.google.com", null, null);
            Thread pipe = new Pipe(is, System.out);
            pipe.start();
            pipe.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
