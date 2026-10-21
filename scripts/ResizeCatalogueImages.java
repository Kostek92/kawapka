import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Downscales downloaded catalogue images for bundling in the app.
 *
 * Invoked by scripts/generate-catalogue. It is not part of the Android app.
 * Every readable image in the source directory is written to the destination
 * directory as a PNG with the same base name, scaled to fit within maxPx.
 */
public final class ResizeCatalogueImages {
    private ResizeCatalogueImages() {}

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: ResizeCatalogueImages <source-dir> <dest-dir> <max-px>");
            System.exit(2);
        }

        System.setProperty("java.awt.headless", "true");

        File sourceDir = new File(args[0]);
        File destDir = new File(args[1]);
        int maxPx = Integer.parseInt(args[2]);

        if (!destDir.isDirectory() && !destDir.mkdirs()) {
            throw new IllegalStateException("Cannot create " + destDir);
        }

        File[] files = sourceDir.listFiles(File::isFile);
        if (files == null || files.length == 0) {
            System.err.println("No images to resize in " + sourceDir);
            System.exit(1);
        }

        int written = 0;
        for (File source : files) {
            BufferedImage image = ImageIO.read(source);
            if (image == null) {
                System.err.println("SKIP " + source.getName() + ": unsupported image format");
                continue;
            }

            BufferedImage scaled = scaleToFit(image, maxPx);
            String baseName = source.getName().replaceFirst("\\.[^.]+$", "");
            File target = new File(destDir, baseName + ".png");
            if (!ImageIO.write(scaled, "png", target)) {
                System.err.println("SKIP " + source.getName() + ": no PNG writer");
                continue;
            }
            written++;
        }

        if (written == 0) {
            System.err.println("No images were resized");
            System.exit(1);
        }
        System.out.println("Resized " + written + " images to fit " + maxPx + "px");
    }

    private static BufferedImage scaleToFit(BufferedImage source, int maxPx) {
        double scale = Math.min(1.0, (double) maxPx / Math.max(source.getWidth(), source.getHeight()));
        int width = Math.max(1, (int) Math.round(source.getWidth() * scale));
        int height = Math.max(1, (int) Math.round(source.getHeight() * scale));

        boolean alpha = source.getColorModel().hasAlpha();
        BufferedImage target =
                new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = target.createGraphics();
        try {
            graphics.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(source, 0, 0, width, height, null);
        } finally {
            graphics.dispose();
        }
        return target;
    }
}
