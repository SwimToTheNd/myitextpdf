/**
 * 
 */
package com.me.itextpdf;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * @author BloodFly
 * @date 2018Äê1ÔÂ1ÈÕ
 */
public class ExtractImages {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String inFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/in/excel_2_pdf_01.pdf";
		String outFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/";

		extractImage(inFilePath, outFilePath);
	}

	public static void extractImage(String inFilePath, String outFilePath) throws IOException {
		PdfReader reader = new PdfReader(inFilePath);
		int pageCounts = reader.getNumberOfPages();
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		for (int i = 1; i <= pageCounts; i++) {
			System.out.println("page:" + i);
			parser.processContent(i, new RenderListener() {

				@Override
				public void renderText(TextRenderInfo renderInfo) {
					// TODO Auto-generated method stub

				}

				@Override
				public void renderImage(ImageRenderInfo renderInfo) {
					PdfImageObject imageObject;
					try {
						imageObject = renderInfo.getImage();
						PdfName filter = (PdfName) imageObject.get(PdfName.FILTER);
						String fileType = imageObject.getFileType();
						System.out.println("fileType£º" + fileType);
						String fileName;
						FileOutputStream os;
						System.out.println(filter + "\t" + renderInfo.getRef().getNumber());
						// jpeg
						if (PdfName.DCTDECODE.equals(filter)) {
							System.out.println("jpeg");
							fileName = String.format(outFilePath + "%s%s", renderInfo.getRef().getNumber()*2, ".jpg");
							os = new FileOutputStream(fileName);
							os.write(imageObject.getImageAsBytes());
							os.flush();
							os.close();
						}
						// JPEG2000
						else if (PdfName.JPXDECODE.equals(filter)) {
							System.out.println("JPEG2000");
							fileName = String.format(outFilePath + "%s%s", renderInfo.getRef().getNumber()*2, ".jpg2");
							os = new FileOutputStream(fileName);
							os.write(imageObject.getImageAsBytes());
							os.flush();
							os.close();
						}
						// other
						else {
							System.out.println("png");
							BufferedImage awtImage = renderInfo.getImage().getBufferedImage();
							if (awtImage != null) {
								fileName = String.format(outFilePath + "%s%s", renderInfo.getRef().getNumber()*2, ".png");
								ImageIO.write(awtImage, "png", new FileOutputStream(fileName));
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void endTextBlock() {
					// TODO Auto-generated method stub

				}

				@Override
				public void beginTextBlock() {
					// TODO Auto-generated method stub

				}
			});
		}
	}
}
