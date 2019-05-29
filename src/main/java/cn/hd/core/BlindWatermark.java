/*
 * Copyright (c) 2019 ww23(https://github.com/ww23/BlindWatermark).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hd.core;

import cn.hd.core.dencoder.*;
import cn.hd.core.converter.Converter;
import cn.hd.core.converter.DctConverter;
import cn.hd.core.converter.DftConverter;


/**
 * @author ww23
 */
public class BlindWatermark {

    private static final String FOURIER = "f";
    private static final String COSINE = "c";
    private static final String IMAGE = "i";
    private static final String TEXT = "t";
    private static final String ROOT_PATH = "/Users/linxiaohang/Documents/GitHub/watermark-test/";

    public static void main(String[] args) {

        if (args.length < 4) {
            // Encoder
            Converter converter = new DctConverter();
//            Encoder encoder = new TextEncoder(converter);
//            encoder.encode(ROOT_PATH + "jmu.png", "u HangDie ip 0.0.0.0", ROOT_PATH + "DCT-TEST.png");
            // Decoder
            Decoder decoder = new Decoder(converter);
            decoder.decode(ROOT_PATH + "DCT-TEST-1.png", ROOT_PATH + "1-Watermark.png");
            return;
        }

        Converter converter = null;
        String option = args[1].substring(1);

        if (option.contains(FOURIER)) {
            converter = new DftConverter();
        } else if (option.contains(COSINE)) {
            converter = new DctConverter();
        } else {
            help();
        }

        switch (args[0]) {
            case "encode":
                Encoder encoder = null;
                if (option.contains(IMAGE)) {
                    encoder = new ImageEncoder(converter);
                } else if (option.contains(TEXT)) {
                    encoder = new TextEncoder(converter);
                } else {
                    help();
                }
                assert encoder != null;
                encoder.encode(args[2], args[3], args[4]);
                break;
            case "decode":
                Decoder decoder = new Decoder(converter);
                decoder.decode(args[2], args[3]);
                break;
            default:
                help();
        }
    }

    private static void help() {
        System.out.println("Usage: java -jar BlindWatermark.jar <commands> [args...] \n" +
                "   commands: \n" +
                "       encode <option> <input> <watermark> <output>\n" +
                "       decode <option> <input> <output>\n" +
                "   encode options: \n" +
                "       -c discrete cosine transform\n" +
                "       -f discrete fourier transform\n" +
                "       -i image watermark\n" +
                "       -t text  watermark\n" +
                "   decode options: \n" +
                "       -c discrete cosine transform\n" +
                "       -f discrete fourier transform\n" +
                "   example: \n" +
                "       encode -ft foo.png test bar.png" +
                "       decode -f  foo.png bar.png"
        );
        System.exit(-1);
    }
}