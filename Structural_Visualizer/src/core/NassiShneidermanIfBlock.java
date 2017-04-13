package core;

import java.util.ArrayList;
import java.util.List;

// Represents an if block in the NS notation.

public class NassiShneidermanIfBlock extends NassiShneidermanBlock {
	
	public NassiShneidermanIfBlock() {
		blockHeight = 50;
		blockWidth = 20;
		headerHeight = 50;
		headerWidth = 400;
		subheaderHeight = 30;
		subheaderWidth = 400;
		blockStartHorizontal = 10;
		blockStartVertical = 10;
		highlightedZone = 0;
	}
	
	public void setBlockHeight(Integer points){
		blockHeight = points;
	}
	public void setBlockWidth(Integer points){
		blockWidth = points;
		headerWidth = points;
		subheaderWidth = points;
	}
	
	public void setHeaderHeight(Integer points){
		headerHeight = points;
	}
	public void setHeaderWidth(Integer points){
		headerWidth = points;
	}
	
	public void setSubheaderHeight(Integer points){
		subheaderHeight = points;
	}
	public void setSubheaderWidth(Integer points){
		subheaderWidth = points;
	}
	
	public void setBlockStartHorizontal(Integer points){
		blockStartHorizontal = points;
	}
	public void setBlockStartVertical(Integer points){
		blockStartVertical = points;
	}
	
	public void setHighlight(String location) {
		switch (location) {
			case "True":
				highlightedZone = 1;
				break;
			case "False":
				highlightedZone = 2;
				break;
			default:
				highlightedZone = 0;
				break;
		}
		
	}
	
