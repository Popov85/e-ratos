package ua.edu.ratos.service.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Charset detector based on juniversalchardet project
 * @link https://code.google.com/archive/p/juniversalchardet/
 * @link https://stackoverflow.com/questions/3759356/what-is-the-most-accurate-encoding-detector?noredirect=1&lq=1
 */
@Slf4j
@Component
public class CharsetDetector {

    /**
     * Detects a file's encoding based on input bytes
     * The file might contain only ASCII characters, making it difficult for the detector to distinguish between encodings.
     * @param inputStream input file
     * @return detected encoding
     * @throws IOException
     */
    public String detectEncoding(@NonNull InputStream inputStream) throws IOException {
        UniversalDetector detector = new UniversalDetector(null);
        int n;
        byte[] buf = new byte[4096];
        while ((n = inputStream.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, n);
        }
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        if (encoding == null) {
            encoding = "ASCII";//Fall back to ASCII encoding
        }
        log.debug("Detected encoding :: {} ", encoding);
        detector.reset();
        return encoding;
    }
}
