/*
 * The Talking Data ORM Tool Open Foundation licenses
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 *
 * @ author: bingxin.li@tendcloud.com
 */
package com.talkingdata.orm.tool;

import org.apache.commons.lang.WordUtils;

public class ClassUtils {
    public static String capitalizeFully(String name){
        name = name.replace("-", " ").replace("_", " ");
        return WordUtils.capitalizeFully(name).replace(" ", "");
    }

    public static String lowerFirst(String name){
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
