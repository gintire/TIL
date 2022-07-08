package jython;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.internal.matchers.Null;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;
import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * Project: library
 * Package: jython
 * <p>
 *
 * @author: jin36
 * @version: v21.04
 * Date: 2021-04-15
 * Time: 오후 8:50
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class JythonTest {
    @Test
    public void givenPythonScriptEngineIsAvailable_whenScriptInvoked_thenOutputDisplayed() throws Exception {
        StringWriter writer = new StringWriter();
        ScriptContext context = new SimpleScriptContext();
        context.setWriter(writer);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("python");
        engine.eval(new FileReader(resolvePythonScriptPath("hello.py")), context);
        Assert.assertEquals("Should contain script output: ", "Hello Baeldung Readers!!", writer.toString().trim());
    }

    private static String resolvePythonScriptPath(String filename) {
        File file = new File("D:\\programmer\\Emotion-detection\\src\\" + filename);
        return file.getAbsolutePath();
    }

    @Test
    public void givenPythonInterpreter_whenPrintExecuted_thenOutputDisplayed() {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            StringWriter output = new StringWriter();
            pyInterp.setOut(output);

            pyInterp.exec("print('Hello Baeldung Readers!!')");
            Assert.assertEquals("Should contain script output: ", "Hello Baeldung Readers!!", output.toString()
                    .trim());
        }
    }
    @Test
    public void givenPythonInterpreter_whenNumbersAdded_thenOutputDisplayed() {
        try (PythonInterpreter pyInterp = new PythonInterpreter()) {
            pyInterp.exec("x = 10+10");
            PyObject x = pyInterp.get("x");
            Assert.assertEquals("x: ", 20, x.asInt());
        }
    }
    @Test
    public void compile_when_no_key() {
        HashMap map = new HashMap();
        map.put("a", "b");
        System.out.println(map.get("b"));
    }
}
