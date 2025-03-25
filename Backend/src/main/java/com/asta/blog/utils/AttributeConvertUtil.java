package com.asta.blog.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.micrometer.common.util.StringUtils;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class AttributeConvertUtil {

    @Named("strToList")
    public List<String> strToList(String str) {
        if (StringUtils.isNotEmpty(str)) {
            return new Gson().fromJson(str, new TypeToken<List<String>>(){}.getType());
        }
        return null;
    }

    /**
     * dto中的list转为po中的String
     */
    @Named("listToStr")
    public String listToStr(List<String> managerList) {
        if (!CollectionUtils.isEmpty(managerList)) {
            return new Gson().toJson(managerList);
        }
        return null;
    }
}