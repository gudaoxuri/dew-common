/*
 * Copyright 2020. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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


