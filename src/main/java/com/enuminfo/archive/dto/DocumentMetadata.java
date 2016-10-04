/**
 * 
 */
package com.enuminfo.archive.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Kumar
 */
public class DocumentMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PROP_UUID = "uuid";
    public static final String PROP_USER = "user-name";
    public static final String PROP_FILE = "file";
    public static final String PROP_DATE = "date";
    
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);
    
    protected String uuid;
    protected String file;
    protected Date date;
    protected String user;
    
    public DocumentMetadata() {
        super();
    }

    public DocumentMetadata(String file, Date date, String user) {
        this(UUID.randomUUID().toString(), file, date, user);
    }
    
    public DocumentMetadata(String uuid, String file, Date date, String user) {
        super();
        this.uuid = uuid;
        this.file = file;
        this.date = date;
        this.user = user;
    }
    
    public DocumentMetadata(Properties properties) {
        this(properties.getProperty(PROP_UUID), properties.getProperty(PROP_FILE), null, properties.getProperty(PROP_USER));
        String dateString = properties.getProperty(PROP_DATE);
        if(dateString!=null) {
            try {
                this.date = DATE_FORMAT.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }    
    }

    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Properties createProperties() {
        Properties props = new Properties();
        props.setProperty(PROP_UUID, getUuid());
        props.setProperty(PROP_FILE, getFile());
        props.setProperty(PROP_USER, getUser());
        props.setProperty(PROP_DATE, DATE_FORMAT.format(getDate()));
        return props;
    }
}
