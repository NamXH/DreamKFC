package component;

import java.awt.*;
import javax.swing.*;

public class JPanelExtend extends JPanel {

	private Image backgroundImage = null;

	public JPanelExtend() {
		super();
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if (backgroundImage != null) {
			g.drawImage(backgroundImage,0,0,this);
		}
	}
}
