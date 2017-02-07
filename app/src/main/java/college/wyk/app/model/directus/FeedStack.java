package college.wyk.app.model.directus;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class FeedStack {

    public int currentPage;
    public List<DirectusPost> items;
    public boolean noMoreUpdates;

    public FeedStack() {
    }

    public FeedStack(int currentPage, List<DirectusPost> items, boolean noMoreUpdates) {
        this.currentPage = currentPage;
        this.items = items;
        this.noMoreUpdates = noMoreUpdates;
    }

    public FeedStack copy(List<DirectusPost> items) {
        return new FeedStack(currentPage, items, noMoreUpdates);
    }

}
