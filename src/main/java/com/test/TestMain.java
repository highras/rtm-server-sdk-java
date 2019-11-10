package com.test;

import com.fpnn.ErrorRecorder;
import com.rtm.RTMClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class TestMain {

    public interface ITestCase {
        void startTest(byte[] fileBytes);
        void stopTest();
    }

    private static ITestCase testCase;

    public static void main(String[] args) {
        System.out.println(new String("rtm test with main!"));

        RTMClient.RTMRegistration.register();
        byte[] fileBytes = new LoadFile().read("key/java.jpeg");
        testCase = new TestCase();

        if (testCase != null) {
            testCase.startTest(fileBytes);
        }
    }

    static class LoadFile {
        /**
         * @param {String} derPath
         */
        public byte[] read(String derPath) {
            File f = new File(derPath);

            if (!f.exists()) {
                System.err.println(new String("file not exists! path: ").concat(f.getAbsolutePath()));
                return null;
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(f));
                int buf_size = 1024;
                byte[] buffer = new byte[buf_size];
                int len = 0;

                while (-1 != (len = in.read(buffer, 0, buf_size))) {
                    bos.write(buffer, 0, len);
                }
                return bos.toByteArray();
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            } finally {
                try {
                    in.close();
                    bos.close();
                } catch (Exception ex) {
                    ErrorRecorder.getInstance().recordError(ex);
                }
            }
            return null;
        }
    }
}
