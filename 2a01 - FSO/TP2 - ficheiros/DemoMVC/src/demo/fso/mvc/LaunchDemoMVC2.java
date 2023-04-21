package demo.fso.mvc;

import java.awt.EventQueue;

public class LaunchDemoMVC2 {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DemoMVC window = new DemoMVC();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
