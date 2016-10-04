/**
 * 
 */
package com.enuminfo.archive.repository.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.enuminfo.archive.dto.Document;
import com.enuminfo.archive.dto.DocumentMetadata;
import com.enuminfo.archive.repository.IDocumentRepository;

/**
 * @author Kumar
 */
@Service
public class DocumentRepository implements IDocumentRepository {

	public static final String DIRECTORY = "archive";
    public static final String META_DATA_FILE_NAME = "metadata.properties";
	
    @PostConstruct
    public void init() {
        createDirectory(DIRECTORY);
    }
    
	@Override
	public void save(Document document) {
		try {
            createDirectory(document);
            saveFileData(document);
            saveMetaData(document);
        } catch (IOException e) {
            String message = "Error while inserting document";
            throw new RuntimeException(message, e);
        }
	}

	@Override
	public List<DocumentMetadata> findByUserAndDate(String user, Date date) {
		try {
            return findInFileSystem(user, date);
        } catch (IOException e) {
            String message = "Error while finding document, name: " + user + ", date:" + date;
            throw new RuntimeException(message, e);
        }
	}

	@Override
	public Document findByUUID(String uuid) {
		try {
            return loadFromFileSystem(uuid);
        } catch (IOException e) {
            String message = "Error while loading document with id: " + uuid;
            throw new RuntimeException(message, e);
        }
	}
	
	private List<DocumentMetadata> findInFileSystem(String user, Date date) throws IOException  {
		List<String> uuidList = getUuidList();
		List<DocumentMetadata> metadataList = new ArrayList<DocumentMetadata>(uuidList.size());
		for (String uuid : uuidList) {
			DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);
            if(isMatched(metadata, user, date)) metadataList.add(metadata);
        }
        return metadataList;
    }

    private boolean isMatched(DocumentMetadata metadata, String personName, Date date) {
        if(metadata == null) return false;
        boolean match = true;
        if(personName != null) match = (personName.equals(metadata.getUser()));
        if(match && date != null) match = (date.equals(metadata.getDate()));
        return match;
    }

    private DocumentMetadata loadMetadataFromFileSystem(String uuid) throws IOException {
        DocumentMetadata document = null;
        String dirPath = getDirectoryPath(uuid);
        File file = new File(dirPath);
        if(file.exists()) {
            Properties properties = readProperties(uuid);
            document = new DocumentMetadata(properties);
        } 
        return document;
    }
    
    private Document loadFromFileSystem(String uuid) throws IOException {
       DocumentMetadata metadata = loadMetadataFromFileSystem(uuid);
       if(metadata == null) {
           return null;
       }
       Path path = Paths.get(getFilePath(metadata));
       Document document = new Document(metadata);
       document.setFileData(Files.readAllBytes(path));
       return document;
    }

    private String getFilePath(DocumentMetadata metadata) {
        String dirPath = getDirectoryPath(metadata.getUuid());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dirPath).append(File.separator).append(metadata.getFile());
        return stringBuilder.toString();
    }
    
    private void saveFileData(Document document) throws IOException {
        String path = getDirectoryPath(document);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(new File(path), document.getFile())));
        bufferedOutputStream.write(document.getFileData());
        bufferedOutputStream.close();
    }
    
    public void saveMetaData(Document document) throws IOException {
    	String path = getDirectoryPath(document);
    	Properties props = document.createProperties();
    	File file = new File(new File(path), META_DATA_FILE_NAME);
    	OutputStream outputStream = new FileOutputStream(file);
    	props.store(outputStream, "Document meta data");       
    }
    
    private List<String> getUuidList() {
        File file = new File(DIRECTORY);
        String[] directories = file.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});
        return Arrays.asList(directories);
    }
    
    private Properties readProperties(String uuid) throws IOException {
        Properties prop = new Properties();
        InputStream inputStream = null;     
        try {
            inputStream = new FileInputStream(new File(getDirectoryPath(uuid), META_DATA_FILE_NAME));
            prop.load(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }
    
    private String createDirectory(Document document) {
        String path = getDirectoryPath(document);
        createDirectory(path);
        return path;
    }

    private String getDirectoryPath(Document document) {
       return getDirectoryPath(document.getUuid());
    }
    
    private String getDirectoryPath(String uuid) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DIRECTORY).append(File.separator).append(uuid);
        return stringBuilder.toString();
    }

    private void createDirectory(String path) {
        File file = new File(path);
        file.mkdirs();
    }
}
