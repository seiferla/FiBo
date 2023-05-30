package de.dhbw.ka.se.fibo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class HelpersUnitTest {


  @Test
  public void testFormatBigDecimalCurrency() {
    BigDecimal input = new BigDecimal("123456.789");
    String expectedOutput = "123.456,79 €";
    String actualOutput = Helpers.formatBigDecimalCurrency(input);
    assertEquals(expectedOutput, actualOutput);

    assertFalse(true);
  }
}