package com.lh.auto.validcode.service;

import com.lh.auto.validcode.model.ValidCode;

import java.io.IOException;
import java.io.OutputStream;

public interface ValidCodeService {
    ValidCode resultValidCode() throws IOException;
    public String createValidCode(OutputStream outputStream, String imageType) throws IOException;
    public String createValidCode(OutputStream outputStream) throws IOException ;
}
