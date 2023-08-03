
package net.sf.robocode.test.robots;
import net.sf.robocode.test.helpers.RobocodeTestBed;
import org.junit.Assert;
import robocode.control.events.TurnEndedEvent;
public class TestHttpAttack extends RobocodeTestBed {
	private boolean securityExceptionOccurred;
	@Override
	public String getRobotNames() {
		return "tested.robots.HttpAttack,sample.Target";
	}
	@Override
	public void onTurnEnded(TurnEndedEvent event) {
		super.onTurnEnded(event);
		final String out = event.getTurnSnapshot().getRobots()[0].getOutputStreamSnapshot();
		if (out.contains("java.lang.SecurityException:")) {
			securityExceptionOccurred = true;	
		}	
	}
	@Override
	protected void runTeardown() {
		Assert.assertTrue("Socket connection is not allowed", securityExceptionOccurred);
	}
	@Override
	protected int getExpectedErrors() {
		return 1;
	}
}
