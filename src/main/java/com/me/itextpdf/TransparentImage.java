/**
 * 
 */
package com.me.itextpdf;

import java.awt.Color;
import java.awt.Toolkit;
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
public class TransparentImage {

	/**
	 * @param args
	 * @throws DocumentException
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
		String outFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/transparent_img_01.pdf";
		String img_1 = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/12.jpg";
		String img_27 = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/27.jpg";
		String img_28 = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/28.png";
		Rectangle rectangle = new Rectangle(PageSize.A4);
		rectangle.setBackgroundColor(BaseColor.BLUE);
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outFilePath));
		document.open();
		// java.awt.Image awtImage =
		// Toolkit.getDefaultToolkit().createImage(img_27);
		// Image image = Image.getInstance(awtImage, new Color(0xFF, 0, 0xFF),
		// true);
		Image image = Image.getInstance(img_27);
		document.add(image);
		document.close();
		System.out.println("OK...");
	}

}
