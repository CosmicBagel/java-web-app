package com.blegh.simplewebapp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * Created by Samuel on 1/18/2016.
 */
public class PostBodyParser {
    public HashMap<String,String> ParseDatShit (String postBody) throws UnsupportedEncodingException {
        //postBody uses url encoding, but spaces are + signs
        //we get this string "name=qwerty+12356&age=555+wut"
        //we want key values "name": "qwerty 123456", "age": "555 wut"
        //and support for multiple params (& separator)
        //and must correctly identify URL encoded characters (eg '+' is '%2B', ignoring quotes of course)
        //reference http://www.w3schools.com/tags/ref_urlencode.asp
        //there's probably something that can handle url encodes already

        HashMap<String,String> output = new HashMap<>();

        //name=qwerty+12356&age=555+wut
        String params[] = postBody.split("&");
        for (String p : params) {
            //name=qwerty+12345
            String pKeyValue[] = p.split("=");
            if (pKeyValue.length == 2) {
                String key = pKeyValue[0];//name
                String value = pKeyValue[1];//qwerty+12345

                value = value.replace('+', ' ');//qwerty 12345

                //url decodes here
                value = URLDecoder.decode(value, "UTF-8");

                output.put(key,value);
            } else
                System.out.println("PostBodyParser: bad param format");
        }

        return output;
    }
}