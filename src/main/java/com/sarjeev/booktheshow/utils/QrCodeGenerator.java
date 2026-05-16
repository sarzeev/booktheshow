package com.sarjeev.booktheshow.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sarjeev.booktheshow.exceptions.QrCodeGenerationException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class QrCodeGenerator {

    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;
    private static final String PNG_FORMAT = "PNG";
    private static final String DATA_URI_PREFIX = "data:image/png;base64,";

    private final QRCodeWriter qrCodeWriter = new QRCodeWriter();

    public String generateBase64Png(String payload) {
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ImageIO.write(image, PNG_FORMAT, outputStream);
                return DATA_URI_PREFIX + Base64.getEncoder().encodeToString(outputStream.toByteArray());
            }
        } catch (WriterException | IOException ex) {
            throw new QrCodeGenerationException("Unable to generate QR code", ex);
        }
    }

    public byte[] decodeBase64Image(String qrCodeData) {
        String normalized = qrCodeData.startsWith(DATA_URI_PREFIX)
                ? qrCodeData.substring(DATA_URI_PREFIX.length())
                : qrCodeData;
        return Base64.getDecoder().decode(normalized.getBytes(StandardCharsets.UTF_8));
    }
}
