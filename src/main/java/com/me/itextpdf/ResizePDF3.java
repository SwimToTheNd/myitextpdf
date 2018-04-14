/**
 * 
 */
package com.me.itextpdf;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import static com.me.itextpdf.OutPut.*;

/**
 * @author BloodFly
 * @date 2018年1月1日
 */
public class ResizePDF3 {

	static final float FACTOR = 0.5f;

	/**
	 * @param args
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		String inFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/in/excel_2_pdf_01.pdf";
		String outFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/excel_2_pdf_01.pdf";

		resizeImageByXRef(inFilePath, outFilePath);
	}

	public static void resizeImageByXRef(String inFile, String outFile) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(inFile);

		List<PdfObject> images = new ArrayList<>();
		List<Integer> xrefs = new ArrayList<>();
		int xrefSize = reader.getXrefSize();
		for (int i = 0; i < xrefSize; i++) {
			PdfObject object = reader.getPdfObject(i);
			if (object == null || !object.isStream()) {
				continue;
			}
			PRStream stream = (PRStream) object;
			
			for (Iterator<?> iterator = stream.getKeys().iterator(); iterator.hasNext();) {
				PdfName key = (PdfName) iterator.next();
				PdfObject value = stream.get(key);
				// System.out.println("key:" + key + "\tvalue:" + value);
				// /Type /XObject
				if (PdfName.TYPE.equals(key) && PdfName.XOBJECT.equals(value)) {
				}

				// key:/Subtype value:/Image
				if (PdfName.SUBTYPE.equals(key) && PdfName.IMAGE.equals(value)) {
					// xref size Gets the number of xref objects.
					images.add(object);
					xrefs.add(i);
					break;
				}
				if (PdfName.SMASK.equals(key)) {
					
				}
			}
			System.out.println();
		}

		for (Integer xrefNo : xrefs) {
			System.out.println("xrefNo:" + xrefNo);
			PRStream stream = (PRStream) reader.getPdfObject(xrefNo);
			PdfImageObject imageObject = new PdfImageObject(stream);
			BufferedImage bi = imageObject.getBufferedImage();
			if (bi == null) {
				continue;
			}
			print(imageObject);
			String fileType = imageObject.getFileType();
			int width = bi.getWidth();
			int height = bi.getHeight();
			int type = bi.getType(); // TYPE_INT_BGR
			print(bi);
			int scaleWidth = (int) (width * FACTOR);
			int scaleHeight = (int) (height * FACTOR);
			BufferedImage bImage = new BufferedImage(scaleWidth, scaleHeight, type);
			AffineTransform aTransform = AffineTransform.getScaleInstance(FACTOR, FACTOR);
			Graphics2D g = bImage.createGraphics();
			g.drawRenderedImage(bi, aTransform);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bImage, fileType,
					new FileOutputStream("E:/work/JAVA/workspace/myitextpdf/src/resources/out/" + xrefNo + "." + fileType));
			ImageIO.write(bi, fileType, bos);
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
			// stream.setData(bos.toByteArray());
			// stream.setDataRaw(bos.toByteArray());
			stream.putAll(originDic);
			stream.remove(PdfName.LENGTH);
			stream.put(PdfName.WIDTH, new PdfNumber(scaleWidth));
			stream.put(PdfName.HEIGHT, new PdfNumber(scaleHeight));
		}

		// 将修改的内容输出为新的PDF
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
		stamper.close();
	}

}
