package net.engin33r.luaspigot.lua;

import lombok.RequiredArgsConstructor;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Wrapper around Globals describing a Lua state.
 */
@RequiredArgsConstructor
public class State {
    private final Globals env;

    public void execute(String code) {
        env.load(code).call();
    }

    public void load(String path) throws IOException {
        Charset inputCharset = Charset.forName("UTF8");
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), inputCharset))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            env.load(everything, path).call();
        }
    }

    public void define(String name, LuaValue value) {
        env.set(name, value);
    }
}
