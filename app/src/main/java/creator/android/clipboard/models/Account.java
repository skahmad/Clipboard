package creator.android.clipboard.models;

public class Account {
    private String id = "";
    private String name = "";
    private Integer count = 0;
    private String createdAt = "";
    private String updatedAt = "";


    // todo remove id parameter form constructor
    public Account(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getIntId() {
        return Integer.parseInt(id);
    }
    public Account setCount(Integer count) {
        this.count = count;
        return this;
    }

    public Account setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Account setName(String name) {
        this.name = name;
        return this;
    }

    public Account setUpdatedAt(String updatedAt) {
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
