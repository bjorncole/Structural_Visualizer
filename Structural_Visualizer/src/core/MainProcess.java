package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class MainProcess {
	public static void main (String[] args) {
		System.out.println ("Reading source from " + args[0]);
		
		File sourceFile = new File(args[0]);
		
		try {
			
			// TODO: Question - what to do with lines of code between one control structure and another?
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile));
			
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			
			try {
				while((line =bufferedReader.readLine())!=null){
					stringBuffer.append(line).append("\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			StructuredProgram sp = new StructuredProgram(stringBuffer.toString());
			
			//sp.setHighlight(28);
			
			DrawingTemplateBook designBook = new DrawingTemplateBook();
			
			String htmlTop = designBook.basicHTMLHeader();
			
			Integer blockTicker = 1;
			
			String part1 = designBook.renderBoilerplate();
			String part2 = designBook.startGraphic();
			String part3 = "";
			for (NassiShneiderman ns : sp.getDiagrams()) {
				// hold to just the one for now to avoid noise
				//if (blockTicker == 2) {
					part3 = part3 + ns.renderDiagram();
				//}
				blockTicker++;
			}
			String part4 = designBook.closeGraphic();
			String part5 = designBook.closeBoilerplate();
			
			String htmlBottom = designBook.basicHTMLClose();
			
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(args[1]), "utf-8"))) {
				writer.write(htmlTop);
				
				writer.write(part1);
				writer.write(part2);
				writer.write(part3);
				writer.write(part4);
				writer.write(part5);
				
				writer.write(htmlBottom);
			   
				writer.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
}