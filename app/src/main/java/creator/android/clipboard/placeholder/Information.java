package creator.android.clipboard.placeholder;

public class Information {
    private String name, details;
    private Integer id;
    private Integer account_id;
    private String updatedAt, createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public Information setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Information setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Integer getAccountId() {
        return account_id;
    }

    public Integer getId() {
        return id;
    }


    public String getDetails() {
        return details;
    }

    public Information setDetails(String details) {
        this.details = details;
        return this;
    }

    public String getName() {
        return name;
    }

    public Information setName(String name) {
        this.name = name;
        return this;
    }

    public Information(Integer account_id) {
        this.account_id = account_id;
    }

    public Information setId(Integer id) {
        this.id = id;
        return this;
    }
}
