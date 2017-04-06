package adrian.musicreminder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReminderTest {

	private static final DateTimeFormatter DTF = DateTimeFormat.forPattern("HHmm");

	@Mock
	private ReminderSound sound;

	@Test
	public void testWithinReminderRange() {

		String time = DTF.print(new DateTime());

		final Event event = new Event(EventType.HORSE, time + " some event");
		final Reminder reminder = new Reminder()
				.withBeforeRaceStart(170)
				.withfterRaceStart(35)
				.withSound(sound);
		assertThat(reminder.withinTimeRange(event), is(true));
	}
}
