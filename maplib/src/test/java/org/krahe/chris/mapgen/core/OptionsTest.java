package org.krahe.chris.mapgen.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Envelope;

class OptionsTest {

    @Test
    void simpleBboxTest() throws Options.OptionsException {
        Envelope envelope = Options.testAndNormalizeBbox("10,10,8,12");
        Assertions.assertEquals(12.0, envelope.getMaxY());
    }

    @Test
    void nanBboxTest() {
        Assertions.assertThrows(Options.OptionsException.class, () -> Options.testAndNormalizeBbox("X,10,8,12"));
    }

    @Test
    void shortBboxTest() {
        Assertions.assertThrows(Options.OptionsException.class, () -> Options.testAndNormalizeBbox("10,10,8"));
    }

    @Test
    void nullBboxTest() {
        Options.OptionsException e = Assertions.assertThrows(Options.OptionsException.class, () ->
                Options.testAndNormalizeBbox(null));
        Assertions.assertTrue(e.getMessage().contains("required"));
    }

    @Test
    void rangeBboxTest() {
        Options.OptionsException e = Assertions.assertThrows(Options.OptionsException.class, () ->
                Options.testAndNormalizeBbox("201,10,8,12"));
        Assertions.assertTrue(e.getMessage().contains("range"));
    }

    @Test
    void flipBboxTest() throws Options.OptionsException {
        Envelope envelope = Options.testAndNormalizeBbox("10,12,8,10");
        Assertions.assertEquals(12.0, envelope.getMaxY());
    }

    @Test
    void lineTest() {
        Options.OptionsException e = Assertions.assertThrows(Options.OptionsException.class, () ->
                Options.testAndNormalizeBbox("10,12,8,12"));
        Assertions.assertTrue(e.getMessage().contains("max"));
    }
}