	public String renderBlock() {
		
		// TODO: Guard against block getting too small
		
		StringBuffer drawingString = new StringBuffer();
		
		// build the "roof" with triangles
		
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:#ffffff;fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect" + new Integer(1).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + headerWidth.toString() + "\"\n");
		drawingString.append("\t\theight=\"" + headerHeight.toString() + "\"\n");
		drawingString.append("\t\tx=\"" + blockStartHorizontal.toString() + "\"\n");
		drawingString.append("\t\ty=\"" + blockStartVertical.toString() + "\"\n");
		drawingString.append("/>");
		
		drawingString.append("<path\n");
		drawingString.append("style=\"fill:#00ff00;fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"triangle" + new Integer(2).toString() + "\"\n");
		drawingString.append("\t\td=\"M " +
			blockStartHorizontal.toString() + "," + ((Integer) (blockStartVertical + headerHeight)).toString() + " " +
			blockStartHorizontal.toString() + "," + ((Integer) (blockStartVertical + headerHeight * 1 / 2)).toString() + " " + 
			((Integer) (blockStartHorizontal + headerWidth / 4)).toString() + "," + ((Integer) (blockStartVertical + headerHeight)).toString() +
			" Z\"\n");
		drawingString.append("/>");
		
		drawingString.append("<path\n");
		drawingString.append("style=\"fill:#ff0000;fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"triangle" + new Integer(3).toString() + "\"\n");
		drawingString.append("\t\td=\"M " +
			((Integer) (blockStartHorizontal + headerWidth)).toString() + "," + ((Integer) (blockStartVertical + headerHeight)).toString() + " " +
			((Integer) (blockStartHorizontal + headerWidth)).toString() + "," + ((Integer) (blockStartVertical + headerHeight * 1 / 2)).toString() + " " + 
			((Integer) (blockStartHorizontal + headerWidth * 3 / 4)).toString() + "," + ((Integer) (blockStartVertical + headerHeight)).toString() +
			" Z\"\n");
		drawingString.append("/>");
		
		/*
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:#ffffff;fill-opacity:1;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect+" + new Integer(1).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + subheaderWidth.toString() + "\"\n");
		drawingString.append("\t\theight=\"" + subheaderHeight.toString() + "\"\n");
		drawingString.append("\t\tx=\"" + blockStartHorizontal.toString() + "\"\n");
		drawingString.append("\t\ty=\"" + ((Integer) Integer.sum(blockStartVertical, headerHeight)).toString() + "\"\n");
		drawingString.append("/>");
		*/
		
		// build the branch headers
		
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:#ffffff;fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect" + new Integer(4).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + ((Integer) (subheaderWidth / 2)).toString() + "\"\n");
		drawingString.append("\t\theight=\"" + subheaderHeight.toString() + "\"\n");
		drawingString.append("\t\tx=\"" + blockStartHorizontal.toString() + "\"\n");
		drawingString.append("\t\ty=\"" + ((Integer) (blockStartVertical + headerHeight)).toString() + "\"\n");
		drawingString.append("/>");
		
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:#ffffff;fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect" + new Integer(5).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + ((Integer) (subheaderWidth / 2)).toString() + "\"\n");
		drawingString.append("\t\theight=\"" + subheaderHeight.toString() + "\"\n");
		drawingString.append("\t\tx=\"" + ((Integer) (subheaderWidth / 2 + blockStartHorizontal)).toString() + "\"\n");
		drawingString.append("\t\ty=\"" + ((Integer) (blockStartVertical + headerHeight)).toString() + "\"\n");
		drawingString.append("/>");
		
		// here are stubs for body
		
		String highlightTrueColor = "";
		String highlightFalseColor = "";
		
		switch (highlightedZone) {
			case 1:
				highlightTrueColor = "#ff0000;";
				highlightFalseColor = "#ffffff;";
				break;
			case 2:
				highlightTrueColor = "#ffffff;";
				highlightFalseColor = "#ff0000;";
				break;
			case 0:
				highlightTrueColor = "#ffffff;";
				highlightFalseColor = "#ffffff;";
				break;
		}
		
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:" +
			highlightTrueColor
				+ "fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect" + new Integer(6).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + getColumnWidth().toString() + "\"\n");
		drawingString.append("\t\theight=\"" + getColumnHeight().toString() + "\"\n");
		drawingString.append("\t\tx=\"" + blockStartHorizontal.toString() + "\"\n");
		drawingString.append("\t\ty=\"" + ((Integer) (blockStartVertical + headerHeight + subheaderHeight)).toString() + "\"\n");
		drawingString.append("/>");
		
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:" +
			highlightFalseColor
				+ "fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect" + new Integer(7).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + getColumnWidth().toString() + "\"\n");
		drawingString.append("\t\theight=\"" + getColumnHeight().toString() + "\"\n");
		drawingString.append("\t\tx=\"" + ((Integer) (subheaderWidth / 2 + blockStartHorizontal)).toString() + "\"\n");
		drawingString.append("\t\ty=\"" + ((Integer) (blockStartVertical + headerHeight + subheaderHeight)).toString() + "\"\n");
		drawingString.append("/>");
		
		return drawingString.toString();
	}
	
	/*** Returns x coordinate of true side upper-left corner
	 * 
	 * @return
	 */
	
	public Integer getTrueSideStartX() {
		return blockStartHorizontal;
	}
	
	/*** Returns y coordinate of true side upper-left corner
	 * 
	 * @return
	 */
	
	public Integer getTrueSideStartY() {
		return blockStartVertical + headerHeight + subheaderHeight;
	}
	
	/*** Returns x coordinate of false side upper-left corner
	 * 
	 * @return
	 */
	
	public Integer getFalseSideStartX() {
		return (subheaderWidth / 2 + blockStartHorizontal);
	}
	
	/*** Returns y coordinate of false side upper-left corner
	 * 
	 * @return
	 */
	
	public Integer getFalseSideStartY() {
		return blockStartVertical + headerHeight + subheaderHeight;
	}
	
	public Integer getColumnWidth() {
		return (subheaderWidth / 2);
	}
	
	public Integer getColumnHeight() {
		return blockHeight - headerHeight - subheaderHeight;
	}
	
	public String toString() {
		return "If block (height, width) = [" + blockHeight + ", " + blockWidth + "], " +
				"(x,y) = [" + blockStartHorizontal + ", " + blockStartVertical + "]";
	}
	
}
