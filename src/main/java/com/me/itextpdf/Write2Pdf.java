/**
 * 
 */
package com.me.itextpdf;

import java.awt.event.MouseWheelEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author BloodFly
 * @date 2018Äê1ÔÂ1ÈÕ
 */
public class Write2Pdf {

	/**
	 * @param args
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
		String outFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/write2pdf_01.pdf";
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outFilePath));
		document.open();
		Image image1 = Image.getInstance("E:/work/JAVA/workspace/myitextpdf/src/resources/in/1.jpg");
		image1.setAbsolutePosition(0, 0);
		Image image2 = Image.getInstance("E:/work/JAVA/workspace/myitextpdf/src/resources/in/png_pic_01.png");
		image2.setAbsolutePosition(0, 0);
		document.add(image1);
		document.add(image2);
		document.close();
		
		System.out.println("complete");
	}

}
