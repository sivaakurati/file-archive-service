/**
 * 
 */
package com.enuminfo.archive.service;

import java.util.Date;
import java.util.List;

import com.enuminfo.archive.dto.Document;
import com.enuminfo.archive.dto.DocumentMetadata;

/**
 * @author Kumar
 */
public interface IArchiveService {

	DocumentMetadata add(Document document);
	List<DocumentMetadata> loadDocuments(String user, Date date);
	byte[] loadDocumentFile(String id);
}
