package com.driver;

import com.server.ServerCrawler;

import java.io.IOException;

public class Driver {

    public static void main(String[] args) throws IOException {
        ServerCrawler serverCrawler = new ServerCrawler(8999);
        serverCrawler.start();
    }

}
