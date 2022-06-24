package com.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerCrawler {
    private final ThreadPoolExecutor mThreadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    private final int mPort;
    private final static String BASE_URL = "localhost";
    private final static String CONTEXT = "/crawl";


    public ServerCrawler(int port) {
        mPort = port;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(BASE_URL, mPort), 0);
        server.createContext(CONTEXT, new CrawlHandler());
        server.setExecutor(mThreadPoolExecutor);
        server.start();
    }

}
