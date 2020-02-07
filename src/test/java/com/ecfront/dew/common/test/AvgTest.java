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

import org.junit.Assert;
import org.junit.Test;

/**
 * Avg Test.
 *
 * @author gudaoxuri
 */
public class AvgTest {

    private Accumulator accumulator = new Accumulator();

    /**
     * Test.
     */
    @Test
    public void test() {
        accumulator.addDateValue(1);
        accumulator.addDateValue(2);
        accumulator.addDateValue(3);
        Assert.assertEquals(2,accumulator.m,0);
        accumulator.addDateValue(4);
        accumulator.addDateValue(5);
        accumulator.addDateValue(6);
        accumulator.addDateValue(7);
        Assert.assertEquals(4,accumulator.m,0);
    }

    static class Accumulator {
        private double m;
        private double s;
        private int N;

        public void addDateValue(double x) {
            N++;
            s = s + 1.0 * (N - 1) / N * (x - m) * (x - m);
            m = m + (x - m) / N;
        }

        public double mean() {
            return m;
        }

        public double var() {
            return s / (N - 1);
        }

        public double stddev() {
            return Math.sqrt(this.var());
        }
    }
}


