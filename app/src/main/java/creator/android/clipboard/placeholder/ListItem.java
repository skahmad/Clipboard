package creator.android.clipboard.placeholder;

public class ListItem {
    private String title;
    private String description;

    public ListItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
