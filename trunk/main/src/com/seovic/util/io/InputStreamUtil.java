/*
 * Copyright 2009 Aleksandar Seovic
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

package com.seovic.util.io;


import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


/**
 * @author Aleksandar Seovic  2009.09.20
 */
public class InputStreamUtil
    {
    public static String readFullyAsString(InputStream in)
        {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder  sb     = new StringBuilder();

        String line;
        try
            {
            while ((line = reader.readLine()) != null)
                {
                sb.append(line).append('\n');
                }
            }
        catch (IOException e)
            {
            throw new RuntimeException(e);
            }
        finally
            {
            try
                {
                in.close();
                }
            catch (IOException ignore)
                {
                }
            }
        return sb.toString();
        }
    }
