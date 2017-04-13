package core;

// A class representing a sample book of key SVG elements
// and other useful helpers

public class DrawingTemplateBook {
	
	/*** Spit out a basic HTML header
	 * 
	 * @return
	 */
	
	public String basicHTMLHeader() {
		StringBuffer htmlBuilder = 
			new StringBuffer();
		
		htmlBuilder.append("<!DOCTYPE html>\n");
		htmlBuilder.append("<html>\n");
		htmlBuilder.append("<body>\n");
		
		return htmlBuilder.toString();
	}
	
	/*** Spit out a basic HTML close
	 * 
	 * @return
	 */
	
	public String basicHTMLClose() {
		StringBuffer htmlBuilder = 
			new StringBuffer();
		
		htmlBuilder.append("</body>\n");
		htmlBuilder.append("</html>\n");
		
		return htmlBuilder.toString();
	}
	
	/*** Spit out some boilerplate to start the SVG
	 * 
	 * @return some boilerplate text
	 */
	
	public String renderBoilerplate() {
		
		StringBuffer boilerPlateBuilder = 
				new StringBuffer();
		
		boilerPlateBuilder.append("<svg\n");
		boilerPlateBuilder.append("\txmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n");
		boilerPlateBuilder.append("\txmlns:cc=\"http://creativecommons.org/ns#\"\n");
		boilerPlateBuilder.append("\txmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
		boilerPlateBuilder.append("\txmlns:svg=\"http://www.w3.org/2000/svg\"\n");
		boilerPlateBuilder.append("\txmlns=\"http://www.w3.org/2000/svg\"\n");
		boilerPlateBuilder.append("\txmlns:sodipodi=\"http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd\"\n");
		boilerPlateBuilder.append("\txmlns:inkscape=\"http://www.inkscape.org/namespaces/inkscape\"\n");
		boilerPlateBuilder.append("\twidth=\"2100mm\"\n");
		boilerPlateBuilder.append("\theight=\"4970mm\"\n");
		boilerPlateBuilder.append("\tid=\"svg2\"\n");
		boilerPlateBuilder.append("\tversion=\"1.1\"\n");
		boilerPlateBuilder.append("\tinkscape:version=\"0.91 r13725\"\n");
		boilerPlateBuilder.append(">\n");
		
		return boilerPlateBuilder.toString();
	}
	
	/*** Spit out closing of the boilerplate tag
	 * 
	 * @return
	 */
	
	public String closeBoilerplate() {
		
		return "</svg>";
	}
	
	/*** Spit out a start to the graphic area
	 * 
	 * @return
	 */
	
	public String startGraphic() {
		return "<g id=\"layer1\">\n";
	}
	
	/*** Spit out the closing of the graphic area
	 * 
	 * @return
	 */
	
	public String closeGraphic() {
		return "</g>";
	}
}
