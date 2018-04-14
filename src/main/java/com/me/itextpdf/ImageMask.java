/**
 * 
 */
package com.me.itextpdf;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author BloodFly
 * @date 2018Äê1ÔÂ2ÈÕ
 */
public class ImageMask {

	/**
	 * @param args
	 * @throws DocumentException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
		String outFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/image_mask_01.pdf";
		String img_1 = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/12.jpg";
		String img_27 = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/27.jpg";
		String img_28 = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/28.png";
		Rectangle rectangle = new Rectangle(PageSize.A4);
		rectangle.setBackgroundColor(BaseColor.BLUE);
		Document document = new Document(rectangle);
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outFilePath));
		document.open();
		Image image_1 = Image.getInstance(img_1);
		image_1.setAbsolutePosition(0, 0);
		document.add(image_1);
		Image image_27 = Image.getInstance(img_27);
		image_27.scalePercent(50, 50);
		System.out.println(img_27.getBytes() + " width:" + image_27.getWidth() + " height:" + image_27.getHeight());
		Image image_28 = Image.getInstance(img_28);
		System.out.println(img_28.getBytes() + " width:" + image_28.getWidth() + " height:" + image_28.getHeight());
		image_28.makeMask();
		image_28.setInverted(false);
		image_27.setImageMask(image_28);
		System.out.println(img_27.getBytes());
		image_27.setAbsolutePosition(0, 0);
		document.add(image_27);
		document.close();
		writer.close();
	}

}
