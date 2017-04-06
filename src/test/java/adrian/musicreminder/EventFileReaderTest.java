package adrian.musicreminder;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class EventFileReaderTest {

	private static final Logger LOG = LoggerFactory.getLogger(EventFileReaderTest.class);

	@Test
	public void testReadFile() throws IOException {
		LOG.debug("{}", getClass().getResource("samplefile.txt").getPath());
		EventFileReader reader = new EventFileReader().withFilePath(getClass().getResource("samplefile.txt").getPath());
		List<Event> events = reader.readEvents();
		assertThat(events.size(), is(3));
		assertThat(events.get(0).getContent().get(0), is("1420 blah blah blah"));
		assertThat(events.get(1).getContent().get(0), is("1350 S ddfefefefe"));
		assertThat(events.get(2).getContent().get(0), is("1640 dfdfefef"));
	}
}
