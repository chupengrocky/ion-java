import com.amazon.ion.IonSystem;
import com.amazon.ion.IonWriter;
import com.amazon.ion.SymbolTable;
import com.amazon.ion.impl.Symtabs;
import com.amazon.ion.impl._Private_IonBinaryWriterBuilder;
import com.amazon.ion.impl._Private_IonWriter;
import com.amazon.ion.impl._Private_Utils;
import com.amazon.ion.system.IonBinaryWriterBuilder;
import com.amazon.ion.system.IonSystemBuilder;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class NewTestCase {

    //Boundary setStreamCopyOptimized() true & false
    //In this case, we test whether setStreamCopyOptimized() works or not.
    @Test
    public void testStreamCopyOptimized()
    {
        IonBinaryWriterBuilder b = IonBinaryWriterBuilder.standard();
        b.setStreamCopyOptimized(false);
        assertFalse(b.isStreamCopyOptimized());

        OutputStream out = new ByteArrayOutputStream();
        IonWriter w = b.build(out);
        assertFalse(((_Private_IonWriter)w).isStreamCopyOptimized());
    }

    //Boundary setStreamCopyOptimized() true & false
    //In this case, we test whether setStreamCopyOptimized() works or not when we use immutable() to create a new IonBinaryWriterBuilder

    @Test(expected = UnsupportedOperationException.class)
    public void testStreamCopyOptimizedImmutability()
    {
        IonBinaryWriterBuilder b = IonBinaryWriterBuilder.standard();
        b.setStreamCopyOptimized(false);

        IonBinaryWriterBuilder b2 = b.immutable();
        assertFalse(b2.isStreamCopyOptimized());
        b2.setStreamCopyOptimized(true);
    }

    //Boundary writeFloat() -2^128 ~ +2^128
    //Boundary setIsFloatBinary32Enabled() true & false
    //when we set setIsFloatBinary32Enabled() as true, we enables writing Binary32 values for floats when there would be no loss in precision.
    //In this case, we test whether setIsFloatBinary32Enabled() works or not by testing the size of ByteArrayOutputStream.
    @Test
    public void testSetIsFloatBinary32Enabled() throws IOException
    {
        IonSystem system = IonSystemBuilder.standard().build();

        IonBinaryWriterBuilder b = IonBinaryWriterBuilder.standard();
        b.setIsFloatBinary32Enabled(true);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IonWriter writer = b.build(out);
        writer.writeFloat(1.5);
        writer.close();
        assertEquals(9, out.size());
        assertEquals(system.newFloat(1.5), system.singleValue(out.toByteArray()));

        b.setIsFloatBinary32Enabled(false);

        out = new ByteArrayOutputStream();
        writer = b.build(out);
        writer.writeFloat(1.5);
        writer.close();
        assertEquals(13, out.size());
        assertEquals(system.newFloat(1.5), system.singleValue(out.toByteArray()));
    }

    //Boundary getSymbolTable().findSymbol()  must be a string
    //Boundary setInitialSymbolTable() must be a SymbolTable
    //In this case, we test whether setInitialSymbolTable() can accept a SymbolTable as input, whether getInitialSymbolTable() can return this SymbolTable
    //and the findSymbol() function can return the right number.
    //we also test when we call build() multiple times, whether we get not same SymbolTables (but they should have same value).

    @Test
    public void testInitialSymtab()
            throws IOException
    {
        SymbolTable sst = _Private_Utils.systemSymtab(1);

        SymbolTable lst0 = Symtabs.localSymbolTableFactory().newLocalSymtab(sst);
        lst0.intern("RookieMan");

        _Private_IonBinaryWriterBuilder b =
                _Private_IonBinaryWriterBuilder.standard();
        b.setInitialSymbolTable(lst0);
        assertSame(lst0, b.getInitialSymbolTable());

        OutputStream out = new ByteArrayOutputStream();
        IonWriter writer = b.build(out);
        assertEquals(sst.getMaxId() + 1,
                writer.getSymbolTable().findSymbol("RookieMan"));

        // Builder makes a copy of the symtab
        SymbolTable lst1 = writer.getSymbolTable();
        assertSame(lst0, b.getInitialSymbolTable());

        // Second call to build, we get another copy.
        writer = b.build(out);
        SymbolTable lst2 = writer.getSymbolTable();
        writer.writeSymbol("addition");

        // Now the LST has been extended, so the builder should make a copy
        // with the original max_id.
        writer = b.build(out);
        SymbolTable lst3 = writer.getSymbolTable();
        assertEquals(sst.getMaxId() + 1,
                lst3.findSymbol("RookieMan"));


        assertEquals(sst.getMaxId() + 1, lst3.getMaxId());
        assertNotSame(lst0, lst3);
        assertNotSame(lst1, lst3);
        assertNotSame(lst2, lst3);
        assertSame(lst0, b.getInitialSymbolTable());
    }
}