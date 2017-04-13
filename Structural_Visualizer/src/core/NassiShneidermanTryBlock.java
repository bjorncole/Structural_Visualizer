package core;

import java.util.ArrayList;

// Represents an if block in the NS notation.

public class NassiShneidermanTryBlock extends NassiShneidermanBlock {
	
	Integer numberCatches;
	ArrayList<Integer> catchHeight;
	
	Integer tryBodyHeight;
	
	// TODO: It looks like try blocks are not drawing correctly
	
	public NassiShneidermanTryBlock() {
		blockHeight = 50;
		blockWidth = 20;
		headerHeight = 50;
		headerWidth = 400;
		subheaderHeight = 50;
		subheaderWidth = 400;
		blockStartHorizontal = 10;
		blockStartVertical = 10;
		highlightedZone = 0;
		
		tryBodyHeight = 0;
		
		numberCatches = 0;
		
		catchHeight = new ArrayList<Integer>();
	}
	
	public void setBlockHeight(Integer points){
		blockHeight = points;
	}
	public void setBlockWidth(Integer points){
		blockWidth = points;
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
	
	public void setTryBodyHeight(Integer points) {
		tryBodyHeight = points;
	}
	
	public void setCatchHeight(Integer points, Integer index){
		if (catchHeight.size() >= index + 1) {
			if (catchHeight.get(index) != null) {
				catchHeight.set(index, points);
			}
			else {
				catchHeight.add(index, points);
			}
		}
		else {
			// pad with zeros until index hit
			for (int j = catchHeight.size() - 1; j < index - 1; j++) {
				catchHeight.add(0);
			}
			catchHeight.add(points);
		}
	}
	
	public void setBlockStartHorizontal(Integer points){
		blockStartHorizontal = points;
	}
	public void setBlockStartVertical(Integer points){
		blockStartVertical = points;
	}
	
	public void setHighlight(String location) {
		switch (location) {
			case "Body":
				highlightedZone = 1;
				break;
			default:
				highlightedZone = 0;
				break;
		}
		
	}
	
	public Integer getNumberCatches() {
		return numberCatches;
	}
	
	public void setNumberCatches(Integer catches) {
		numberCatches = catches;
	}
	
	public String renderBlock() {
		
		// TODO: Guard against block getting too small
		
		StringBuffer drawingString = new StringBuffer();
		
		// build the outer area for the for loop
		
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:#ffffff;fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect" + new Integer(1).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + ((Integer) (blockWidth)).toString() + "\"\n");
		drawingString.append("\t\theight=\"" + ((Integer) (blockHeight)).toString() + "\"\n");
		drawingString.append("\t\tx=\"" + blockStartHorizontal.toString() + "\"\n");
		drawingString.append("\t\ty=\"" + blockStartVertical.toString() + "\"\n");
		drawingString.append("/>");
		
		// build the inner area for the rest
		
		String highlightBlockColor = "";
		
		switch (highlightedZone) {
			case 1:
				highlightBlockColor = "#ff0000;";
				break;
			case 0:
				highlightBlockColor = "#ffffff;";
				break;
		}
		
		drawingString.append("<rect\n");
		drawingString.append("style=\"fill:" +
				highlightBlockColor
				+ "fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
		drawingString.append("\t\tid=\"rect" + new Integer(2).toString() + "\"\n");
		drawingString.append("\t\twidth=\"" + getBodyWidth().toString() + "\"\n");
		drawingString.append("\t\theight=\"" + getBodyHeight().toString() + "\"\n");
		drawingString.append("\t\tx=\"" + getBodyStartX().toString() + "\"\n");
		drawingString.append("\t\ty=\"" + getBodyStartY().toString() + "\"\n");
		drawingString.append("/>");
		
		for (int i = 0; i < numberCatches; i++) {
			String catchString = "";
			
			drawingString.append("<rect\n");
			drawingString.append("style=\"fill:" +
					highlightBlockColor
					+ "fill-opacity:0.3;stroke:#000000;stroke-width:7;stroke-linejoin:round;stroke-miterlimit:4;stroke-dasharray:none;stroke-opacity:1\"\n");
			drawingString.append("\t\tid=\"rect" + new Integer(2).toString() + "\"\n");
			drawingString.append("\t\twidth=\"" + getCatchWidth(i).toString() + "\"\n");
			drawingString.append("\t\theight=\"" + getCatchHeight(i).toString() + "\"\n");
			drawingString.append("\t\tx=\"" + getCatchStartX(i).toString() + "\"\n");
			drawingString.append("\t\ty=\"" + getCatchStartY(i).toString() + "\"\n");
			drawingString.append("/>");
		}
		
		return drawingString.toString();
	}
	
	/*** Returns x coordinate of the loop body
	 * 
	 * @return
	 */
	
	public Integer getBodyStartX() {
		return blockStartHorizontal + headerHeight;
	}
	
	/*** Returns y coordinate of the loop body
	 * 
	 * @return
	 */
	
	public Integer getBodyStartY() {
		return blockStartVertical + headerHeight;
	}
	
	/*** Returns width of the loop body
	 * 
	 * @return
	 */
	
	public Integer getBodyWidth() {
		return blockWidth - headerHeight;
	}
	
	/*** Returns height of the loop body
	 * 
	 * @return
	 */
	
	public Integer getBodyHeight() {
		return tryBodyHeight;
	}
	
	public Integer getCatchStartX(Integer index) {
		return getBodyStartX();
	}
	
	// TODO: Use catch height to properly locate the start
	
	public Integer getCatchStartY(Integer index) {
		Integer cummulativeHeight = tryBodyHeight;
		
		for (int i = 0; i < index - 1; i++) {
			cummulativeHeight = cummulativeHeight + catchHeight.get(index);
		}
		
		return blockStartVertical + cummulativeHeight + headerHeight * (index + 1);
	}
	
	public Integer getCatchWidth(Integer index) {
		return getBodyWidth();
	}
	
	public Integer getCatchHeight(Integer index) {
		return catchHeight.get(index);
	}
	
	public String toString() {
		return "Try block (height, width) = [" + blockHeight + ", " + blockWidth + "], " +
				"(x,y) = [" + blockStartHorizontal + ", " + blockStartVertical + "]";
	}
}
