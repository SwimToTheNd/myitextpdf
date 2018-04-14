/**
 * 
 */
package com.me.itextpdf;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * @author BloodFly
 * @date 2018年1月1日
 */
public class ResizePDF2 {

	static final float FACTOR = 0.5f;

	/**
	 * @param args
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		String inFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/in/excel_2_pdf_01.pdf";
		String outFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/excel_2_pdf_02.pdf";
		List<Integer> xrefs = new ArrayList<>();
		resizePdfByRendenerListener(inFilePath, outFilePath, xrefs);
	}

	public static void resizePdfByRendenerListener(String fileIn, String fileOut, List<Integer> xrefs)
			throws IOException, DocumentException {
		PdfReader reader = new PdfReader(fileIn);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(fileOut));
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		int pageCounts = reader.getNumberOfPages();
		for (int i = 1; i <= pageCounts; i++) {
			parser.processContent(i, new RenderListener() {

				@Override
				public void renderText(TextRenderInfo renderInfo) {

				}

				@Override
				public void renderImage(ImageRenderInfo renderInfo) {
					PdfIndirectReference reference = renderInfo.getRef();
					int xrefNo = reference.getNumber();
					if (!xrefs.contains(xrefNo)) {
						xrefs.add(xrefNo);
					} else {
						return;
					}
					PRStream stream = (PRStream) reader.getPdfObject(xrefNo);
					System.out.println("xrefNo:" + xrefNo + " stream:" + stream);
					PdfImageObject imageObject;
					try {
						imageObject = renderInfo.getImage();
						BufferedImage bi = imageObject.getBufferedImage();
						int width = bi.getWidth();
						int height = bi.getHeight();
						int type = bi.getType();
						String fileType = imageObject.getFileType();
						int scaleWidth = (int) (width * FACTOR);
						int scaleHeight = (int) (height * FACTOR);
						BufferedImage bImage = new BufferedImage(scaleWidth, scaleHeight, type);
						AffineTransform aTransform = AffineTransform.getScaleInstance(FACTOR, FACTOR);
						Graphics2D g = bImage.createGraphics();
						g.drawRenderedImage(bi, aTransform);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(bImage, fileType, bos);
						PdfDictionary originDic = new PdfDictionary();
						for (Iterator<?> iterator = stream.getKeys().iterator(); iterator.hasNext();) {
							PdfName key = (PdfName) iterator.next();
							PdfObject value = stream.get((PdfName) key);
							originDic.put(key, value);
						}
						// 清除原图片内容
						stream.clear();
						// 重新设置图片内容 false 不压缩
						stream.setData(bos.toByteArray(), false, PRStream.NO_COMPRESSION);
						stream.putAll(originDic);
						stream.put(PdfName.WIDTH, new PdfNumber(scaleWidth));
						stream.put(PdfName.HEIGHT, new PdfNumber(scaleHeight));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void endTextBlock() {

				}

				@Override
				public void beginTextBlock() {

				}
			});
		}
		stamper.close();
	}
}
