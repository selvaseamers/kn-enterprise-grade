package kn.service.citylist.be.util;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;

import java.util.ArrayList;
import java.util.List;

public class MockedAppender extends AbstractAppender {

	public MockedAppender() {
		super("MockedAppender", null, null, true, Property.EMPTY_ARRAY);
	}

	public List<String> message = new ArrayList<>();

	@Override
	public void append(LogEvent event) {
		message.add(event.getMessage().getFormattedMessage());
	}

}
