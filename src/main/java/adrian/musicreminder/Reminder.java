package adrian.musicreminder;

import com.google.common.annotations.VisibleForTesting;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class Reminder {

	private static final Logger LOG = LoggerFactory.getLogger(Reminder.class);

	@Value("${music-reminder.horse-event.before:170}")
	private int beforeRaceStart;

	@Value("${music-reminder.horse-event.after:35}")
	private int afterRaceStart;

	@Value("${music-reminder.horse-event.before:0}")
	private int beforeSoccerStart;

	@Value("${music-reminder.horse-event.after:60*90}")
	private int afterSoccerStart;

	@Autowired
	private ReminderSound sound;

	@Autowired
	private EventFileReader reader;

	@VisibleForTesting
	Reminder withBeforeRaceStart(int beforeRaceStart) {
		this.beforeRaceStart = beforeRaceStart;
		return this;
	}

	@VisibleForTesting
	Reminder withfterRaceStart(int afterRaceStart) {
		this.afterRaceStart = afterRaceStart;
		return this;
	}

	@VisibleForTesting
	Reminder withSound(ReminderSound sound) {
		this.sound = sound;
		return this;
	}

	@Scheduled(cron="*/30 * * * * *")  // FOR USE
//	@Scheduled(cron="*/5 * * * * *") // DEBUG ONLY
	public void run() throws IOException {
		LOG.info("Checking event ...");
		List<Event> events = reader.readEvents();
		reader.writeEvents(events);
		remindEvents(events);
	}

	private void remindEvents(List<Event> events) {
		final long count = events.stream()
				.filter(this::withinTimeRange)
				.count();
		if (count > 0) {
			sound.play();
		}
	}

	boolean withinTimeRange(Event event) {
		if (EventType.HORSE == event.getType()) {
			return withinTimeRange(event, beforeRaceStart, afterRaceStart);
		} else if (EventType.SOCCER == event.getType()) {
			return withinTimeRange(event, beforeSoccerStart, afterSoccerStart);
		}
		return false;
	}

	private boolean withinTimeRange(Event event, int secondsBeforeEvent, int secondsAfterEvent) {
		final Date startMark = new DateTime().minusSeconds(secondsBeforeEvent).toDate();
		final Date endMark = new DateTime().plusSeconds(secondsAfterEvent).toDate();
		final Date eventDate = event.getDate();
		return eventDate.after(startMark) && eventDate.before(endMark);
	}
}
