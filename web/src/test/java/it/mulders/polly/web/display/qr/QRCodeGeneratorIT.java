package it.mulders.polly.web.display.qr;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.imageio.ImageIO;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class QRCodeGeneratorIT implements WithAssertions {
    private final QRCodeGenerator generator = new QRCodeGenerator();

    private final QRCodeReader qrReader = new QRCodeReader();

    @Test
    void should_generate_scanable_qr_code()
            throws TranscoderException, ChecksumException, NotFoundException, FormatException, IOException {
        var input = "test";

        try {
            // generate QR Code in SVG format
            var svg =
                    """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="%s" stroke="none" class="qr-code">
                    <path d="%s" />
                </svg>
            """
                            .formatted(
                                    QRCodeGenerator.QR_CODE_DIMENSION_VIEWBOX, generator.generateQRCodeSvgPath(input));

            // convert to bitmap
            var buffer = new ByteArrayOutputStream();
            new PNGTranscoder().transcode(new TranscoderInput(new StringReader(svg)), new TranscoderOutput(buffer));

            // "scan"
            var binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(ImageIO.read(new ByteArrayInputStream(buffer.toByteArray())))));
            var result = qrReader.decode(binaryBitmap);

            // verify
            assertThat(result.getText()).isEqualTo(input);
        } catch (QRGenerationException e) {
            fail("Could not generate QR code", e);
        }
    }
}
