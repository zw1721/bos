package com.taotao.common.httpclient;

import org.apache.http.conn.HttpClientConnectionManager;

// 创建内部类，继承Thread，成为线程类
public class IdleConnectionEvictor extends Thread {

    private final HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;

    public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
        // 启动自己这个线程。
        this.start();
    }

    // 定义线程任务，定时关闭失效的连接
    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    // 每隔5秒钟一次
                    wait(50000);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}