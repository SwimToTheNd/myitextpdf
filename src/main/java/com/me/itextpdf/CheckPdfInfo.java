/**
 * 
 */
package com.me.itextpdf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.itextpdf.text.pdf.parser.PdfImageObject.ImageBytesType;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import static com.me.itextpdf.OutPut.*;

/**
 * @author BloodFly
 * @date 2017年12月31日
 */
public class CheckPdfInfo {

	/**
	 * @param args
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		String inFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/out/excel_2_pdf_01.pdf";
//		String inFilePath = "E:/work/JAVA/workspace/myitextpdf/src/resources/in/excel_2_pdf_01.pdf";

		checkPdfInfo(inFilePath);
	}

	public static void checkPdfInfo(String inFilePath) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(inFilePath);
		int xrefs = reader.getXrefSize();
		for (int i = 0; i < xrefs; i++) {
			PdfObject pdfObject = reader.getPdfObject(i);
			if (pdfObject == null || !pdfObject.isStream()) {
				continue;
			}
			PRStream stream = (PRStream) pdfObject;
			for (Iterator<?> iterator = stream.getKeys().iterator(); iterator.hasNext();) {
				PdfName key = (PdfName) iterator.next();
				PdfObject value = stream.get(key);

				// key:/Subtype value:/Image
				if (PdfName.SUBTYPE.equals(key) && PdfName.IMAGE.equals(value)) {
					System.out.println("\nxrefNo:" + i);
					PdfImageObject imageObject = new PdfImageObject(stream);
					print(imageObject);
				}

			}
		}
//		pagePrase(reader);
	}

	/**
	 * @param reader
	 * @throws IOException
	 */
	private static void pagePrase(PdfReader reader) throws IOException {
		// 文档解析器
		PdfReaderContentParser readerContentParser = new PdfReaderContentParser(reader);
		int pageCounts = reader.getNumberOfPages();
		for (int i = 1; i <= pageCounts; i++) {
			readerContentParser.processContent(i, new RenderListener() {

				@Override
				public void renderText(TextRenderInfo renderInfo) {
					// TODO Auto-generated method stub

				}

				@Override
				public void renderImage(ImageRenderInfo renderInfo) {
					float area = renderInfo.getArea();
//					BaseColor currentFillColor = renderInfo.getCurrentFillColor();
					Matrix imageCTM = renderInfo.getImageCTM();
//					int mcid = renderInfo.getMcid();
					Vector startPoint = renderInfo.getStartPoint();
					PdfIndirectReference reference = renderInfo.getRef();
					System.out.println("area:[" + area + "]");
//					System.out.println("currentFillColor:[" + currentFillColor + "]");
					System.out.println("imageCTM:\n" + imageCTM);
//					System.out.println("mcid:[" + mcid + "]");
					System.out.println("startPoint:[" + startPoint + "]");
					System.out.println("indirectReference:[" + reference + "]\tNumber:" + reference.getNumber()
							+ "\tgetBytes:" + reference.getBytes() + "\tgetIndRef:" + reference.getIndRef());
					System.out.println(">>>>>>>>>>");
					try {
						PdfImageObject imageObject = renderInfo.getImage();
						String fileType = imageObject.getFileType();
						System.out.println("fileType:[" + fileType + "]");
						ImageBytesType imageBytesType = imageObject.getImageBytesType();
						System.out.println("imageBytesType:[" + imageBytesType + "]");

						PdfDictionary dictionary = imageObject.getDictionary();
						System.out.println("dictionary:" + dictionary);
						for (Iterator<?> iterator = dictionary.getKeys().iterator(); iterator.hasNext();) {
							PdfName nameKey = (PdfName) iterator.next();
							PdfObject object = dictionary.get(nameKey);
							System.out.println("nameKey:[" + nameKey + "]\tobject:[" + object + "]\timageObject:"
									+ imageObject.get(nameKey));

							if (nameKey.equals(PdfName.COLORSPACE)) {
								System.out.println(PdfReader.getPdfObject(object));
							}
						}
						renderInfo.getImage();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("========================================\n");

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
