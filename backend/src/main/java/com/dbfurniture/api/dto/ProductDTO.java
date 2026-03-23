package com.dbfurniture.api.dto;

import java.util.List;

public class ProductDTO {
    public String id;
    public String categoryId;
    public String title;
    public String summary;
    public String materialsCraft;
    public String specs;
    public List<String> imageIds;
    public Boolean isActive;
    public Integer sort;
}

