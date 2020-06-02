package gov.usgs.wma.qw;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

import gov.usgs.wma.qw.CustomStringToArrayConverter;

public class CustomStringToArrayConverterTest {

	CustomStringToArrayConverter conv = new CustomStringToArrayConverter();

	@Test
	public void convertTest() {
		//NPE
		assertArrayEquals(new String[0],conv.convert(null));

		//Happy paths
		assertArrayEquals(new String[0], conv.convert(""));

		assertArrayEquals(new String[]{"abc"}, conv.convert("abc"));

		assertArrayEquals(new String[]{"a,b,c"}, conv.convert("a,b,c"));

		assertArrayEquals(new String[]{"a","b,c"}, conv.convert("a;b,c"));
	}
}
