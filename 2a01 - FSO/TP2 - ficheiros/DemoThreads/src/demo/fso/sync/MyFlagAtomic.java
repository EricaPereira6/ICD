package demo.fso.sync;

import java.util.concurrent.atomic.AtomicBoolean;

public class MyFlagAtomic extends MyFlag {
	private AtomicBoolean flagAtomic;
	
	public MyFlagAtomic() {
		this(false);
	}
	
	public MyFlagAtomic(boolean flag) {
		super(flag);
		
		this.flagAtomic = new AtomicBoolean( flag );
	}
	
	@Override
	public boolean isFlag() {
		return this.flagAtomic.compareAndSet(true, false);
	}

	@Override
	public void setFlag(boolean flag) {
		this.flagAtomic.set(flag);
	}
}
