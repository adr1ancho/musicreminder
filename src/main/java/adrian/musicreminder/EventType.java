package adrian.musicreminder;

public enum EventType {
	NON_EVENT,
	HORSE,
	SOCCER;

	private static final String REGEX_TIME = "([0-1][0-9]|2[0-3])[0-5][0-9]";

	static EventType parseEventType(final String line) {
		if (line.matches("^" + REGEX_TIME + "\\s*S\\s*\\S.*$")) {
			return SOCCER;
		} else if (line.matches("^" + REGEX_TIME + "\\s*\\S.*$")) {
			return HORSE;
		}
		return NON_EVENT;
	}
}
