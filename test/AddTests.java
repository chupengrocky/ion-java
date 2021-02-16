import com.amazon.ion.apps.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AddTests {
    @Test
    public void testEncodeApp(){
        EncodeApp encodeApp = new EncodeApp();
        Class c = EncodeApp.class;
        assertEquals(4, TestUtil.invoke(encodeApp, "processOptions",
                new String[]{"--catalog", ".\\new.txt", "--output-dir", ".\\",
                ".\\new.txt"}));
    }

    @Test
    public void testPrintApp(){
        PrintApp printApp = new PrintApp();
        Class c = PrintApp.class;
        assertEquals(4, TestUtil.invoke(printApp, "processOptions",
                new String[]{"--catalog", ".\\new.txt", "--output-dir", ".\\",
                        ".\\new.txt"}));
    }

    @Test
    public void testSymtabApp(){
        SymtabApp symtabapp = new SymtabApp();
        Class c = SymtabApp.class;
        assertEquals(6, TestUtil.invoke(symtabapp, "processOptions",
                new String[]{"--catalog", ".\\new.txt", "--name","test",
                        "--version", "1", "--output-dir", ".\\",
                        ".\\new.txt"}));
    }
}
