package adrian.musicreminder;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Observer;

@Component
public class ReminderSound {

	private static final Logger LOG = LoggerFactory.getLogger(ReminderSound.class);

	@Async
	public void play() {
		sampleAdvancedRhythms();
	}

	protected void sampleAdvancedRhythms() {
		RhythmCommand command = new RhythmCommand(HystrixCommandGroupKey.Factory.asKey("reminder-sound"));
		Observable<Void> o = command.toObservable();
		o.subscribe(new Observer<Void>() {
			@Override
			public void onCompleted() {
				// nothing
			}

			@Override
			public void onError(Throwable e) {
				LOG.error("RhythmCommand failed", e);
			}

			@Override
			public void onNext(Void aVoid) {
				// nothing
			}
		});
	}

	private static class RhythmCommand extends HystrixCommand<Void> {

		protected RhythmCommand(HystrixCommandGroupKey group) {
			super(group);
		}

		@Override
		protected Void run() throws Exception {
			Rhythm rhythm = new Rhythm()
					.addLayer("O..oO...O..oOO..") // This is Layer 0
					.addLayer("..S...S...S...S.")
					.addLayer("````````````````")
					.addLayer("...............+") // This is Layer 3
					.addOneTimeAltLayer(3, 3, "...+...+...+...+") // Replace Layer 3 with this string on the 4th (count from 0) measure
					.setLength(4); // Set the length of the rhythm to 4 measures
			new Player().play(rhythm.getPattern().repeat(1));
			return null;
		}
	}
}
