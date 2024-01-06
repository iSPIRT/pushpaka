package in.ispirt.pushpaka.unittests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import in.ispirt.pushpaka.authorisation.utils.SpicedbClient;

@RunWith(JUnit4.class)
public class AuthZTest {
    private static final String target = "localhost:50051";
	private static final String token = "somerandomkeyhere";
	private static SpicedbClient spicedbClient;

	@BeforeClass
    public static void setup() {
		spicedbClient = SpicedbClient.getInstance(target, token);
    }

    @AfterClass
    public static void tearDown() {
    	try {
      		spicedbClient.shutdownChannel();
    	} catch (InterruptedException exception) {
      		exception.printStackTrace();
    	}
    }

    @Test
	public void testWriteSchema() {
		spicedbClient.writeSchema(SpicedbClient.SPICEDDB_PERMISSION_FILE);
	}
		

}
