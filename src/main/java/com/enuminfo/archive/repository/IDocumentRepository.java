/**
 * 
 */
package com.enuminfo.archive.repository;

import java.util.Date;
import java.util.List;

import com.enuminfo.archive.dto.Document;
import com.enuminfo.archive.dto.DocumentMetadata;

/**
 * @author Kumar
 */
public interface IDocumentRepository {

	void save(Document document);
	List<DocumentMetadata> findByUserAndDate(String user, Date date);
	Document findByUUID(String uuid);
}
