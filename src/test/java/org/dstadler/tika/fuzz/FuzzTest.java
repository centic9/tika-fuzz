package org.dstadler.tika.fuzz;

import org.junit.jupiter.api.Test;

class FuzzTest {
	@Test
	public void testTikaFuzz() {
		Fuzz.fuzzerTestOneInput(new byte[] {});
		Fuzz.fuzzerTestOneInput(new byte[] {1});
		Fuzz.fuzzerTestOneInput(new byte[] {'P', 'K'});
	}
}