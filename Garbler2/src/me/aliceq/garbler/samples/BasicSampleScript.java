/*
 * The MIT License
 *
 * Copyright 2016 Alice Quiros <email@aliceq.me>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.aliceq.garbler.samples;

import me.aliceq.garbler.GarblerScript;
import me.aliceq.heatmap.HeatMap;
import me.aliceq.heatmap.HeatMapAnalysis;

/**
 *
 * @author Alice Quiros <email@aliceq.me>
 */
public class BasicSampleScript extends GarblerScript {

    long startTime = 0;

    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onComplete(String context) {
        System.out.println("[" + (System.currentTimeMillis() - startTime) + "ms] " + context);
    }

    @Override
    public void preIterate(String context) {
        // Extract the initial character
        char seed = (Character) HeatMapAnalysis.randomFromCDF(next("FirstChar", context));
        buffer += seed;

        // And find the word length
        HeatMap map = HeatMapAnalysis.trim(next("WordLength", context), 0.05f); // Trim low values
        int length = (Integer) HeatMapAnalysis.randomFromCDF(map);
        iterations = length - 2;    // Room for an ending
    }

    @Override
    public void onIterate(String context) {
        // Extract the next letter and add it
        Character next = (Character) HeatMapAnalysis.randomFromCDF(next("Influence", context));
        if (next != null) {
            buffer += next;
        }
    }

    @Override
    public void postIterate(String context) {
        // See if an ending exists 
        HeatMap map = next("WordEnding", context);
        if (map != null) {
            buffer += (String) HeatMapAnalysis.randomKey(map);
        }
    }
}
