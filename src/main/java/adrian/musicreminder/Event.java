package adrian.musicreminder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {

	private final DateTimeFormatter DTF = DateTimeFormat.forPattern("yyyy-MM-dd HHmm");
	private final DateTimeFormatter TODAY = DateTimeFormat.forPattern("yyyy-MM-dd");

	private final EventType type;
	private final Date date;
	private final List<String> contentLines;

	public Event(EventType type, String l) {
		this.type = type;
		this.date = DTF.parseDateTime(TODAY.print(new DateTime()) + " " + l.substring(0, 4)).toDate();
		this.contentLines = new ArrayList<>();
		addContent(l);
	}

	public EventType getType() {
		return type;
	}

	public Date getDate() {
		return date;
	}

	public List<String> getContent() {
		return contentLines;
	}

	public void addContent(String content) {
		contentLines.add(content);
	}
}
