package core;

// Represents a block within an NS diagram. Supports collapsing.

abstract public class NassiShneidermanBlock {
	
	protected Integer blockWidth;
	protected Integer blockHeight;
	
	protected Integer headerWidth;
	protected Integer headerHeight;
	
	protected Integer subheaderWidth;
	protected Integer subheaderHeight;
	
	protected Integer blockStartHorizontal;
	protected Integer blockStartVertical;
	
	protected Integer highlightedZone;
	
	abstract public void setBlockHeight(Integer points);
	abstract public void setBlockWidth(Integer points);
	
	abstract public void setHeaderHeight(Integer points);
	abstract public void setHeaderWidth(Integer points);
	
	abstract public void setSubheaderHeight(Integer points);
	abstract public void setSubheaderWidth(Integer points);
	
	abstract public void setBlockStartHorizontal(Integer points);
	abstract public void setBlockStartVertical(Integer points);
	
	abstract public void setHighlight(String location);
	
	abstract public String renderBlock();
	
}
