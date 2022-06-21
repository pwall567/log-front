package net.pwall.log.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import net.pwall.log.BasicFormatter;
import net.pwall.log.FormattingLogger;
import net.pwall.log.FormattingLoggerFactory;
import net.pwall.log.Logger;
import net.pwall.log.PrintStreamAppender;

public class BasicFormatterTest {

    @Test
    public void shouldSetColouredLevel() {
        BasicFormatter basicFormatter = new BasicFormatter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        FormattingLoggerFactory<BasicFormatter, PrintStreamAppender<BasicFormatter>> baosFactory =
                FormattingLoggerFactory.getFactory(new PrintStreamAppender<>(printStream, basicFormatter));
        baosFactory.setDefaultClock(Clock.fixed(Instant.parse("2022-06-19T14:43:24.000Z"), ZoneOffset.UTC));
        FormattingLogger<?, ?> baosLog = baosFactory.getLogger("NAME");
        assertSame(basicFormatter, baosLog.getFormatter());
        baosLog.info("Coloured");
        assertEquals("14:43:24.000 \u001B[32mINFO \u001B[m NAME: Coloured\n", baos.toString());
        baos.reset();
        assertTrue(basicFormatter.isColouredLevel());
        basicFormatter.setColouredLevel(false);
        baosLog.info("Not coloured");
        assertEquals("14:43:24.000 INFO  NAME: Not coloured\n", baos.toString());
    }

    @Test
    public void shouldSetNameLengthLimit() {
        BasicFormatter basicFormatter = new BasicFormatter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        FormattingLoggerFactory<?, ?> baosFactory =
                FormattingLoggerFactory.getFactory(new PrintStreamAppender<>(printStream, basicFormatter));
        baosFactory.setDefaultClock(Clock.fixed(Instant.parse("2022-06-19T14:43:24.000Z"), ZoneOffset.UTC));
        Logger baosLog = baosFactory.getLogger("ABCDEFGHIJKLMNOP");
        baosLog.info("Default limit");
        assertEquals("14:43:24.000 \u001B[32mINFO \u001B[m ABCDEFGHIJKLMNOP: Default limit\n", baos.toString());
        baos.reset();
        basicFormatter.setNameLengthLimit(4);
        assertEquals(8, basicFormatter.getNameLengthLimit());
        basicFormatter.setNameLengthLimit(12);
        assertEquals(12, basicFormatter.getNameLengthLimit());
        baosLog.info("Limit 12");
        assertEquals("14:43:24.000 \u001B[32mINFO \u001B[m ...HIJKLMNOP: Limit 12\n", baos.toString());
    }

}
