/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.vxquery.exceptions;

import java.text.MessageFormat;

import org.apache.vxquery.util.SourceLocation;

public class SystemException extends Exception {
    private static final long serialVersionUID = 1L;

    private ErrorCode code;    
    
    private static String message(ErrorCode code, SourceLocation loc) {
        return code + ": " + (loc == null ? "" : loc + " ") + code.getDescription();
    }
    
    private static String message(ErrorCode code, SourceLocation loc, Object... params) {
        return code + ": " + (loc == null ? "" : loc + " ") + MessageFormat.format(code.getDescription(), params);
    }
    
    public SystemException(ErrorCode code) {
        super(message(code, null));
        this.code = code;
    }

    public SystemException(ErrorCode code, Object... params) {
        super(message(code, null, params));
        this.code = code;
    }

    public SystemException(ErrorCode code, Throwable cause) {
        super(message(code, null), cause);
        this.code = code;
    }
    
    public SystemException(ErrorCode code, Throwable cause, Object... params) {
        super(message(code, null, params), cause);
        this.code = code;
    }
    
    public SystemException(ErrorCode code, SourceLocation loc) {
        super(message(code, loc));
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}