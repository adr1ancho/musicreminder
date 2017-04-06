package adrian.musicreminder;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventFileReader {

	@Value("${music-reminder.event-file:event.txt}")
	private String filePath;

	@Value("${music-reminder.event-outfile:out.txt}")
	private String outfilePath;

	@VisibleForTesting
	EventFileReader withFilePath(String path) {
		this.filePath = path;
		return this;
	}

	@VisibleForTesting
	EventFileReader withOutFilePath(String path) {
		this.outfilePath = path;
		return this;
	}

	public List<Event> readEvents() throws IOException {
		List<Event> events = new ArrayList<>();
		List<String> lines = FileUtils.readLines(FileUtils.getFile(filePath), Charset.defaultCharset());
		Event event = null;
		for (String l: lines) {
			EventType type = EventType.parseEventType(l);
			if (type != EventType.NON_EVENT) {
				event = new Event(type, l);
				events.add(event);
			} else {
				if (event != null) {
					event.addContent(l);
				}
			}
		}
		return events;
	}

	public void writeEvents(List<Event> events) throws IOException {
		List<Event> list = events.stream()
				.sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
				.collect(Collectors.toList());
		FileUtils.openOutputStream(FileUtils.getFile(outfilePath), false).close();
		for (Event event: list) {
			FileUtils.writeLines(FileUtils.getFile(outfilePath), event.getContent(), true);
		}
	}
}
