package com.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;

import org.primefaces.model.UploadedFile;

public class Uploader {

	public String upload(UploadedFile file,String path) throws IOException{
    	try (InputStream input = file.getInputstream()) {
            Date date = new Date();
            long datetime = date.getTime();
            Files.copy(input, new File(path + file.getFileName()).toPath());
            File fileper = new File(path + file.getFileName());
    	    fileper.setReadable(true, false);
    	    fileper.setWritable(true, false);
    	    //setSshkeypath("/Users/darrenw/dicegui/DICEFITGUI/Uploads/"+ datetime + file.getFileName());
	    	 String SSHKeyPath = path + datetime + file.getFileName();

    	   return SSHKeyPath;
    	}     
	}
}