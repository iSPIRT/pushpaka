package in.ispirt.pushpaka.unittests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import in.ispirt.pushpaka.authorisation.utils.SpicedbClient;

public class AuthZTest {

	public static SpicedbClient spicedbClient;

	@BeforeClass
    public static void setup() {
        spicedbClient = SpicedbClient.getInstance(SpicedbClient.SPICEDDB_TARGET, SpicedbClient.SPICEDB_TOKEN);
    }

    @AfterClass
    public static void tearDown() {
    	try {
			spicedbClient.shutdownChannel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

	@Test
	public void testWriteSchema(){
		spicedbClient.writeSchema(SpicedbClient.SPICEDDB_PERMISSION_FILE);
	} 
    
}
