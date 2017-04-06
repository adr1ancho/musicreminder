package adrian.musicreminder;

import org.junit.Test;

import static adrian.musicreminder.EventType.HORSE;
import static adrian.musicreminder.EventType.NON_EVENT;
import static adrian.musicreminder.EventType.SOCCER;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EventTypeTest {

	@Test
	public void testParseHorseEvent() {
		EventType type = EventType.parseEventType("1420 xxxxx");
		assertThat(type, is(HORSE));
	}

	@Test
	public void testParseSoccerEvent() {
		EventType type = EventType.parseEventType("1420 S xxxxx");
		assertThat(type, is(SOCCER));
	}

	@Test
	public void testParseNonEventSimple() {
		EventType type = EventType.parseEventType(" xxxxx");
		assertThat(type, is(NON_EVENT));
	}

	//@Test
	public void testParseNonEventCase1() {
		EventType type = EventType.parseEventType("2420 xxxxx");
		assertThat(type, is(NON_EVENT));
	}

	//@Test
	public void testParseNonEventCase2() {
		EventType type = EventType.parseEventType("1460 xxxxx");
		assertThat(type, is(NON_EVENT));
	}
}
