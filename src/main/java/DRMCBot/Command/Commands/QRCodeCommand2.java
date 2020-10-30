package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeCommand2 implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("請輸入連結！").queue();
            return;
        }
        BufferedImage qrCode = createQRCode(ctx.getArgs().get(0), 512, 512, 0);
        String path = generateQRCodeToPath(qrCode);
        File file = new File(path);
        ctx.getChannel().sendMessage("生成結果：").addFile(file).queue();
        file.deleteOnExit();

    }

    public BufferedImage createQRCode(String contents,int width,int height,int margin) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, CharacterSetECI.UTF8);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, margin);

        BarcodeFormat format = BarcodeFormat.QR_CODE;
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(contents, format, width, height, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        MatrixToImageConfig config = new MatrixToImageConfig(Color.black.getRGB(), Color.white.getRGB());
        BufferedImage qrcode = MatrixToImageWriter.toBufferedImage(matrix, config);
        return qrcode;
    }

    public String generateQRCodeToPath(BufferedImage bufferedImage) {
        String fileFullPath = "C:\\Users\\Kay\\Desktop\\Java\\DRMC-Bot\\" + "qrcode" + "." + "png";
        JSONObject data = new JSONObject();
        boolean result;
        try {
            result = ImageIO.write(bufferedImage, "png", new File(fileFullPath));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileFullPath;
    }

    @Override
    public String getName() {
        return "qrcode2";
    }
}
