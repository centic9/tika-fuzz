package org.dstadler.tika.fuzz;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;

public class FuzzTika {
	public static void fuzzerTestOneInput(byte[] input) {
		final Tika tika = new Tika();

		// try to invoke various methods which parse documents/workbooks/slide-shows/...
		try (TikaInputStream str = TikaInputStream.get(new ByteArrayInputStream(input))) {
			tika.detect(str, "fuzzed input");
		} catch (IOException e) {
			// expected here
		}
	}
}
