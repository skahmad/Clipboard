package creator.android.clipboard.placeholder;

public class ListItem {
    private String id = "";
    private String name = "";
    private Integer count = 0;
    private String createdAt = "";
    private String updatedAt = "";


    public ListItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getIntId() {
        return Integer.parseInt(id);
    }
    public ListItem setCount(Integer count) {
        this.count = count;
        return this;
    }

    public ListItem setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ListItem setName(String name) {
        this.name = name;
        return this;
    }

    public ListItem setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
