package org.studnia.general.DTOs;
import jdk.jshell.spi.ExecutionControl;

import java.util.HashMap;

public class DTO {
    public HashMap<String,String> toHashMap() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("toHashMap() not implemented.");
    }
}
