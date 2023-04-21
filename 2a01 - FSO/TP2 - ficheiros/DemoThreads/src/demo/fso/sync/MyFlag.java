package demo.fso.sync;

public class MyFlag {
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public MyFlag(boolean flag) {
		this.flag = flag;
	}
	
	public MyFlag() {
		this.flag = false;
	}
}
