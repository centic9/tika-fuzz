package org.dstadler.tika.fuzz;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class FuzzTest {
	@Test
	public void testTikaFuzz() {
		Fuzz.fuzzerTestOneInput(new byte[] {});
		Fuzz.fuzzerTestOneInput(new byte[] {1});
		Fuzz.fuzzerTestOneInput(new byte[] {'P', 'K'});
	}

	@Disabled("Local test for verifying a slow run")
	@Test
	public void testSlowUnit() throws IOException {
		Fuzz.fuzzerTestOneInput(FileUtils.readFileToByteArray(new File("slow-unit-9b30ff4d9f28e68033cfe554a0ee373d5ef4f0ca")));
	}
}