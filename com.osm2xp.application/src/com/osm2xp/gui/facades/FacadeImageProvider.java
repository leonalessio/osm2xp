package com.osm2xp.gui.facades;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.model.facades.FacadeDefinition;
import com.osm2xp.model.facades.FacadeDefinitionParser;
import com.osm2xp.model.facades.Wall;

import math.geom2d.Point2D;
import net.npe.dds.DDSReader;

public class FacadeImageProvider {

	private static ImageRegistry imageRegistry = new ImageRegistry();

	public static void reloadImgs() {
		imageRegistry.dispose();
		imageRegistry = new ImageRegistry();
	}

	public static Image getPreviewImage(File facadeFile) {
		Image img = imageRegistry.get(facadeFile.getAbsolutePath());
		if (img != null) {
			return img;
		}
		if (!facadeFile.isFile()) {
			return null;
		}
		FacadeDefinition definition = FacadeDefinitionParser.parse(facadeFile);

		Collection<String> textures = definition.getProperties().get("TEXTURE");
		if (textures == null || textures.isEmpty()) {
			return null;
		}
		String imgFileName = textures.iterator().next();
		File imgFile = new File(facadeFile.getParentFile(), imgFileName);
		if (!imgFile.isFile()) {
			int lastIdx = imgFileName.lastIndexOf('.');
			if (lastIdx > 0) {
				imgFileName = imgFileName.substring(0, lastIdx) + ".dds";
				imgFile = new File(imgFile.getParentFile(), imgFileName);
			}
		}
		if (!imgFile.isFile()) {
			return null;
		}
		List<Wall> walls = definition.getWalls();
		if (walls.isEmpty()) {
			return null;
		}

		List<Double> hCoordsList = new ArrayList<Double>(walls.get(0).getxCoords());
		List<Double> vCoordsList = new ArrayList<Double>(walls.get(0).getyCoords());
		Collections.sort(hCoordsList);
		Collections.sort(vCoordsList);
		Collections.reverse(vCoordsList);
		if (hCoordsList.size() > 1 && vCoordsList.size() > 1) {
			Image commonImg = imageRegistry.get(imgFile.getAbsolutePath());
			if (commonImg == null) {
				if (imgFile.getName().endsWith(".dds")) {
					commonImg = readDDS(imgFile);
				} else {
					commonImg = readStandard(imgFile);
				}
			}
			if (commonImg == null) {
				return null;
			}
			Rectangle bounds = commonImg.getBounds();
			Point2D srcSize = new Point2D(commonImg.getBounds().width, commonImg.getBounds().height);
			if (definition.getTexSize() != null) {
				srcSize = definition.getTexSize();
			}
			hCoordsList = fixCoordsList(hCoordsList, (int) srcSize.x());
			vCoordsList = fixCoordsList(vCoordsList, (int) srcSize.y());
			int srcX = (int) Math.round(hCoordsList.get(0) * bounds.width);
			int srcW = (int) Math.round(hCoordsList.get(hCoordsList.size() - 1) * bounds.width - srcX);

			int srcY = (int) Math.round((1.0 - vCoordsList.get(0)) * bounds.height);
			int h = (int) Math.round((1.0 - vCoordsList.get(vCoordsList.size() - 1)) * bounds.height - srcY);

			final Image destImage = new Image(Display.getDefault(), srcW, h);

			final GC g = new GC(destImage);
			g.drawImage(commonImg, srcX, srcY, srcW, h, 0, 0, srcW, h);
			g.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));

			for (int i = 1; i < hCoordsList.size() - 1; i++) {
				int xCoord = (int) Math.round(hCoordsList.get(i) * bounds.width) - srcX;
				g.drawLine(xCoord, 0, xCoord, h);
			}

			for (int i = 1; i < vCoordsList.size() - 1; i++) {
				int yCoord = (int) Math.round((1.0 - vCoordsList.get(i)) * bounds.height) - srcY;
				g.drawLine(0, yCoord, bounds.width, yCoord);
			}
			g.dispose();
			imageRegistry.put(facadeFile.getAbsolutePath(), destImage);
			return destImage;
		}
		return null;

	}

	protected static Image readStandard(File imgFile) {
		try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(imgFile))) {
			ImageData[] data = new ImageLoader().load(input);
			Image result = new Image(Display.getDefault(), data[0]);
			imageRegistry.put(imgFile.getAbsolutePath(), result);
			return result;
		} catch (Exception e) {
			Osm2xpLogger.log(e);
			return null;
		}
	}

	private static Image readDDS(File imgFile) {
		try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(imgFile))) {
			
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			
			int[] pixels = DDSReader.read(buffer, DDSReader.ARGB, 0);
			int width = DDSReader.getWidth(buffer);
			int height = DDSReader.getHeight(buffer);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			image.setRGB(0, 0, width, height, pixels, 0, width);
			return new Image(Display.getDefault(), convertToSWT(image));
		} catch (IOException e) {
			// Best effort
		}
		return null;
	}
	
	/**
     * Converts a buffered image to SWT <code>ImageData</code>.
     *
     * @param bufferedImage  the buffered image (<code>null</code> not
     *         permitted).
     *
     * @return The image data.
     */
    public static ImageData convertToSWT(BufferedImage bufferedImage) {
        if (bufferedImage.getColorModel() instanceof DirectColorModel) {
            DirectColorModel colorModel
                    = (DirectColorModel) bufferedImage.getColorModel();
            PaletteData palette = new PaletteData(colorModel.getRedMask(),
                    colorModel.getGreenMask(), colorModel.getBlueMask());
            ImageData data = new ImageData(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), colorModel.getPixelSize(),
                    palette);
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[bufferedImage.getColorModel().getPixelSize() / 8];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    int pixel = palette.getPixel(new RGB(pixelArray[0],
                            pixelArray[1], pixelArray[2]));
                    data.setPixel(x, y, pixel);
                }
            }
            return data;
        }
        else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
            IndexColorModel colorModel = (IndexColorModel)
                    bufferedImage.getColorModel();
            int size = colorModel.getMapSize();
            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            colorModel.getReds(reds);
            colorModel.getGreens(greens);
            colorModel.getBlues(blues);
            RGB[] rgbs = new RGB[size];
            for (int i = 0; i < rgbs.length; i++) {
                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF,
                        blues[i] & 0xFF);
            }
            PaletteData palette = new PaletteData(rgbs);
            ImageData data = new ImageData(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), colorModel.getPixelSize(),
                    palette);
            data.transparentPixel = colorModel.getTransparentPixel();
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++) {
                for (int x = 0; x < data.width; x++) {
                    raster.getPixel(x, y, pixelArray);
                    data.setPixel(x, y, pixelArray[0]);
                }
            }
            return data;
        }
        return null;
    }


	private static List<Double> fixCoordsList(List<Double> coordsList, int width) {
		boolean needReCalc = false;
		for (Double val : coordsList) {
			if (Math.round(val) > 1) {
				needReCalc = true;
				break;
			}
		}
		if (needReCalc) {
			List<Double> resList = new ArrayList<Double>();
			for (Double current : coordsList) {
				resList.add(current / width);
			}
			return resList;
		}
		return coordsList;
	}
}
