package org.kjussakov.sochorn.samples;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Example of a simple Echo WebSocket client using the Nashorn polyfill
 */
public class Echo {
    private static final Lock lock = new ReentrantLock();
    private static final Condition done  = lock.newCondition();

    private static ScriptEngine nashornEngine = new ScriptEngineManager().getEngineByName("nashorn");

    public static void main(String[] args)
    {
        lock.lock();
        try {
            InputStream pollyStream = Echo.class.getResourceAsStream("/resources/polyfills.js");
            nashornEngine.eval(new InputStreamReader(pollyStream));

            InputStream indexStream = Echo.class.getResourceAsStream("/resources/echo/index.js");
            nashornEngine.eval(new InputStreamReader(indexStream));

            // Wait at most 30 seconds for the test to finish
            done.await(30, TimeUnit.SECONDS);
        } catch (ScriptException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void done() {
        lock.lock();
        try {
            System.out.println("Echo test completed!");
            done.signal();
        } finally {
            lock.unlock();
        }
    }
}