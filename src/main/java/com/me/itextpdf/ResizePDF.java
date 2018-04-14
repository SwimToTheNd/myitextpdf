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
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.sun.image.codec.jpeg.*;
import static com.me.itextpdf.OutPut.*;

/**
 * @author BloodFly
 * @date 2018年1月1日
 */
public class ResizePDF {

	static final float FACTOR = 0.25f;

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

		List<Integer> xrefs = new ArrayList<>();
		List<Integer> imageMasks = new ArrayList<>();

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
					xrefs.add(i);
				}

				if (PdfName.SMASK.equals(key)) {
					PRIndirectReference reference = (PRIndirectReference) value;
					if (!imageMasks.contains(reference.getNumber())) {
						imageMasks.add(reference.getNumber());
					}
					System.out.println(imageMasks);
				}
			}
			System.out.println();
		}

		for (Integer xrefNo : xrefs) {
			System.out.println("xrefNo:" + xrefNo);
			if (imageMasks.contains(xrefNo)) {
				continue;
			}
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
			ImageIO.write(bImage, fileType, new FileOutputStream("E:/work/JAVA/workspace/myitextpdf/src/resources/out/" + xrefNo + "." + fileType));
//			ImageIO.write(bImage, fileType, bos);

			JPEGImageEncoder jpegImageEncoder = JPEGCodec.createJPEGEncoder(bos);
			JPEGEncodeParam jpegEncodeParam = jpegImageEncoder.getDefaultJPEGEncodeParam(bi);
			jpegEncodeParam.setDensityUnit(jpegEncodeParam.DENSITY_UNIT_DOTS_INCH);
			jpegImageEncoder.setJPEGEncodeParam(jpegEncodeParam);
			jpegEncodeParam.setQuality(0.25f, false);
//			jpegEncodeParam.setXDensity(30);
//			jpegEncodeParam.setYDensity(30);
			jpegImageEncoder.encode(bi, jpegEncodeParam);
			bi.flush();
			
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
			stream.put(PdfName.LENGTH, new PdfNumber(bos.toByteArray().length));
			stream.put(PdfName.WIDTH, new PdfNumber(width));
			stream.put(PdfName.HEIGHT, new PdfNumber(height));
			// stream.put(PdfName.TYPE, PdfName.XOBJECT);
			// stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
			// stream.put(PdfName.FILTER, PdfName.DCTDECODE);
			// stream.put(PdfName.WIDTH, new PdfNumber(scaleWidth));
			// stream.put(PdfName.HEIGHT, new PdfNumber(scaleHeight));
			// stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
			// stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
			System.out.println();
		}

		// 将修改的内容输出为新的PDF
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outFile));
		stamper.close();
	}

}
