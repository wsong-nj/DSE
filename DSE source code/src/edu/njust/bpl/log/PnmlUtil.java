package edu.njust.bpl.log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class PnmlUtil {

	public static void write(ArrayList<Dot> dots, File file, int number) {
		int num = 0;
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("pnml");
		Element net = root.addElement("net");
		net.addAttribute("id", "net" + number);
		net.addAttribute("type",
				"http://www.pnml.org/version-2009/grammar/pnmlcoremodel");
		net.addElement("name").addElement("text").addText(file.getName());
		Element page = net.addElement("page");
		page.addAttribute("id", "n0");
		for (Dot dot : dots) {
			switch (dot.getType()) {
			case Dot.Dot_Place:
				Element place = page.addElement("place");
				place.addAttribute("id", dot.getId());
				break;
			case Dot.Dot_Transition:
				Element transition = page.addElement("transition");
				transition.addAttribute("id", dot.getId());
				break;
			case Dot.Dot_Arc:
				Element arc = page.addElement("arc");
				arc.addAttribute("id", "a" + num++)
						.addAttribute("source", dot.getSource())
						.addAttribute("target", dot.getTarget());
				break;
			}
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
