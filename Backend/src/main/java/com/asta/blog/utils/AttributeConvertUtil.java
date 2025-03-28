package com.asta.blog.utils;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AttributeConvertUtil {
    @Named("stringToList")
    public List<String> stringToList(String tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        return Arrays.asList(tags.split("，"));
    }

    @Named("listToString")
    public String listToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            str.append(tags.get(i));
            if (i < tags.size() - 1) {
                str.append("，");
            }
        }
        return str.toString();
    }
}
