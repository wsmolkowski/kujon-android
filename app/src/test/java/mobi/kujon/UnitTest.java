package mobi.kujon;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public abstract class UnitTest {
    @Before
    public void setup() {
        initializeMocks();
        onSetup();
    }

    private void initializeMocks() {
        MockitoAnnotations.initMocks(this);
    }

    protected abstract void onSetup();
}
