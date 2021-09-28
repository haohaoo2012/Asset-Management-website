package com.nashtech.rootkies.dto.category.response;

import lombok.*;

@Data
public class BasicCategoryDTO {
    private String categoryCode;

    private String categoryName;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categoryCode == null) ? 0 : categoryCode.hashCode());
        result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BasicCategoryDTO other = (BasicCategoryDTO) obj;
        if (categoryCode == null) {
            if (other.categoryCode != null)
                return false;
        } else if (!categoryCode.equals(other.categoryCode))
            return false;
        if (categoryName == null) {
            if (other.categoryName != null)
                return false;
        } else if (!categoryName.equals(other.categoryName))
            return false;
        return true;
    }

}
