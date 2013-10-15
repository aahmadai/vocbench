package org.fao.aoscs.client.widgetlib.shared.panel;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;

public class SimpleBaloonPanel extends PopupPanel {

	// decides if balloon should hide after some time or not
	private boolean shouldHide = true;

	// how long will take the balloon to show up
	private final int DURATION = 1200;

	// how long the balloon will be visible - used only if shouldHide==true
	private final  int HIDE_DELAY  = 4000;

	public SimpleBaloonPanel(boolean shouldHide) {
		super(true);
		this.shouldHide = shouldHide;
		setAutoHideEnabled(true);
		setAnimationEnabled(true);
		addStyleName("baloonPanel");
		
	}

	public void show() {
		// some sample widget will be content of the balloon
		//HTML text = new HTML(baloonText, false);
		//text.setWordWrap(false);
		//setWidget(text);
		BaloonAnimation showBaloon = new BaloonAnimation();
		showBaloon.run(DURATION);
		super.show();

		if (shouldHide)
		{
			BaloonAnimation hideAnim = new BaloonAnimation(false);
			// run hide animation after some time
			hideAnim.run(DURATION, Duration.currentTimeMillis() + HIDE_DELAY);
			// after animation wil end, the balloon must be also hidden and deteached from the page
			Timer t = new Timer() {
				@Override
				public void run() {
					SimpleBaloonPanel.this.hide();
				}
			};
			t.schedule(HIDE_DELAY + DURATION);
		}
	}
	/** animation which will change opacity of the balloon depending on the show value
	 * if it will be false: balloon will start to disappear
	 * if it will be true: balloon will start to appear
	 */
	class BaloonAnimation extends Animation {
		boolean show = true;

		BaloonAnimation(boolean show) {
			super();
			this.show = show;
		}

		public BaloonAnimation() {
			this(true);
		}

		@Override
		protected void onUpdate(double progress) {
			double opacityValue = progress;
			if (!show) {
				opacityValue = 1.0 - progress;
			}
			SimpleBaloonPanel.this.getElement().getStyle().setOpacity(
					opacityValue);
		}
	}
}
