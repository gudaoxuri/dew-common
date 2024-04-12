package com.ecfront.dew.common.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Avg Test.
 *
 * @author gudaoxuri
 */
public class AvgTest {

    private final Accumulator accumulator = new Accumulator();

    /**
     * Test.
     */
    @Test
    public void test() {
        accumulator.addDateValue(1);
        accumulator.addDateValue(2);
        accumulator.addDateValue(3);
        Assertions.assertEquals(2, accumulator.m, 0);
        accumulator.addDateValue(4);
        accumulator.addDateValue(5);
        accumulator.addDateValue(6);
        accumulator.addDateValue(7);
        Assertions.assertEquals(4, accumulator.m, 0);
    }

    static class Accumulator {
        private double m;
        private double s;
        private int n;

        public void addDateValue(double x) {
            n++;
            s = s + 1.0 * (n - 1) / n * (x - m) * (x - m);
            m = m + (x - m) / n;
        }

        public double mean() {
            return m;
        }

        public double var() {
            return s / (n - 1);
        }

        public double stddev() {
            return Math.sqrt(this.var());
        }
    }
}


