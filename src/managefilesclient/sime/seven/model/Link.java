package managefilesclient.sime.seven.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is used to represent links that can be sent to client along with a
 * response.
 * 
 * @author Simachew
 *
 */
@XmlRootElement
public class Link {
	/**
	 * A property to represent the link, or URI that represents an entity in the
	 * API
	 */
	private String uri;

	/**
	 * A property to represent the relation of this link to the entity that it
	 * is attached to.
	 */
	private String rel;
	
	public Link(){
		
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

}
