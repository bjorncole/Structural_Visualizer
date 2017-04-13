package core;

// Represents an if block in the NS notation.

public class NassiShneidermanForBlock extends NassiShneidermanBlock {
	
	public NassiShneidermanForBlock() {
		blockHeight = 50;
		blockWidth = 20;
		headerHeight = 50;
		headerWidth = 400;
		subheaderHeight = 50;
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
			case "Body":
				highlightedZone = 1;
				break;
			default:
				highlightedZone = 0;
				break;
		}
		
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
				highlightBlockColor = "#00ff00;";
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
		drawingString.append("\t\twidth=\"" + ((Integer) (blockWidth - headerHeight)).toString() + "\"\n");
		drawingString.append("\t\theight=\"" + ((Integer) (blockHeight - headerHeight * 2)).toString() + "\"\n");
		drawingString.append("\t\tx=\"" + getBodyStartX().toString() + "\"\n");
		drawingString.append("\t\ty=\"" + getBodyStartY().toString() + "\"\n");
		drawingString.append("/>");
		
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
		return blockHeight - headerHeight * 2;
	}
	
	public Integer getHeaderStartX() {
		return 0;
	}
	
	public Integer getHeaderStartY() {
		return 0;
	}
	
	public Integer getHeaderHeight() {
		return 0;
	}
	
	public Integer getHeaderWeight() {
		return 0;
	}
	
	public String toString() {
		return "For block (height, width) = [" + blockHeight + ", " + blockWidth + "], " +
				"(x,y) = [" + blockStartHorizontal + ", " + blockStartVertical + "]";
	}
	
}
