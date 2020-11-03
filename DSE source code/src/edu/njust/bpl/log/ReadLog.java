package edu.njust.bpl.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.njust.bpl.structure.EventFlow;

public class ReadLog {

	@SuppressWarnings("unchecked")
	public static List<EventFlow> getTraces(File file) {

		List<EventFlow> traceList = new ArrayList<>();

		SAXReader reader = new SAXReader();

		try {

			Document doc = reader.read(file);

			Element log = doc.getRootElement();

			Iterator<Element> traceIterator = log.elementIterator("trace");

			while (traceIterator.hasNext()) {

				Element trace = traceIterator.next();

				EventFlow eventFlow = new EventFlow();

				for (Element event : (List<Element>) trace.elements("event")) {

					List<Element> traceStrings = event.elements("String");

					if (traceStrings.isEmpty()) {

						traceStrings = event.elements("string");

					}

					for (Element eventString : traceStrings) {

						if (eventString.attribute("key").getValue()
								.equals("concept:name")) {

							String value = eventString.attributeValue("value");

							eventFlow.add(value);
							break;
						}

					}
				}

				boolean flag = true;

				for (EventFlow flow : traceList) {

					if (flow.equals(eventFlow)) {

						flow.setCount(flow.getCount() + 1);

						flag = false;

						break;

					}

				}

				if (flag)

					traceList.add(eventFlow);

			}

		} catch (DocumentException e) {

			e.printStackTrace();
		}

		return traceList;

	}
}
