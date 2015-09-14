/*
 * Copyright 2014 Kenshoo.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kenshoo.freemarker.model;

import java.util.Map;

/**
 * Created by Pmuruge on 8/29/2015.
 */
public class ExecuteResponse {
    private String result;
    private Map<ExecuteResourceErrorFields, String> problems;
    private boolean truncatedResult;

    public ExecuteResponse(String result, Map<ExecuteResourceErrorFields, String> problems, boolean truncatedResult) {
        this.result = result;
        this.problems = problems;
        this.truncatedResult = truncatedResult;
    }

    public ExecuteResponse() {

    }

    public Map<ExecuteResourceErrorFields, String> getProblems() {
        return problems;
    }

    public void setProblems(Map<ExecuteResourceErrorFields, String> problems) {
        this.problems = problems;
    }

    public boolean isTruncatedResult() {
        return truncatedResult;
    }

    public void setTruncatedResult(boolean truncatedResult) {
        this.truncatedResult = truncatedResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
