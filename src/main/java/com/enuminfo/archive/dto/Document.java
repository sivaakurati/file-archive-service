/**
 * 
 */
package com.enuminfo.archive.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

/**
 * @author Kumar
 */
public class Document extends DocumentMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte[] fileData;
	
	public Document(byte[] fileData, String file, Date date, String user) {
        super(file, date, user);
        this.fileData = fileData;
    }

    public Document(Properties properties) {
        super(properties);
    }
    
    public Document(DocumentMetadata metadata) {
        super(metadata.getUuid(), metadata.getFile(), metadata.getDate(), metadata.getUser());
    }

    public byte[] getFileData() {
        return fileData;
    }
    
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    
    public DocumentMetadata getMetadata() {
        return new DocumentMetadata(getUuid(), getFile(), getDate(), getUser());
    }
}
