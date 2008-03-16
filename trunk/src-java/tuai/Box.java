package tuai;

public class Box {

	/**
	 * Generate a box with content.
	 * 
	 * @param content
	 *            The content within the box.
	 */
	public Box(byte[] content) {
		_content = content;
	}

	/**
	 * Get the content.
	 * 
	 * @return Returns the content.
	 */
	public byte[] getContent() {
		return _content;
	}

	private byte[] _content;
}
