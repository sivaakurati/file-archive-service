/**
 * 
 */
package com.enuminfo.archive.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enuminfo.archive.dto.Document;
import com.enuminfo.archive.dto.DocumentMetadata;
import com.enuminfo.archive.repository.IDocumentRepository;
import com.enuminfo.archive.service.IArchiveService;

/**
 * @author Kumar
 */
@Service
public class ArchiveService implements IArchiveService {

	@Autowired
	IDocumentRepository documentRepository;
	
	@Override
	public DocumentMetadata add(Document document) {
		documentRepository.save(document);
		return document.getMetadata();
	}

	@Override
	public List<DocumentMetadata> loadDocuments(String user, Date date) {
		return documentRepository.findByUserAndDate(user, date);
	}

	@Override
	public byte[] loadDocumentFile(String id) {
		Document document = documentRepository.findByUUID(id);
        if(document != null) return document.getFileData();
        else return null;
	}
}
