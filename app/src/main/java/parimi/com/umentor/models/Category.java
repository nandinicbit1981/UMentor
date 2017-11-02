package parimi.com.umentor.models;

/**
 * Created by nandpa on 9/17/17.
 */

public class Category {

    private String category;

    public Category(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        return this.category == ((Category) o).getCategory();
    }
}
