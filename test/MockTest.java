import static com.amazon.ion.IonType.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import com.amazon.ion.IonReader;
import com.amazon.ion.apps.SymtabApp;
import org.junit.*;
import org.mockito.*;

public class MockTest {
    private IonReader ionReader;
    private SymtabApp symtabApp;

    @Before
    public void setup() {
        //  Mocks are being created.
        ionReader = mock(IonReader.class);
        symtabApp = new SymtabApp(ionReader);
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSymtabAPPMock() {
        when(ionReader.hasNext()).thenReturn(true);
        when(ionReader.getDepth()).thenReturn(0);

        when(ionReader.next()).thenReturn(SYMBOL);
        symtabApp.process();
        verify(ionReader, times(1)).getFieldName();
        verify(ionReader, times(2)).hasNext();
        verify(ionReader, times(1)).stringValue();

        when(ionReader.next()).thenReturn(STRUCT);
        symtabApp.process();
        verify(ionReader, times(1)).stepIn();

        when(ionReader.next()).thenReturn(LIST);
        symtabApp.process();
        verify(ionReader, times(2)).stepIn();

    }

    @Test
    public void testSymtabOrderMock() {
        when(ionReader.hasNext()).thenReturn(true);
        when(ionReader.getDepth()).thenReturn(0);
        when(ionReader.next()).thenReturn(SYMBOL);

        InOrder inOrder = inOrder(ionReader);
        symtabApp.process();
        inOrder.verify(ionReader).hasNext();
        inOrder.verify(ionReader).next();
        inOrder.verify(ionReader).getFieldName();
        inOrder.verify(ionReader).stringValue();
        inOrder.verify(ionReader).hasNext();
        verifyNoMoreInteractions(ionReader);
    }

    @Test
    public void testSymtabResultMock(){
        when(ionReader.hasNext()).thenReturn(false);
        assertFalse(symtabApp.process(ionReader));
    }
}
