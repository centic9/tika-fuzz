package org.dstadler.tika.fuzz;

import org.junit.jupiter.api.Test;

class FuzzTikaTest {
	@Test
	public void testTikaFuzz() {
		FuzzTika.fuzzerTestOneInput(new byte[] {});
		FuzzTika.fuzzerTestOneInput(new byte[] {1});
		FuzzTika.fuzzerTestOneInput(new byte[] {'P', 'K'});
	}
}