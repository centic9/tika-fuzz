package org.dstadler.tika.fuzz;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class Fuzz {
	private static final Tika tika = new Tika();

	public static void fuzzerTestOneInput(byte[] input) {
		// try to invoke various methods which parse documents/workbooks/slide-shows/...

		try (InputStream str = new ByteArrayInputStream(input)) {
			tika.detect(str);
		} catch (IOException | IllegalArgumentException e) {
			// expected here, Tika throws both types of exceptions here
		}

		try (InputStream str = new ByteArrayInputStream(input)) {
			// using tika.parse() was slow as it creates a new Thread for each iteration
			/*try (Reader reader = tika.parse(str)) {
				char[] bytes = new char[1024];

				// make sure to read all the resulting data
				while (true) {
					int read = reader.read(bytes);
					if (read == -1) {
						break;
					}
				}
			}*/

			tika.parseToString(str);
		} catch (TikaException | IOException | AssertionError e) {
			// expected here, Tika throws both types of exceptions here
		}
	}
}
