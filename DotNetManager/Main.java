package com.company;

import java.io.InputStream;
import java.lang.String;

public class Main {
    public static void main(String[] args) throws InterruptedException, Exception{

        try {
            DotNetServiceManager dnsm = new DotNetServiceManager();
            InputStream is =dnsm.getOutPutStream(HttpMethod.GET, "https://www.google.com", null, null);
            Thread outPipe = new Pipe(is, System.out);
            Thread errPipe = new Pipe(dnsm.getErrStream(), System.err);
            errPipe.start();
            outPipe.start();
            outPipe.join();
            errPipe.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
