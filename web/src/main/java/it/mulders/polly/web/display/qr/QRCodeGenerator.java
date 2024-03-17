package it.mulders.polly.web.display.qr;

import static java.util.logging.Level.SEVERE;
import static java.util.stream.Collectors.joining;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@ApplicationScoped
public class QRCodeGenerator {
    private static final Logger log = Logger.getLogger(QRCodeGenerator.class.getName());

    private static final int QR_CODE_DIMENSION = 150;
    public static final String QR_CODE_DIMENSION_VIEWBOX = "0 0 %1$d %1$d".formatted(QR_CODE_DIMENSION);

    private final QRCodeWriter writer = new QRCodeWriter();

    public String generateQRCodeSvgPath(final String text) throws QRGenerationException {
        try {
            var matrix = writer.encode(text, BarcodeFormat.QR_CODE, QR_CODE_DIMENSION, QR_CODE_DIMENSION);
            return convertMatrixToSvgPath(matrix);
        } catch (WriterException e) {
            log.log(SEVERE, e, () -> "Error generating QR code for %s".formatted(text));
            throw new QRGenerationException(e);
        }
    }

    private String convertMatrixToSvgPath(final BitMatrix matrix) {
        return IntStream.range(0, matrix.getHeight())
                .mapToObj(x -> IntStream.range(0, matrix.getWidth())
                        .filter(y -> matrix.get(x, y))
                        .mapToObj(y -> " M%d,%dh1v1h-1z".formatted(x, y))
                        .collect(joining(" ")))
                .collect(joining(" "));
    }
}
