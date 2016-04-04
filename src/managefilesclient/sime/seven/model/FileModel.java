package managefilesclient.sime.seven.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Simachew
 * Model class to represent a single file and other attributes 
 * related to a file.
 */
@XmlRootElement
public class FileModel {

	/**
	 * The file part of the model
	 */
	@XmlElement(required = true)
	private String fileName;
	
	/**
	 * List of links related to the file part 
	 */
	@XmlElement(required=true)
	List<Link> links = new ArrayList<Link>();
	
	public FileModel(){
		
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(String uri, String rel){
		Link link = new Link();
		link.setUri(uri);
		link.setRel(rel);
		links.add(link);
	}

	
	
	
}
