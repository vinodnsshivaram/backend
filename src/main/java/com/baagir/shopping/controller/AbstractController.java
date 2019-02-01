package com.baagir.shopping.controller;

import com.baagir.shopping.Constants;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.util.Translate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.ws.rs.BadRequestException;
import java.util.*;

public abstract class AbstractController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    public static final String SELF_LINK = "self";
    public static final String PREV_LINK = "prev";
    public static final String NEXT_LINK = "next";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";

    @Autowired
    protected Environment environment;



    //Validates the filter format "filter={key}::{value}"
    protected void validateFilter(Map<String, String> queryStringMap) {

        String keyValue[], filterPairs[];

        log.info("QueryString map : "+queryStringMap);

        if (queryStringMap.containsKey(Constants.QueryString.FILTER)) {
            String filter = queryStringMap.get(Constants.QueryString.FILTER);
            if (!filter.contains(Constants.QueryString.FILTER_DELIMITER)) {
                throw new BadRequestException("Invalid filter. Filter must be in the form filter={key}::{value}");
            }

            //For QA,finding the specific occurences of %7C ,the encoded value of the pipe delimiter (|), and decoding/replacing it back here from the url.
            if(filter.split("%7C").length > 0) {
                log.info("%7C match found");
                filter = filter.replaceAll("%7C",Constants.QueryString.FILTER_SEPARATOR);
            }

            //Validating the existence of a valid filter separator.
            if(filter.split(Constants.QueryString.FILTER_SEPARATOR).length > 1) {
                keyValue = filter.split(Constants.QueryString.FILTER_SEPARATOR);
                for(String pair : keyValue){
                    filterPairs = pair.split(Constants.QueryString.FILTER_DELIMITER);
                    if((filterPairs.length % 2) > 0)
                        throw new BadRequestException("Only '|' is allowed as a filter separator.");
                }
            } else {
                keyValue = filter.split(Constants.QueryString.FILTER_DELIMITER);

                if ((keyValue.length % 2) != 0) {
                    throw new BadRequestException("Only '|' is allowed as a filter separator.");
                }
            }
        } else
            throw new BadRequestException("Missing a filter. Filter must be in the form 'filter={key}::{value}' ");
    }

    //Traverses through the request filters and returns a LinkedHashMap of <key,value> pairs.
    protected LinkedHashMap<String,String> parseFilter(String filterPart) {

        String filterPairs[], keyValue[];
        LinkedHashMap<String,String> filterKeyValueMap = new LinkedHashMap<String, String>();

        log.info("FILTER PART : "+filterPart);

        //For QA,finding the specific occurrences of %7C ,the encoded value of the pipe delimiter (|), and decoding/replacing it back here from the url.
        if(filterPart.split("%7C").length > 0) {
            log.info("%7C match found");
            filterPart = filterPart.replaceAll("%7C",Constants.QueryString.FILTER_SEPARATOR);
        }

        if(filterPart.split(Constants.QueryString.FILTER_SEPARATOR).length > 1) {
            filterPairs = filterPart.split(Constants.QueryString.FILTER_SEPARATOR);

            for(String pair : filterPairs) {
                keyValue = pair.split(Constants.QueryString.FILTER_DELIMITER);
                if((keyValue.length % 2) > 0)
                    throw new BadRequestException("Invalid filter format. Filter must be in the form filter={key}::{value}");
                else
                    filterKeyValueMap.put(keyValue[0],keyValue[1]);
            }
        } else {
            keyValue = filterPart.split(Constants.QueryString.FILTER_DELIMITER);
            if((keyValue.length % 2) > 0)
                throw new BadRequestException("Invalid filter format. Filter must be in the form filter={key}::{value}");
            else if(keyValue.length == 0)
                throw new BadRequestException("Invalid filter format. Filter must be in the form filter={key}::{value}");
            else
                filterKeyValueMap.put(keyValue[0],keyValue[1]);
        }

        return filterKeyValueMap;
    }


    public void iterateMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof String) {
                encode(entry.getValue(), entry);
            } else if(entry.getValue() != null && entry.getValue() instanceof HashMap){
                iterateMap((Map<String, Object>) entry.getValue());
            } else {
                if(entry.getValue() != null && entry.getValue() instanceof ArrayList)
                    iterateArray(entry.getValue());
            }
        }
    }

    public void iterateArray(Object array) {
        for (Object obj : (ArrayList) array) {
            for (Iterator it = ((LinkedHashMap<Object, Object>) obj).entrySet().iterator(); it.hasNext(); ) {
                Map.Entry objEntry = (Map.Entry) it.next();
                Object objValue = objEntry.getValue();

                if(objValue != null && objValue instanceof String){
                    encode(objValue, objEntry);
                } else if(objValue != null && objValue instanceof ArrayList){
                    iterateArray(objValue);
                } else {
                    if(objValue != null && objValue instanceof HashMap)
                        iterateMap((Map<String, Object>) objValue);
                }
            }
        }
    }



    protected void encode(Object object, Map.Entry objectEntry) {
        StringBuilder builder = new StringBuilder();
        builder.append(object);

        for (int i = 0; i < object.toString().toCharArray().length; i++) {
            char currentChar = object.toString().charAt(i);
            if (Character.UnicodeBlock.of(currentChar) != Character.UnicodeBlock.BASIC_LATIN) {
                log.info("ENCODED CHAR : " + currentChar);
                String encodedChar = Translate.encode(currentChar);
                builder.replace(i, i + 1, encodedChar);
                log.info("MODIFIED STRING : " + builder.toString());
                object = builder.toString();
                objectEntry.setValue(object);
            }
        }
    }

    public Map<String, String> getLimitAndOffsetValue(Map<String, String> filter) {
        Map<String, String> limitOffsetVal = new HashMap<>();
        int defaultCount = Integer.parseInt(environment.getProperty("pagination.limit.default"));
        Integer limit = 0;
        Integer offset = 0;
        String before = null;
        String after = null;

        if(filter!= null && filter.containsKey(Constants.Url.NOTIFICATIONS_LIMIT)) {
            String limitVal = filter.get(Constants.Url.NOTIFICATIONS_LIMIT);
            limit = !StringUtils.isEmpty(limitVal) ? Integer.parseInt(limitVal) : defaultCount;
        } else {
            limit = defaultCount;
        }

        if(filter!= null && filter.containsKey(Constants.Url.NOTIFICATIONS_OFFSET)) {
            String offsetVal = filter.get(Constants.Url.NOTIFICATIONS_OFFSET);
            offset = !StringUtils.isEmpty(offsetVal) ? Integer.parseInt(offsetVal) : 0;

            if(offset < 0)
                throw new BadRequestException("Offset value should always be equal to or more than 0");
        }

        if(filter!= null && filter.containsKey(Constants.Url.NOTIFICATIONS_BEFORE)) {
            String beforeVal = filter.get(Constants.Url.NOTIFICATIONS_BEFORE);
            before = !StringUtils.isEmpty(beforeVal) ? beforeVal : null;
        }

        if(filter!= null && filter.containsKey(Constants.Url.NOTIFICATIONS_AFTER)) {
            String afterVal = filter.get(Constants.Url.NOTIFICATIONS_AFTER);
            after = !StringUtils.isEmpty(afterVal) ? afterVal : null;
        }

        limitOffsetVal.put(Constants.Url.NOTIFICATIONS_LIMIT, limit.toString());
        limitOffsetVal.put(Constants.Url.NOTIFICATIONS_OFFSET, offset.toString());
        limitOffsetVal.put(Constants.Url.NOTIFICATIONS_BEFORE, before);
        limitOffsetVal.put(Constants.Url.NOTIFICATIONS_AFTER, after);

        return limitOffsetVal;
    }
}
