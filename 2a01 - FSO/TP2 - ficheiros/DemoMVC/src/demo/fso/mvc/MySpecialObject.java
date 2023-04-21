package demo.fso.mvc;

public class MySpecialObject {
	
	private final int mySpecialAttribute;
	
	public MySpecialObject(int mySpecialAttribute) {
		this.mySpecialAttribute = mySpecialAttribute;
	}
	
	public String getDescription() {
		return String.format( "Special attribute = %d", this.mySpecialAttribute );
	}
	
	@Override
	public String toString() {
		return String.format( "MySpecialObject(%d)", this.mySpecialAttribute );
	}

}
