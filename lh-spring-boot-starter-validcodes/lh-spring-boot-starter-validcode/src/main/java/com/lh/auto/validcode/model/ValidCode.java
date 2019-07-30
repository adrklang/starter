package com.lh.auto.validcode.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.ByteArrayOutputStream;

@Data
@Accessors(chain = true)
public class ValidCode {
    private String validStr;
    private ByteArrayOutputStream validSteam;
}
