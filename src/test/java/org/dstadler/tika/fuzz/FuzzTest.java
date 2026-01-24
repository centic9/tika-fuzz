package org.dstadler.tika.fuzz;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.audio.MidiParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.parser.video.FLVParser;
import org.apache.tika.sax.ToTextContentHandler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

class FuzzTest {
	@Test
	void testTikaFuzz() {
		Fuzz.fuzzerTestOneInput(new byte[] {});
		Fuzz.fuzzerTestOneInput(new byte[] {1});
		Fuzz.fuzzerTestOneInput(new byte[] {'P', 'K'});
	}

	@Disabled("Local test for verifying a slow run")
	@Test
	void testSlowUnit() throws IOException {
		Fuzz.fuzzerTestOneInput(FileUtils.readFileToByteArray(new File("slow-unit-9b30ff4d9f28e68033cfe554a0ee373d5ef4f0ca")));
	}

	@Disabled("Local test to try to reproduce issues")
	@Test
	void reproduce() throws Throwable {
		Parser[] parsers = new Parser[] {
				new AudioParser(),
				new MidiParser(),
				new Mp3Parser(),
				new MP4Parser(),
				new FLVParser()
		};
		Parser p = new AutoDetectParser(parsers);
		try {
			parseOne(p, FileUtils.readFileToByteArray(new File("./oom-7824929c120c5d137ce65e11e2604df12952e29a")));
		} catch (TikaException | SAXException | IOException e) {
			//swallow
		}
	}

	private static void parseOne(Parser parser, byte[] bytes) throws Throwable {
		parseBytes(parser, bytes, new ParseContext());
		parseFile(parser, bytes, new ParseContext());
	}

	private static void parseBytes(Parser parser, byte[] bytes, ParseContext parseContext) throws Throwable {
		ContentHandler handler = new ToTextContentHandler();
		//make sure that other parsers cannot be invoked
		parseContext.set(Parser.class, parser);
		//try first with bytes
		try (TikaInputStream is = TikaInputStream.get(bytes)) {
			parser.parse(is, handler, new Metadata(), parseContext);
		}
	}

	private static void parseFile(Parser parser, byte[] bytes, ParseContext parseContext) throws Throwable {
		ContentHandler handler = new ToTextContentHandler();
		//make sure that other parsers cannot be invoked
		parseContext.set(Parser.class, parser);
		try (TikaInputStream tis = TikaInputStream.get(bytes)) {
			//force writing to tmp file
			tis.getPath();
			parser.parse(tis, handler, new Metadata(), parseContext);
		}
	}
}