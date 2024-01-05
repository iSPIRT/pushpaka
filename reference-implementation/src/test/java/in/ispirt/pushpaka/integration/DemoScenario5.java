package in.ispirt.pushpaka.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenario 5:
 **/
class DemoScenario5 {

  @BeforeEach
  void cleanup() {}

  // Scenario 5.a.1
  @Test
  public void testScenario_5_a_1()
    throws ClientProtocolException, IOException, java.text.ParseException {
    assertEquals(1, 2);
  }
}
