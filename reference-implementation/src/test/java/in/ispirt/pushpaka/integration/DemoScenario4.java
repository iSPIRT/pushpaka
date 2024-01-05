package in.ispirt.pushpaka.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenario 4
 */
class DemoScenario4 {

  @BeforeEach
  void cleanup() {}

  // Scenario 4.a.1
  @Test
  public void testScenario_4_a_1()
    throws ClientProtocolException, IOException, java.text.ParseException {
    assertEquals(1, 2);
  }
}
