package adrian.musicreminder;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.rhythm.Rhythm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.staccato.ReplacementMapPreprocessor;
import rx.Observable;
import rx.Observer;

import java.util.HashMap;
import java.util.Map;



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

	protected void sampleLindenmayerSystem() {

		// Specify the transformation rules for this Lindenmayer system
		Map rules = new HashMap() {{
			put("Cmajw", "Cmajw Fmajw");
			put("Fmajw", "Rw Bbmajw");
			put("Bbmajw", "Rw Fmajw");
			put("C5q", "C5q G5q E6q C6q");
			put("E6q", "G6q D6q F6i C6i D6q");
			put("G6i+D6i", "Rq Rq G6i+D6i G6i+D6i Rq");
			put("axiom", "axiom V0 I[Flute] Rq C5q V1 I[Tubular_Bells] Rq Rq Rq G6i+D6i V2 I[Piano] Cmajw E6q " +
					"V3 I[Warm] E6q G6i+D6i V4 I[Voice] C5q E6q");
		}};

		// Set up the ReplacementMapPreprocessor to iterate 3 times
		// and not require brackets around replacements
		ReplacementMapPreprocessor rmp = ReplacementMapPreprocessor.getInstance();
		rmp.setReplacementMap(rules);
		rmp.setIterations(4);
		rmp.setRequireAngleBrackets(false);

		// Create a Pattern that contains the L-System axiom
		Pattern axiom = new Pattern("T120 " + "V0 I[Flute] Rq C5q "
				+ "V1 I[Tubular_Bells] Rq Rq Rq G6i+D6i "
				+ "V2 I[Piano] Cmajw E6q "
				+ "V3 I[Warm] E6q G6i+D6i "
				+ "V4 I[Voice] C5q E6q");

		Player player = new Player();
		LOG.debug("{}", rmp.preprocess(axiom.toString(), null));
		player.play(axiom);

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
