import java.math.BigDecimal;

public class SmallPoint {
	
	BigDecimal x, y;
	
	public SmallPoint() {
		this(new BigDecimal(0), new BigDecimal(0));
	}
	
	public SmallPoint(BigDecimal x, BigDecimal y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(BigDecimal x, BigDecimal y) {
		this.x = x;
		this.y = y;
	}
}
