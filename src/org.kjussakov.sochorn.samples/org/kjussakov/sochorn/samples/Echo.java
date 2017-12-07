package org.kjussakov.sochorn.samples;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.lang.Thread.sleep;

/**
 * Example of a simple Echo WebSocket client using the Nashorn polyfill
 */
public class Echo {
    private static ScriptEngine nashornEngine = new ScriptEngineManager().getEngineByName("nashorn");

    public static void main(String[] args)
    {
        try {
            InputStream pollyStream = Echo.class.getResourceAsStream("/resources/polyfills.js");
            nashornEngine.eval(new InputStreamReader(pollyStream));

            InputStream indexStream = Echo.class.getResourceAsStream("/resources/echo/index.js");
            nashornEngine.eval(new InputStreamReader(indexStream));

            sleep(5000);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}