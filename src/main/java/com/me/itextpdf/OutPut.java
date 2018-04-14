/**
 * 
 */
package com.me.itextpdf;

import java.awt.image.BufferedImage;
import java.util.Iterator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.Vector;
import com.itextpdf.text.pdf.parser.PdfImageObject.ImageBytesType;

/**
 * @author BloodFly
 * @date 2018Äê1ÔÂ1ÈÕ
 */
public class OutPut {

	/**
	 * print ImageRenderInfo
	 * @param renderInfo
	 */
	public static void print(ImageRenderInfo renderInfo) {
		float area = renderInfo.getArea();
//		BaseColor currentFillColor = renderInfo.getCurrentFillColor();
		Matrix imageCTM = renderInfo.getImageCTM();
//		int mcid = renderInfo.getMcid();
		Vector startPoint = renderInfo.getStartPoint();
		PdfIndirectReference reference = renderInfo.getRef();
		System.out.println("area:[" + area + "]");
//		System.out.println("currentFillColor:[" + currentFillColor + "]");
		System.out.println("imageCTM:\n" + imageCTM);
//		System.out.println("mcid:[" + mcid + "]");
		System.out.println("startPoint:[" + startPoint + "]");
		System.out.println("indirectReference:[" + reference + "]\tNumber:"+reference.getNumber()+"\tgetBytes:"+reference.getBytes()+"\tgetIndRef:"+reference.getIndRef());
	}
	
	/**
	 * print PdfImageObject
	 * @param imageObject
	 */
	public static void print(PdfImageObject imageObject) {
		String fileType = imageObject.getFileType();
		ImageBytesType imageBytesType = imageObject.getImageBytesType();
		System.out.println("fileType:[" + fileType + "]");
		System.out.println("imageBytesType:[" + imageBytesType + "]");
		PdfDictionary dictionary = imageObject.getDictionary();
		System.out.println("dictionary:" + dictionary);
		for (Iterator<?> iterator = dictionary.getKeys().iterator(); iterator.hasNext();) {
			PdfName nameKey = (PdfName) iterator.next();
			PdfObject object = dictionary.get(nameKey);
			System.out.println("nameKey:[" + nameKey + "]\tobject:[" + object + "]\timageObject:"+imageObject.get(nameKey));
			
			if (nameKey.equals(PdfName.COLORSPACE)) {
				System.out.println(PdfReader.getPdfObject(object));
			}
		}
	}
	
	/**
	 * print BufferedImage
	 * @param bi
	 */
	public static void print(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		int type = bi.getType(); // TYPE_INT_BGR
		System.out.println("width:["+width+"]\theight:["+height+""+"]\ttype:["+type+"]");
	}
	
}
