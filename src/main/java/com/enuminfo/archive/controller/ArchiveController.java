/**
 * 
 */
package com.enuminfo.archive.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.enuminfo.archive.dto.Document;
import com.enuminfo.archive.dto.DocumentMetadata;
import com.enuminfo.archive.service.IArchiveService;

/**
 * @author Kumar
 */
@Controller
@RequestMapping(value = "/archive")
public class ArchiveController {

	@Autowired
	IArchiveService archiveService;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody DocumentMetadata handleInternalRequestForUploadFile(@RequestParam (value = "file", required = true) MultipartFile file, 
			@RequestParam(value = "user", required = true) String user,
            @RequestParam(value = "date", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		try {
            Document document = new Document(file.getBytes(), file.getOriginalFilename(), date, user);
            archiveService.add(document);
            return document.getMetadata();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	@RequestMapping(value = "/documents", method = RequestMethod.GET)
	public HttpEntity<List<DocumentMetadata>> handleInternalRequestForDownloadFiles(@RequestParam(value = "user", required = false) String user,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		HttpHeaders httpHeaders = new HttpHeaders();
        return new ResponseEntity<List<DocumentMetadata>>(archiveService.loadDocuments(user, date), httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/document/{id}", method = RequestMethod.GET)
    public HttpEntity<byte[]> handleInternalRequestForFile(@PathVariable String id) { 
    	HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(archiveService.loadDocumentFile(id), httpHeaders, HttpStatus.OK);
    }
}
