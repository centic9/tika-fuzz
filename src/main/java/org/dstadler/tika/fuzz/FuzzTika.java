package org.dstadler.tika.fuzz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;

public class FuzzTika {
	public static void fuzzerTestOneInput(byte[] input) {
		final Tika tika = new Tika();

		// try to invoke various methods which parse documents/workbooks/slide-shows/...

		try (InputStream str = new ByteArrayInputStream(input)) {
			tika.detect(str);
		} catch (IOException | IllegalArgumentException e) {
			// expected here
		}

		try (InputStream str = new ByteArrayInputStream(input)) {
			tika.parse(str);
		} catch (IOException | IllegalArgumentException e) {
			// expected here
		}
	}
}
